package data.services.mom

import org.apache.activemq.ActiveMQConnection
import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.Destination
import javax.jms.Session

class Publisher(val topic: String, val message: String) {

    init {
        init()
    }

    private fun init() {

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
            val dest: Destination = session.createTopic(topic)

            /*
             * Criando Produtor
             */
            val publisher = session.createProducer(dest)
            val message = session.createTextMessage()
            message.text = this.message

            /*
             * Publicando Mensagem
             */
            publisher.send(message)

            publisher.close()
            session.close()
            connection.close()

        } catch (e: Exception) {
            print(e.message)
        }

    }

    companion object {
        /*
     * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor est� em localhost
    */
        private val url = ActiveMQConnection.DEFAULT_BROKER_URL
    }
}