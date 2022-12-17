package data.services.tuples

import net.jini.core.lookup.ServiceTemplate
import net.jini.discovery.DiscoveryEvent
import net.jini.discovery.DiscoveryListener
import net.jini.discovery.LookupDiscovery
import java.io.IOException
import java.rmi.RemoteException
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * A class which supports a simple JINI multicast lookup.  It doesn't register
 * with any ServiceRegistrars it simply interrogates each one that's
 * discovered for a ServiceItem associated with the passed interface class.
 * i.e. The service needs to already have registered because we won't notice
 * new arrivals. [ServiceRegistrar is the interface implemented by JINI
 * lookup services].
 *
 * @todo Be more dynamic in our lookups - see above
 * @author  Dan Creswell (dan@dancres.org)
 * @version 1.00, 7/9/2003
 */
class LookupTuples internal constructor(aServiceInterface: Class<*>) : DiscoveryListener {
    private val theTemplate: ServiceTemplate
    private var theDiscoverer: LookupDiscovery? = null
    private var theProxy: Any? = null
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    /**
     * @param aServiceInterface the class of the type of service you are
     * looking for.  Class is usually an interface class.
     */
    init {
        val myServiceTypes = arrayOf(aServiceInterface)
        theTemplate = ServiceTemplate(null, myServiceTypes, null)
    }

    val service: Any
        /**
         * Having created a Lookup (which means it now knows what type of service
         * you require), invoke this method to attempt to locate a service
         * of that type.  The result should be cast to the interface of the
         * service you originally specified to the constructor.
         *
         * @return proxy for the service type you requested - could be an rmi
         * stub or an intelligent proxy.
         */
        get() {
            lock.withLock {
                if (theDiscoverer == null) {
                    try {
                        theDiscoverer = LookupDiscovery(LookupDiscovery.ALL_GROUPS)
                        theDiscoverer!!.addDiscoveryListener(this)
                    } catch (anIOE: IOException) {
                        System.err.println("Failed to init lookup")
                        anIOE.printStackTrace(System.err)
                    }
                }
            }
            return waitForProxy()
        }

    /**
     * Location of a service causes the creation of some threads.  Call this
     * method to shut those threads down either before exiting or after a
     * proxy has been returned from getService().
     */
    fun terminate() {
        synchronized(this) { if (theDiscoverer != null) theDiscoverer!!.terminate() }
    }

    /**
     * Caller of getService ends up here, blocked until we find a proxy.
     *
     * @return the newly downloaded proxy
     */
    private fun waitForProxy(): Any {
        lock.withLock {
            while (theProxy == null) {
                try {
                    condition.await()
                } catch (anIE: InterruptedException) {
                }
            }
            return theProxy!!
        }
    }

    /**
     * Invoked to inform a blocked client waiting in waitForProxy that
     * one is now available.
     *
     * @param aProxy the newly downloaded proxy
     */
    private fun signalGotProxy(aProxy: Any) {
        lock.withLock {
            if (theProxy == null) {
                theProxy = aProxy
                condition.signal()
            }
        }
    }

    /**
     * Everytime a new ServiceRegistrar is found, we will be called back on
     * this interface with a reference to it.  We then ask it for a service
     * instance of the type specified in our constructor.
     */
    override fun discovered(anEvent: DiscoveryEvent) {
        lock.withLock { if (theProxy != null) return }
        val myRegs = anEvent.registrars
        for (i in myRegs.indices) {
            val myReg = myRegs[i]
            var myProxy: Any?
            try {
                myProxy = myReg.lookup(theTemplate)
                if (myProxy != null) {
                    signalGotProxy(myProxy)
                    break
                }
            } catch (anRE: RemoteException) {
                System.err.println("ServiceRegistrar barfed")
                anRE.printStackTrace(System.err)
            }
        }
    }

    /**
     * When a ServiceRegistrar "disappears" due to network partition etc.
     * we will be advised via a call to this method - as we only care about
     * new ServiceRegistrars, we do nothing here.
     */
    override fun discarded(anEvent: DiscoveryEvent) {}
}