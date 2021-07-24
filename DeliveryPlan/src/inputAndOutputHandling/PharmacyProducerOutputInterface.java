package inputAndOutputHandling;

import java.util.List;
import outputElements.OrdersSet;

public interface PharmacyProducerOutputInterface {

    public void saveResults(List<OrdersSet> orders, String filePath);
}
