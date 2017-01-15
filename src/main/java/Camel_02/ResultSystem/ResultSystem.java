package Camel_02.ResultSystem;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class ResultSystem {

    public static void main(String[] args) {

            from("activemq:queue:validateOrders")
                    .aggregate(constant(0), new CountingAggregation()).completionInterval(5) .to("stream:out")
                    .end()
        } };
    }
}
