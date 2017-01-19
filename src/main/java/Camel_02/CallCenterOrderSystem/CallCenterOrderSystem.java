package Camel_02.CallCenterOrderSystem;

import Camel_02.Shared.OrderFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by riccardosibani on 15/01/2017.
 *
 * Every 2 minutes, the class reads the callCenter.txt file and send its content to the queue
 */
public class CallCenterOrderSystem {

    private static OrderFactory orderFactory = new OrderFactory();
    private static CallCenterOrderTranslatorProcessor translator = new CallCenterOrderTranslatorProcessor();

    private static Processor idGenerator = new Processor() {
        private final String CODE = "CCOS";
        private int counter = 0;
        @Override
        public void process(Exchange exchange) throws Exception {
            String in = exchange.getIn().getBody(String.class);
            in += ", "+CODE+(counter++);
            exchange.getIn().setBody(in);
        }
    };
    public static void main(String[] args) {
        final int sleepSeconds = 100; //Change to 120

        CamelContext ctx = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);

        try {
        ctx.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:callCenterOrder?noop=true") // add ?noop=true
                        .split(body().tokenize("\n"))
                        .process(idGenerator)
                        .process(translator)
                        .to("stream:out")
                        .process(orderFactory)
                        .to("stream:out")
                        .multicast()
                        .to("activemq:queue:billingOrders")
                        .to("activemq:queue:inventoryOrders");

                }
            });

            while(true) {
                    ctx.start();
                    System.out.println("Start");

                    Thread.sleep(sleepSeconds * 1000);
                    ctx.stop();
                    System.out.println("End");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
