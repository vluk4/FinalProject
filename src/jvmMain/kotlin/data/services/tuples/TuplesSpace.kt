package data.services.tuples

import kotlinx.coroutines.coroutineScope
import net.jini.core.entry.UnusableEntryException
import net.jini.core.transaction.TransactionException
import net.jini.space.JavaSpace
import java.rmi.RemoteException
import kotlin.system.exitProcess

class TuplesSpace {
    private var finder: LookupTuples? = null
    private var space: JavaSpace? = null
    suspend fun init() {
        coroutineScope {
            try {
                println("Procurando pelo servico JavaSpace...")
                finder = LookupTuples(JavaSpace::class.java)
                space = finder?.service as JavaSpace
                if (space == null) {
                    println("O servico JavaSpace nao foi encontrado. Encerrando...")
                    exitProcess(-1)
                }
                println("O servico JavaSpace foi encontrado.")
                println("  ESPACO >>> $space")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun read(nome: String?): String? {
        val msg: Message
        val template = Message()
        try {
            template.nome = nome
            msg = space?.read(template, null, 500) as Message
        } catch (e: RemoteException) {
            e.printStackTrace()
            return null
        } catch (e: UnusableEntryException) {
            e.printStackTrace()
            return null
        } catch (e: TransactionException) {
            e.printStackTrace()
            return null
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return null
        }
        return msg.mensagem
    }

    fun take(nome: String?): String? {
        val msg: Message?
        val template = Message()
        try {
            template.nome = nome
            msg = space?.take(template, null, 500) as? Message
        } catch (e: RemoteException) {
            e.printStackTrace()
            return null
        } catch (e: UnusableEntryException) {
            e.printStackTrace()
            return null
        } catch (e: TransactionException) {
            e.printStackTrace()
            return null
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return null
        }
        return msg?.mensagem
    }

    fun write(nome: String?, mensagem: String?) {
        val tupla = Message()
        val template = Message()
        template.nome = nome
        try {
            tupla.nome = nome
            tupla.mensagem = mensagem
            space?.write(tupla, null, (60 * 60 * 1000).toLong())
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: TransactionException) {
            e.printStackTrace()
        }
    }
}