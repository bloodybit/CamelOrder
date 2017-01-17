package Camel_02.WebOrderSystem;

import Camel_02.Shared.OrderFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.util.Scanner;

/**
 * Created by riccardosibani on 15/01/2017.
 *
 *
 * Everytime a file "webOrder.txt" it is created, it is read, split by line and sent to its own queue
 */
public class WebOrderSystem {


    private static OrderFactory orderFactory = new OrderFactory();

    public static void main(String[] args) {
        CamelContext ctx = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);


        try {
            ctx.addRoutes(new RouteBuilder() {
                public void configure() {
                    from("stream:in")
                            .process(orderFactory)
                            .to("stream:out")
                            .to("activemq:queue:inventoryOrders");
                }
            });


            ctx.start();

            System.out.println("Start");
            Thread.sleep(10000);
            System.in.read();
            ctx.stop();


        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
