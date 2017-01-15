package Camel_02.CallCenterOrderSystem;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class CallCenterOrderTranslatorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String[] parts = exchange.getIn().getBody(String.class).split(",");
        String customerId = parts[0];
        String[] fullname = parts[1].split("\\s+");
        String name = fullname[1];
        String lastname = fullname[2];

        String numberOfSurferboards = parts[2];
        String numberOfDivingSuits = parts[3];

        exchange.getIn().setBody(name + ", " + lastname + ", " + numberOfSurferboards + ", " + numberOfDivingSuits + ", " + customerId);
    }
}
