package Camel_02.Shared;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Group G05
 * Riccardo Sibani (0382708)
 * Filippo Boiani (0387680)
 * Jin Hu (387514)
 * Said Ibrihen (386979)
 *
 * This class parses a String order and creates an Order object
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
        String orderId = parts[5];

        exchange.getIn().setBody(new Order(name, lastname, numberOfSurferboards, numberOfDivingSuits, customerId, orderId));
    }
}
