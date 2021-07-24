package fileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import outputElements.OrdersSet;

public class ResultDataSavier {

    List<OrdersSet> orders;
    int longestProductNameLength;
    int longestPharmacyNameLength;
    double totalCost;

    public ResultDataSavier(List<OrdersSet> orders) {
        this.orders = orders;
        setLongestProducentPharmacyNamesAndTotalCost();
    }

    public void saveToFile(String fileName) {
        createFile("Wyniki/" + fileName);
        try {
            FileWriter fileWriter = new FileWriter("Wyniki/" + fileName + ".txt");
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
        rowToWrite = rowToWrite + " [Koszt = " + order.getProductCost() + " * " + order.getAmountOfOrderedProducts() + " = " + orderCost + " zł]";
        return rowToWrite;
    }

    public File createFile(String fileName) {
        try {
            File file = new File(fileName + ".txt");
            if (file.createNewFile()) {
                System.out.println("Utworzno plik o nazwie: " + file.getName());
            } else {
                System.out.println("Plik: " + file.getName() + " został nadpisany.");
            }
            return file;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    void setLongestProducentPharmacyNamesAndTotalCost() {
        int producentLength = 0;
        int pharmacyLength = 0;
        double totalCost = 0;

        for (int i = 0; i < this.orders.size(); i++) {
            totalCost = totalCost + this.orders.get(i).getProductCost() * this.orders.get(i).getAmountOfOrderedProducts();

            String producentName = this.orders.get(i).getProducerName();
            String pharmacyName = this.orders.get(i).getPharmacyName();

            if (producentName.length() > producentLength) {
                producentLength = producentName.length();
            }

            if (pharmacyName.length() > pharmacyLength) {
                pharmacyLength = pharmacyName.length();
            }
        }

        this.longestProductNameLength = producentLength;
        this.longestPharmacyNameLength = pharmacyLength;
        this.totalCost = totalCost;
    }

}
