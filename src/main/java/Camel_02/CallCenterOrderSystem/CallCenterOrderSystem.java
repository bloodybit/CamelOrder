package Camel_02.CallCenterOrderSystem;

import Camel_02.Shared.OrderFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Group G05
 * Riccardo Sibani (0382708)
 * Filippo Boiani (0387680)
 * Jin Hu (387514)
 * Said Ibrihen (386979)
 *
 * Every 2 minutes, the class generates a orderfile and sends it to the Billing and Invetory System
 */
public class CallCenterOrderSystem {

    // A caouter used to name the file
    private static int fileCounter = 0;
    private static final String FILE_PREFIX = "call-center-order-";
    private static final String FILE_PATH = "callCenterOrder/";
    private static final int SLEEP_SECS = 20; //Change to 120

    // A factory that creates the order object
    private static OrderFactory orderFactory = new OrderFactory();

    // A Translator that translate the Order string to get the right layout
    private static CallCenterOrderTranslatorProcessor translator = new CallCenterOrderTranslatorProcessor();

    // This processor generates an ID for each order
    private static Processor idGenerator = new Processor() {

        // Prefix for the medium used
        private final String CODE = "CCOS";
        private int counter = 0;

        @Override
        public void process(Exchange exchange) throws Exception {

            // Create the code and add to the message body
            String in = exchange.getIn().getBody(String.class);
            in += ", "+CODE+(counter++);
            exchange.getIn().setBody(in);
        }
    };

    // Create a file
    private static void createFile(String fileName, int numOfRows){
        try{
            Path file = Paths.get(FILE_PATH+fileName+".txt");

            PrintWriter writer = new PrintWriter(file.toAbsolutePath().toString(), "UTF-8");

            for (int i=0; i< numOfRows; i++){
                writer.println(createRandomOrder());

            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    // Generates a Random User
    private static String createRandomOrder() {
        String[] names = {"Philip", "Alice", "Bob", "Mark", "Joe", "Ann"};
        String[] surnames = {"Green", "Red", "Blue", "While", "Black", "Doe"};
        Random r = new Random();

        String order = r.nextInt(80) + ", "
                + names[r.nextInt(6)] +" "
                + surnames[r.nextInt(6)] + ","
                + r.nextInt(4) + ", "
                + r.nextInt(8);
        return order;
    }

    // Generates the filename
    private static String randomName() {
        return FILE_PREFIX + generateNum();
    }

    // Generates the number for the file name
    private static int generateNum(){
        return ++fileCounter;
    }

    public static void main(String[] args) {


        CamelContext ctx = new DefaultCamelContext();
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setTrustAllPackages(true);
        ctx.addComponent("activemq", activeMQComponent);

        System.out.println("Starting... ");
        System.out.println("It could require few seconds... ");

        try {
        ctx.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:callCenterOrder?delete=true")
                        .split(body().tokenize("\n"))
                        .process(idGenerator)
                        .process(translator)
                        .to("stream:out")
                        .process(orderFactory)
                        .to("stream:out")
                        .multicast()
                        .to("activemq:queue:billingOrders")
                        .to("activemq:queue:inventoryOrders");

                }
            });

            ctx.start();
            start();
            System.in.read(); // wait till ENTER pressed
            ctx.stop();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void start(){
        while(true) {

            try {
                Thread.sleep(SLEEP_SECS * 1000);
                createFile(randomName(), new Random().nextInt(20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}
