package Camel_02.ResultSystem;

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


    public static class CountingAggregation implements AggregationStrategy {
        private int count = 0;

        @Override
        public Exchange aggregate(Exchange exchange, Exchange exchange1) {
            count++;
            exchange1.getIn().setBody(count);
            return exchange1;
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
                            .choice()
                            .when(header("validated"))
                            .filter(method(orderFilter, "isValid"))
                            .aggregate(constant(0), new CountingAggregation()).completionInterval(5)
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
