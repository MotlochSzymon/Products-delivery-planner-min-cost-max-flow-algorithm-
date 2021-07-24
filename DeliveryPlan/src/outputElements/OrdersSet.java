package outputElements;

public class OrdersSet {

    int producerId;
    String producerName;
    String pharmacyName;
    Double productCost;
    int amountOfOrderedProducts;

    public OrdersSet(int producerId, String producentName, String pharmacyName, Double productCost, int amountOfOrderedProducts) {
        this.producerId = producerId;
        this.producerName = producentName;
        this.pharmacyName = pharmacyName;
        this.productCost = productCost;
        this.amountOfOrderedProducts = amountOfOrderedProducts;
    }

    public int getProducerId() {
        return this.producerId;
    }

    public String getProducerName() {
        return this.producerName;
    }

    public String getPharmacyName() {
        return this.pharmacyName;
    }

    public Double getProductCost() {
        return this.productCost;
    }

    public int getAmountOfOrderedProducts() {
        return this.amountOfOrderedProducts;
    }
}
