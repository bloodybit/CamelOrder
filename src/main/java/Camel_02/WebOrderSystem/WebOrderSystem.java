package Camel_02.WebOrderSystem;

import Camel_02.Shared.OrderFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.util.Random;

/**
 * Group G05
 * Riccardo Sibani (0382708)
 * Filippo Boiani (0387680)
 * Jin Hu (387514)
 * Said Ibrihen (386979)
 *
 * WebOrderSystem class
 *
 * Automatically generates web orders and sends them to the Billing and Inventory Systems.
 */
public class WebOrderSystem {


    private static OrderFactory orderFactory = new OrderFactory();

    private static Processor idGenerator = new Processor() {
        private final String CODE = "WOS";
        private int counter = 0;
        @Override
        public void process(Exchange exchange) throws Exception {
            String in = exchange.getIn().getBody(String.class);
            in += ", "+CODE+(counter++);
            exchange.getIn().setBody(in);
        }
    };

    public static void main(String[] args) {
        CamelContext ctx = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);

        ProducerTemplate template = ctx.createProducerTemplate();

        System.out.println("Starting... ");
        System.out.println("It could require few seconds... ");

        RouteBuilder route = new RouteBuilder() {
            public void configure() {
                from("direct:in")
                        .process(idGenerator)
                        .process(orderFactory)
                        .to("stream:out")
                        .multicast()
                        .to("activemq:queue:billingOrders")
                        .to("activemq:queue:inventoryOrders");
            }
        };

        try {
            ctx.addRoutes(route);
            ctx.start();

            start(template);
            System.in.read();

            ctx.stop();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * start method
     * Generates random orders (at a random amount of time) and sends them to the standard input.
     *
     * @param tmp
     */
    public static void start(ProducerTemplate tmp) {
        Random r = new Random();

        while(true){

            tmp.sendBody("direct:in", createRandomOrder());

            try {

                Thread.sleep(r.nextInt(10)* 1000);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
        }

    }

    // Generates a Random User
    private static String createRandomOrder() {
        String[] names = {"Jane", "Simon", "Robert", "Bob", "Ron", "Sophie"};
        String[] surnames = {"Purple", "Orange", "Smith", "White", "Black", "Doe"};
        Random r = new Random();

        String order = names[r.nextInt(6)] +", "
                + surnames[r.nextInt(6)] + ","
                + r.nextInt(4) + ", "
                + r.nextInt(8) + "," +
                r.nextInt(80);
        return order;
    }

}
