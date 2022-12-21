package data.services.mom

import org.apache.activemq.ActiveMQConnection
import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.*

class Subscriber : MessageListener {

    private var session: Session? = null
    private var callback: ((String) -> Unit)? = null

    fun initSubscriber() {
        runCatching {

            val connectionFactory = ActiveMQConnectionFactory(url)
            val connection = connectionFactory.createConnection()
            connection.start()

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        }.getOrElse {
            it.printStackTrace()
        }
    }

    fun subscribeToTopic(topic: String, onMessageCallback: (String) -> Unit) {
        runCatching {
            val dest: Destination = session!!.createTopic(topic)

            val subscriber = session?.createConsumer(dest)

            subscriber?.messageListener = this

            callback = onMessageCallback
        }.getOrElse {
            it.printStackTrace()
        }
    }

    override fun onMessage(message: Message) {
        if (message is TextMessage) {
            try {
                callback?.invoke(message.text)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    companion object {
        /*
     * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor est� em localhost
    */
        private val url = ActiveMQConnection.DEFAULT_BROKER_URL
    }
}
