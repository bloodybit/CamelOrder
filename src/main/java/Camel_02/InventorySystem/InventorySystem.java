package Camel_02.InventorySystem;

import Camel_02.Shared.Order;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class InventorySystem {
    public static void main(String[] args) {
        try {
            ActiveMQConnectionFactory conFactory = new ActiveMQConnectionFactory();
            conFactory.setTrustAllPackages(true);
            Connection con = conFactory.createConnection();

            final Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue inQueue = session.createQueue("inventoryOrders");
            Queue outQueue = session.createQueue("billingOrders");
            MessageConsumer consumer = session.createConsumer(inQueue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        Order order = (Order)((ObjectMessage)message).getObject();

                        System.out.println(order);

                        ObjectMessage answer = session.createObjectMessage(order);
                        boolean validated = Math.random() > 0.6;
                        answer.setBooleanProperty("validated", validated);

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
