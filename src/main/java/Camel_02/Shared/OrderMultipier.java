package Camel_02.Shared;

import org.apache.camel.builder.RouteBuilder;

/**
 * Group G05
 * Riccardo Sibani (0382708)
 * Filippo Boiani (0387680)
 * Jin Hu (387514)
 * Said Ibrihen (386979)
 *
 * OrderMultiplier class
 *
 * It just multiplies the messages
 */
public class OrderMultipier extends RouteBuilder{

    @Override
    public void configure() {
        from("direct:a")
                .multicast().to("direct:b", "direct:c");
    }
}
