package Camel_02.BillingSystem;

import Camel_02.Shared.Order;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 *
 * BillingSystem class
 *
 * Check if the user can pay for the order. The program takes the order message from the billingOrders
 * queue and sends the modified order to the resultOrders queue.
 * Created by GROUP 05 on 15/01/2017.
 */
public class BillingSystem {
    public static void main(String[] args) {
        try {
            // Create a connection to activeMQ
            ActiveMQConnectionFactory conFactory = new ActiveMQConnectionFactory();
            conFactory.setTrustAllPackages(true);
            Connection con = conFactory.createConnection();

            // Set up the session as well as the incoming queue and the outocming one
            final Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue inQueue = session.createQueue("billingOrders");
            Queue outQueue = session.createQueue("resultOrders");
            MessageConsumer consumer = session.createConsumer(inQueue);

            // Setting up the listener (publisher-subscriber)
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        Order order = (Order)((ObjectMessage)message).getObject();

                        // random validatio
                        boolean validated = Math.random() > 0.45;

                        // set the valid property and the answer
                        order.setValid(validated);
                        ObjectMessage answer = session.createObjectMessage(order);

                        // Set the answer header and send it.
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
