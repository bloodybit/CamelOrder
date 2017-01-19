package Camel_02.ResultSystem;

import Camel_02.Shared.Order;
import Camel_02.Shared.OrderFilter;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class ResultSystem {


    public static class ValidatingAggregation implements AggregationStrategy {
        private int count = 0;
        private boolean valid = true;
        private Order order = null;
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                System.out.print("First");
                // the first time we only have the new exchange so it wins the first round
                return newExchange;
            }
            System.out.print("Second");
            //order = oldExchange.getIn().getBody(Order.class);
            boolean param1 = oldExchange.getIn().getBody(Order.class).getValid();
            boolean param2 = newExchange.getIn().getBody(Order.class).getValid();

            if ( param1 && param2 ) {
                newExchange.getIn().getBody(Order.class).setValid(true);
            } else{
                newExchange.getIn().getBody(Order.class).setValid(false);
            }

            newExchange.getIn().setHeader("validated", newExchange.getIn().getBody(Order.class).getValid());

            return newExchange;
        }
    }

    public static void main(String[] args) {
        CamelContext ctx = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);


        try {
            ctx.addRoutes(new RouteBuilder() {
                public void configure() {

                    OrderFilter orderFilter = new OrderFilter();

                    from("activemq:queue:resultOrders")
                            .aggregate(header("orderId"), new ValidatingAggregation()).completionTimeout(1000)
                            .choice()
                            .when(header("validated"))
                            .filter(method(orderFilter, "isValid"))
                            .to("stream:out")
                            .end()
                            .endChoice().otherwise()
                            .to("stream:err");
                }
            });


            ctx.start();

            System.out.println("Start");
            System.in.read();
            ctx.stop();
            System.out.println("End");


        } catch (Exception e){
            e.printStackTrace();
        }


    }


}
