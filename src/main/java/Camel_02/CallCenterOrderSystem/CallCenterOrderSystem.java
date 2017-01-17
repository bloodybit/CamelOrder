package Camel_02.CallCenterOrderSystem;

import Camel_02.Shared.OrderFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by riccardosibani on 15/01/2017.
 *
 * Every 2 minutes, the class reads the callCenter.txt file and send its content to the queue
 */
public class CallCenterOrderSystem extends RouteBuilder {

    private static OrderFactory orderFactory = new OrderFactory();
    private static CallCenterOrderTranslatorProcessor translator = new CallCenterOrderTranslatorProcessor();

    public static void main(String[] args) {
        final int sleepSeconds = 20; //Change to 120

        CamelContext ctx = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);

        RouteBuilder callCenterOrderSystem = new CallCenterOrderSystem();

        while(true) {

            try {
                ctx.addRoutes(callCenterOrderSystem);
                ctx.start();
                System.out.println("Start");

                Thread.sleep(sleepSeconds * 1000);
                ctx.stop();
                System.out.println("End");
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void configure() throws Exception {
        from("file:callCenterOrder?noop=true") // add ?noop=true
                .split(body().tokenize("\n"))
                .to("stream:out")
                .process(translator)
                .to("stream:out")
                .process(orderFactory)
                .to("stream:out")
                .to("activemq:queue:inventoryOrders");
        //from("activemq:queue:callCenterOrder").to("stream:out");
    }
}
