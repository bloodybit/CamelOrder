package Camel_02.InventorySystem;

import Camel_02.Shared.Order;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by GROUP 05 on 15/01/2017.
 * Riccardo Sibani (0382708)
 * Filippo Boiani (0387680)
 * Jin Hu (387514)
 * Said Ibrihen (386979)
 *
 * InventorySystem class.
 * Checks if the order could be fullfilled and sends the result to the result system.
 */
public class InventorySystem {

    public static void main(String[] args) {

        try {
            // Create a connection to activeMQ
            ActiveMQConnectionFactory conFactory = new ActiveMQConnectionFactory();
            conFactory.setTrustAllPackages(true);
            Connection con = conFactory.createConnection();

            // Set up the session as well as the incoming queue and the outocming one
            final Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue inQueue = session.createQueue("inventoryOrders");
            Queue outQueue = session.createQueue("resultOrders");
            MessageConsumer consumer = session.createConsumer(inQueue);

            // Setting up the listener (publisher-subscriber)
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        Order order = (Order)((ObjectMessage)message).getObject();

                        // Random mock validator
                        boolean validated = Math.random() > 0.45;

                        // Set the valid property and creagte the answer message.
                        order.setValid(validated);
                        ObjectMessage answer = session.createObjectMessage(order);

                        // Set the anser header and send it
                        answer.setBooleanProperty("validated", validated);
                        answer.setStringProperty("orderId", order.getOrderId());
                        System.out.println(order);
                        MessageProducer producer = session.createProducer(outQueue);
                        producer.send(answer);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            con.start();
            System.in.read();
            con.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
