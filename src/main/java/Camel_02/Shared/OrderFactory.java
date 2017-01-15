package Camel_02.Shared;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class OrderFactory implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String[] parts = exchange.getIn().getBody(String.class).split(",");
        String name = parts[0];
        String lastname = parts[1];
        String numberOfSurferboards = parts[2];
        String numberOfDivingSuits = parts[3];
        String customerId = parts[4];

        exchange.getIn().setBody(new Order(name, lastname, numberOfSurferboards, numberOfDivingSuits, customerId));
    }
}
