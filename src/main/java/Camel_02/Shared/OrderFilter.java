package Camel_02.Shared;

/**
 * Created by riccardosibani on 17/01/2017.
 */
public class OrderFilter {

    public boolean isValid(Order order) {
        return order.getValid();
    }
}
