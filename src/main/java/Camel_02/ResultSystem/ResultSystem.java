package Camel_02.ResultSystem;

import Camel_02.Shared.Order;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * GORUP 05
 * Riccardo Sibani (0382708)
 * Filippo Boiani (0387680)
 * Jin Hu (387514)
 * Said Ibrihen (386979)
 *
 * ResultSystem class. It prints out the results. The orders are from the resultOrders queue
 */
public class ResultSystem {


    /**
     * ValidaitonAggregation
     * @implements AggregationStrategy
     *
     * Inner class that implements the aggergation strategy.
     */
    public static class ValidatingAggregation implements AggregationStrategy {

        // The valid property that needs to be checked
        private boolean valid = true;

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

            if (oldExchange == null) {
                // the first time we only have the new exchange so it wins the first round
                return newExchange;
            }
            // Otherwise we get the two orders
            boolean param1 = oldExchange.getIn().getBody(Order.class).getValid();
            boolean param2 = newExchange.getIn().getBody(Order.class).getValid();

            // ... and check if they are valid
            if ( param1 && param2 ) {
                newExchange.getIn().getBody(Order.class).setValid(true);

            } else{
                newExchange.getIn().getBody(Order.class).setValid(false);
            }

            // Set the validaiton property inside the header and return the Exchange
            newExchange.getIn().setHeader("validated", newExchange.getIn().getBody(Order.class).getValid());
            return newExchange;
        }
    }

    // The enricher adds information to the order. It's a proessor and it will be used to
    // process only the valid orders.
    private static Processor enricher = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            // Gets the order
            Order order = exchange.getIn().getBody(Order.class);
            Integer sb = Integer.parseInt(order.getNumberOfSurferboards());
            Integer ds = Integer.parseInt(order.getGetNumberOfDivingSuits());
            int tot = sb + ds;
            //... and sets the overall number of items ordered
            order.setOverallItems(""+tot);
            exchange.getIn().setBody(order);
        }
    };
    public static void main(String[] args) {

        // Camel context
        CamelContext ctx = new DefaultCamelContext();
        // ActiveMQ reader
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);


        try {
            ctx.addRoutes(new RouteBuilder() {
                public void configure() {

                    // This route takes the order form the resultOrder queue aggregates the orders with the same id
                    // that should be 2 (one from the billing system and one from the inventory system). If both orders
                    // are valid, displays them on the console.
                    from("activemq:queue:resultOrders")
                            .aggregate(header("orderId"), new ValidatingAggregation()).completionTimeout(1000)
                            .choice()
                                .when(header("validated"))
                                    .process(enricher)
                                    .to("stream:out")
                                .otherwise()
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
