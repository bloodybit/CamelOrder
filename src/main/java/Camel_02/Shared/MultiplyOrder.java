package Camel_02.Shared;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class MultiplyOrder extends RouteBuilder{

    @Override
    public void configure() {

        from("direct:a")
                .multicast().to("direct:b", "direct:c", "direct:d");
    }

}