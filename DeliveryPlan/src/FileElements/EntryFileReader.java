package fileElements;

import enums.TableOrderInEntryFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryFileReader {

    private List<VaccineProducent> vaccineProducentsList;
    private List<Pharmacy> pharmaciesList;
    private List<ProducentPharmacyConnection> producentPharmacyConnectionList;
    private int loadingTableNumber;
    private int lineNumber;
    private static final int NumberOfTables = 3;
    private static final int NumberOfColumnsInFirstTable = 3;
    private static final int NumberOfColumnsInSecondTable = 3;
    private static final int NumberOfColumnsInThirdTable = 4;
    private static final String VaccineProducentTableName = "Producenci szczepionek";
    private static final String PharmacyTableName = "Apteki";
    private static final String ProducentPharmacy = "Połączenie producentów z aptekami";

    public EntryFileReader() {
        this.vaccineProducentsList = new ArrayList<VaccineProducent>();
        this.pharmaciesList = new ArrayList<Pharmacy>();
        this.producentPharmacyConnectionList = new ArrayList<ProducentPharmacyConnection>();
        this.loadingTableNumber = 0;
        this.lineNumber = 0;
    }

    public void loadAllFileRows() throws FileNotFoundException, IOException, Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("Test data.txt")));
        String row;
        while ((row = br.readLine()) != null) {
            loadAndValidateRow(row);

            System.out.println(row);
        }

        checkLoadedTablesAmount();
    }

    private void checkLoadedTablesAmount() throws Exception {
        if (lineNumber == 0) {
            throw new java.lang.Exception("Podany plik jest pusty!");
        } else if (loadingTableNumber != NumberOfTables) {
            throw new java.lang.Exception("Nie podano wystarczającej liczby tabel");
        }
    }

    private void loadAndValidateRow(String row) throws Exception {
        lineNumber++;
        boolean isDescriptionRow = validateRowAndCheckIsDescriptionRow(row);

        String[] columnValues = row.split(" [|] ");

        if (!isDescriptionRow) {

            if (this.loadingTableNumber == TableOrderInEntryFile.VACCINE_PRODUCENTS_TABLE.getValue()) {
                loadVaccineProducentsRow(columnValues);
            } else if (this.loadingTableNumber == TableOrderInEntryFile.PHARMACIES_TABLE.getValue()) {
                loadPharmaciesRow(columnValues);
            } else if (this.loadingTableNumber == TableOrderInEntryFile.PRODUCENT_PHARMACY_TABLE.getValue()) {
                loadProducentPharmacyConnectionRow(columnValues);
            } else {
                throw new java.lang.Exception("Przed podaniem danych należy umieśić wiersz zawierający opis tabeli! Numer lini: " + this.lineNumber);
            }
        }
    }

    private void loadVaccineProducentsRow(String[] columnValues) throws Exception {
        checkNumberOfColumns(columnValues, NumberOfColumnsInFirstTable, VaccineProducentTableName);
        int producentId = loadColumnAsIntegerValue(columnValues[0], "Id producenta");
        String producentName = columnValues[1];
        int dailyProduction = loadColumnAsUnsignedValue(columnValues[2], "Dzienna produkcja");
        addProducentToList(producentId, producentName, dailyProduction);
    }

    private void loadPharmaciesRow(String[] columnValues) throws Exception {
        checkNumberOfColumns(columnValues, NumberOfColumnsInSecondTable, PharmacyTableName);
        int pharmacyId = loadColumnAsIntegerValue(columnValues[0], "Id apteki");
        String pharmacyName = columnValues[1];
        int dailyNeeds = loadColumnAsUnsignedValue(columnValues[2], "Dzienne zapotrzebowanie");
        addPharmacyToList(pharmacyId, pharmacyName, dailyNeeds);
    }

    private void loadProducentPharmacyConnectionRow(String[] columnValues) throws Exception {
        checkNumberOfColumns(columnValues, NumberOfColumnsInThirdTable, ProducentPharmacy);
        int producentId = loadColumnAsIntegerValue(columnValues[0], "Id producenta");
        int pharmacyId = loadColumnAsIntegerValue(columnValues[1], "Id apteki");
        int maxDailyDeliveredVaccines = loadColumnAsUnsignedValue(columnValues[2], "Dzienna maksymalna liczba dostarczanych szczepionek");
        double vaccineCost = loadColumnAsNonNegativeDoubleValue(columnValues[3], "Koszt szczepionki [zł]");
        addProducentPharmacyConnectionToList(producentId, pharmacyId, maxDailyDeliveredVaccines, vaccineCost);
    }

    private void checkNumberOfColumns(String[] columnValues, int validValue, String tableName) throws Exception {
        if (columnValues.length < validValue) {
            throw new java.lang.Exception("W wierszu tabeli " + tableName + " podano zbyt mało kolumn! Numer lini: " + this.lineNumber);
        }

        if (columnValues.length > validValue) {
            throw new java.lang.Exception("W wierszu tabeli " + tableName + " podano zbyt wiele kolumn! Numer lini: " + this.lineNumber);
        }
    }

    private int loadColumnAsIntegerValue(String valueToRead, String columnName) throws Exception {
        int columnValue = 0;
        try {
            columnValue = Integer.parseInt(valueToRead.trim());

        } catch (NumberFormatException nfe) {
            throw new java.lang.Exception("Wartośc kolumny: " + columnName + " musi być liczbą całkowitą! Numer lini: " + this.lineNumber);
        }

        return columnValue;
    }

    private double loadColumnAsNonNegativeDoubleValue(String valueToRead, String columnName) throws Exception {
        double columnValue = 0;
        try {
            columnValue = Double.parseDouble(valueToRead.trim().replace(',', '.'));

            if (columnValue < 0) {
                throw new java.lang.Exception("Wartośc kolumny: " + columnName + " nie może być liczbą ujemną! Numer lini: " + this.lineNumber);
            }

        } catch (NumberFormatException nfe) {
            throw new java.lang.Exception("Wartośc kolumny: " + columnName + "musi być liczbą rzeczywistą! Numer lini: " + this.lineNumber);
        }
        return columnValue;
    }

    private void addProducentToList(int producentId, String producentName, int dailyProduction) throws Exception {
        if (isAlreadyProducentAdded(producentId)) {
            throw new java.lang.Exception("Producent o id: " + producentId + " już został dodany! Numer lini: " + this.lineNumber);
        } else {
            this.vaccineProducentsList.add(new VaccineProducent(producentId, producentName, dailyProduction));
        }
    }

    private void addPharmacyToList(int pharmacyId, String pharmacyName, int dailyNeeds) throws Exception {
        if (isAlreadyPharmacyAdded(pharmacyId)) {
            throw new java.lang.Exception("Apteka o id: " + pharmacyId + " już została dodana! Numer lini: " + this.lineNumber);
        } else {
            this.pharmaciesList.add(new Pharmacy(pharmacyId, pharmacyName, dailyNeeds));
        }
    }

    private void addProducentPharmacyConnectionToList(int producentId, int pharmacyId, int maxDailyDeliveredVaccines, double vaccineCost) throws Exception {
        if (isAlreadyProducentPharmacyRelation(producentId, pharmacyId)) {
            throw new java.lang.Exception("Relacja producenta z apteką jest już dodana: " + this.lineNumber);
        }

        ProducentPharmacyConnection connectionToAdd = new ProducentPharmacyConnection(producentId, pharmacyId, maxDailyDeliveredVaccines, vaccineCost);
        this.producentPharmacyConnectionList.add(connectionToAdd);
    }

    private int loadColumnAsUnsignedValue(String valueToRead, String columnName) throws Exception {
        int columnValue = 0;
        try {
            columnValue = Integer.parseInt(valueToRead.trim());
            if (columnValue < 0) {
                throw new java.lang.Exception("Wartość kolumny: " + columnName + " musi być liczbą całkowitą dodatnią! Numer lini: " + this.lineNumber);
            }
        } catch (NumberFormatException nfe) {
            throw new java.lang.Exception("Wartość kolumny: " + columnName + " musi być liczbą całkowitą! Numer lini: " + this.lineNumber);
        }

        return columnValue;
    }

    private boolean isAlreadyPharmacyAdded(int pharamcyId) {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            if (this.pharmaciesList.get(i).getPharmacyId() == pharamcyId) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyProducentAdded(int producentId) {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            if (this.vaccineProducentsList.get(i).getProducentId() == producentId) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyProducentPharmacyRelation(int producentId, int pharmacyId) {
        boolean isRealtionAlready = false;
        int relationProducentId;
        int relationPharmacyId;

        for (int i = 0; i < this.producentPharmacyConnectionList.size(); i++) {
            relationProducentId = this.producentPharmacyConnectionList.get(i).getProducentId();
            relationPharmacyId = this.producentPharmacyConnectionList.get(i).getPharmacyId();

            if (relationProducentId == producentId && relationPharmacyId == pharmacyId) {
                return true;
            }
        }

        return isRealtionAlready;
    }

    private boolean validateRowAndCheckIsDescriptionRow(String row) throws Exception {
        if (row.isBlank()) {
            throw new java.lang.Exception("Podano pusty wiersz. Należy go usunąć. Numer lini: " + this.lineNumber);
        } else if (row.charAt(0) == '#') {
            this.loadingTableNumber++;

            if (loadingTableNumber > NumberOfTables) {
                throw new java.lang.Exception("Liczba podanych tabel jest zbyt duża. Numer lini: " + this.lineNumber);
            }

            return true;
        } else if (row.charAt(0) == ' ') {
            throw new java.lang.Exception("Wiersz nie może zaczynać się od pustego znaku" + this.lineNumber);
        }
        return false;
    }

    public List<VaccineProducent> getVaccineProducentList() {
        return this.vaccineProducentsList;
    }

    public List<Pharmacy> getPharmacyList() {
        return this.pharmaciesList;
    }

    public List<ProducentPharmacyConnection> getProducentPharmacyList() {
        return this.producentPharmacyConnectionList;
    }
}
