package data.services.mom

import org.apache.activemq.ActiveMQConnection
import org.apache.activemq.ActiveMQConnectionFactory
import java.lang.Exception
import javax.jms.*

class Subscriber(val topico: String) : MessageListener {
    var mensagemRecebida = ""
        private set

    init {
        go()
    }

    private fun go() {
        try {
            /*
             * Estabelecendo conex�o com o Servidor JMS
             */
            val connectionFactory = ActiveMQConnectionFactory(url)
            val connection = connectionFactory.createConnection()
            connection.start()

            /*
             * Criando Session 
             */
            val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

            /*
             * Criando Topic
             */
            val dest: Destination = session.createTopic(topico)

            /*
             * Criando Consumidor
             */
            val subscriber = session.createConsumer(dest)

            /*
             * Setando Listener
             */
            subscriber.messageListener = this

        } catch (e: Exception) {
            print(e.message)
        }
    }


    override fun onMessage(message: Message) {
        if (message is TextMessage) {
            try {
                val msg = message.text
                mensagemRecebida = msg
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