package fileLogic;

import inputAndOutputHandling.PharmacyProducerOutputInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import outputElements.OrdersSet;

public class ResulFileSavier implements PharmacyProducerOutputInterface {

    List<OrdersSet> orders;
    int longestProductNameLength;
    int longestPharmacyNameLength;
    double totalCost;
    static final String fileNameEnd =  " - wynik.txt";

    public ResulFileSavier() {

    }

    @Override
    public void saveResults(List<OrdersSet> orders, String filePath) {
        this.orders = orders;
        setLongestProducerPharmacyNamesAndTotalCost();

        createResultFile(filePath);
        try {
            FileWriter fileWriter = new FileWriter(filePath + fileNameEnd);
            BufferedWriter out = new BufferedWriter(fileWriter);

            for (int i = 0; i < this.orders.size(); i++) {
                String orderRow = createRowToSave(this.orders.get(i));
                out.write(orderRow);
                out.newLine();
            }

            out.newLine();
            String sumUp = "Opłaty całkowite: " + this.totalCost + " zł";
            out.write(sumUp);

            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String createRowToSave(OrdersSet order) {
        String rowToWrite = "";
        rowToWrite = rowToWrite + order.getProducerName();

        for (int i = 0; i < longestProductNameLength - order.getProducerName().length(); i++) {
            rowToWrite = rowToWrite + " ";
        }

        rowToWrite = rowToWrite + " -> " + order.getPharmacyName();

        for (int i = 0; i < longestPharmacyNameLength - order.getPharmacyName().length(); i++) {
            rowToWrite = rowToWrite + " ";
        }

        double orderCost = order.getProductCost() * order.getAmountOfOrderedProducts();
        rowToWrite = rowToWrite + " [Koszt = " + order.getAmountOfOrderedProducts() + " * " +  order.getProductCost() + " = " + orderCost + " zł]";
        return rowToWrite;
    }

    public File createResultFile(String fileName) {
        try {
            File file = new File(fileName + fileNameEnd);
            if (file.createNewFile()) {
                System.out.println("Dane zostały zapisanego do nowego pliku o nazwie: " + file.getName());
            } else {
                System.out.println("Dane zostały zapisane w istniejącym pliku: " + file.getName());
            }
            return file;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    void setLongestProducerPharmacyNamesAndTotalCost() {
        int producerLength = 0;
        int pharmacyLength = 0;
        double totalCost = 0;

        for (int i = 0; i < this.orders.size(); i++) {
            totalCost = totalCost + this.orders.get(i).getProductCost() * this.orders.get(i).getAmountOfOrderedProducts();

            String producerName = this.orders.get(i).getProducerName();
            String pharmacyName = this.orders.get(i).getPharmacyName();

            if (producerName.length() > producerLength) {
                producerLength = producerName.length();
            }

            if (pharmacyName.length() > pharmacyLength) {
                pharmacyLength = pharmacyName.length();
            }
        }

        this.longestProductNameLength = producerLength;
        this.longestPharmacyNameLength = pharmacyLength;
        this.totalCost = totalCost;
    }

}
