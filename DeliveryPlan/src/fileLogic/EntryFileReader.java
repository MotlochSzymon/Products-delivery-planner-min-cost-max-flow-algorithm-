package fileLogic;

import enums.TableOrderInEntryFile;
import inputElements.Pharmacy;
import inputElements.ProducerPharmacyConnection;
import inputElements.VaccineProducer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import inputAndOutputHandling.PharmacyProducerInputDataInterface;

public class EntryFileReader implements PharmacyProducerInputDataInterface {

    private List<VaccineProducer> vaccineProducerList;
    private List<Pharmacy> pharmaciesList;
    private List<ProducerPharmacyConnection> producerPharmacyConnectionList;
    private int loadingTableNumber;
    private int lineNumber;
    private static final int NumberOfTables = 3;
    private static final int NumberOfColumnsProducerTable = 3;
    private static final int NumberOfColumnsInPharmacyTable = 3;
    private static final int NumberOfColumnsInProducerPharmacyTable = 4;
    private static final String VaccineProducerTableName = "Producenci szczepionek";
    private static final String PharmacyTableName = "Apteki";
    private static final String ProducerPharmacy = "Połączenie producentów z aptekami";

    public EntryFileReader() {
        this.vaccineProducerList = new ArrayList<VaccineProducer>();
        this.pharmaciesList = new ArrayList<Pharmacy>();
        this.producerPharmacyConnectionList = new ArrayList<ProducerPharmacyConnection>();
        this.loadingTableNumber = 0;
        this.lineNumber = 0;
    }

    @Override
    public void loadAllFileRows(String filePath) throws FileNotFoundException, IOException, Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath+ ".txt"))); 
        System.out.println("Plik został odnaleziony, trwa wczytywanie danych");
        System.out.println("Wczytane dane:");
        System.out.println();
        
        String row;      
        while ((row = br.readLine()) != null) {
            loadAndValidateRow(row);

            System.out.println(row);
        }

        checkLoadedTablesAmount();
        checkRecordsAmount();
        System.out.println();
        System.out.println("Wszystkie dane zostały poprawnie wczytane");
    }

    @Override
    public List<VaccineProducer> getVaccineProducerList() {
        return this.vaccineProducerList;
    }

    @Override
    public List<Pharmacy> getPharmacyList() {
        return this.pharmaciesList;
    }

    @Override
    public List<ProducerPharmacyConnection> getProducerPharmacyList() {
        return this.producerPharmacyConnectionList;
    }

    private void checkLoadedTablesAmount() throws Exception {
        if (lineNumber == 0) {
            throw new java.lang.Exception("Podany plik jest pusty!");
        } else if (loadingTableNumber != NumberOfTables) {
            throw new java.lang.Exception("Nie podano wystarczającej liczby tabel");
        }
    }

    private void checkRecordsAmount() throws Exception {
        if (this.vaccineProducerList.size() < 2) {
            throw new java.lang.Exception("Tabela producentów musi zawierać co najmniej 2 rekordy!");
        } else if (this.pharmaciesList.size() < 2) {
            throw new java.lang.Exception("Tabela z aptekami musi zawierać co najmniej 2 rekordy!");
        } else if (this.producerPharmacyConnectionList.size() < this.vaccineProducerList.size() * this.pharmaciesList.size()) {
            throw new java.lang.Exception("W tabeli z połączeniami producentów i aptek nie podano wszystkich połączeń!");
        }
    }

    private void loadAndValidateRow(String row) throws Exception {
        lineNumber++;
        boolean isDescriptionRow = validateRowAndCheckIsDescriptionRow(row);

        String[] columnValues = row.split(" [|] ");

        if (!isDescriptionRow) {

            if (this.loadingTableNumber == TableOrderInEntryFile.VACCINE_PRODUCERS_TABLE.getValue()) {
                loadVaccineProducersRow(columnValues);
            } else if (this.loadingTableNumber == TableOrderInEntryFile.PHARMACIES_TABLE.getValue()) {
                loadPharmaciesRow(columnValues);
            } else if (this.loadingTableNumber == TableOrderInEntryFile.PRODUCER_PHARMACY_TABLE.getValue()) {
                loadProducerPharmacyConnectionRow(columnValues);
            } else {
                throw new java.lang.Exception("Przed podaniem danych należy umieśić wiersz zawierający opis tabeli! Numer lini: " + this.lineNumber);
            }
        }
    }

    private void loadVaccineProducersRow(String[] columnValues) throws Exception {
        checkNumberOfColumns(columnValues, NumberOfColumnsProducerTable, VaccineProducerTableName);
        int producerId = loadColumnAsIntegerValue(columnValues[0], "Id producenta");
        String producerName = columnValues[1];
        int dailyProduction = loadColumnAsUnsignedValue(columnValues[2], "Dzienna produkcja");
        addProducerToList(producerId, producerName, dailyProduction);
    }

    private void loadPharmaciesRow(String[] columnValues) throws Exception {
        checkNumberOfColumns(columnValues, NumberOfColumnsInPharmacyTable, PharmacyTableName);
        int pharmacyId = loadColumnAsIntegerValue(columnValues[0], "Id apteki");
        String pharmacyName = columnValues[1];
        int dailyNeeds = loadColumnAsUnsignedValue(columnValues[2], "Dzienne zapotrzebowanie");
        addPharmacyToList(pharmacyId, pharmacyName, dailyNeeds);
    }

    private void loadProducerPharmacyConnectionRow(String[] columnValues) throws Exception {
        checkNumberOfColumns(columnValues, NumberOfColumnsInProducerPharmacyTable, ProducerPharmacy);
        int producerId = loadColumnAsIntegerValue(columnValues[0], "Id producenta");
        int pharmacyId = loadColumnAsIntegerValue(columnValues[1], "Id apteki");
        int maxDailyDeliveredVaccines = loadColumnAsUnsignedValue(columnValues[2], "Dzienna maksymalna liczba dostarczanych szczepionek");
        double vaccineCost = loadColumnAsNonNegativeDoubleValue(columnValues[3], "Koszt szczepionki [zł]");
        addProducerPharmacyConnectionToList(producerId, pharmacyId, maxDailyDeliveredVaccines, vaccineCost);
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
            columnValue = Math.floor(columnValue * 100) / 100;

            if (columnValue < 0) {
                throw new java.lang.Exception("Wartośc kolumny: " + columnName + " nie może być liczbą ujemną! Numer lini: " + this.lineNumber);
            }

        } catch (NumberFormatException nfe) {
            throw new java.lang.Exception("Wartośc kolumny: " + columnName + "musi być liczbą rzeczywistą! Numer lini: " + this.lineNumber);
        }
        return columnValue;
    }

    private void addProducerToList(int producerId, String producerName, int dailyProduction) throws Exception {
        if (isAlreadyProducerAdded(producerId)) {
            throw new java.lang.Exception("Producent o id: " + producerId + " już został dodany! Numer lini: " + this.lineNumber);
        } else {
            this.vaccineProducerList.add(new VaccineProducer(producerId, producerName, dailyProduction));
        }
    }

    private void addPharmacyToList(int pharmacyId, String pharmacyName, int dailyNeeds) throws Exception {
        if (isAlreadyPharmacyAdded(pharmacyId)) {
            throw new java.lang.Exception("Apteka o id: " + pharmacyId + " już została dodana! Numer lini: " + this.lineNumber);
        } else {
            this.pharmaciesList.add(new Pharmacy(pharmacyId, pharmacyName, dailyNeeds));
        }
    }

    private void addProducerPharmacyConnectionToList(int producerId, int pharmacyId, int maxDailyDeliveredVaccines, double vaccineCost) throws Exception {
        if (isAlreadyProducerPharmacyRelation(producerId, pharmacyId)) {
            throw new java.lang.Exception("Relacja producenta z apteką jest już dodana! Numer lini: " + this.lineNumber);
        }

        if (!doesProducerExist(producerId)) {
            throw new java.lang.Exception("Wskazany w tabeli relacji producentów z aptekami producent nie istnieje! Numer lini: " + this.lineNumber);
        }

        if (!doesPharmacyExist(pharmacyId)) {
            throw new java.lang.Exception("Wskazana w tabeli relacji producentów z aptkami apteka nie istenieje! Numer lini: " + this.lineNumber);
        }

        ProducerPharmacyConnection connectionToAdd = new ProducerPharmacyConnection(producerId, pharmacyId, maxDailyDeliveredVaccines, vaccineCost);
        this.producerPharmacyConnectionList.add(connectionToAdd);
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

    private boolean isAlreadyProducerAdded(int producerId) {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            if (this.vaccineProducerList.get(i).getProducerId() == producerId) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyProducerPharmacyRelation(int producerId, int pharmacyId) {
        boolean isRealtionAlready = false;
        int relationProducerId;
        int relationPharmacyId;

        for (int i = 0; i < this.producerPharmacyConnectionList.size(); i++) {
            relationProducerId = this.producerPharmacyConnectionList.get(i).getProducerId();
            relationPharmacyId = this.producerPharmacyConnectionList.get(i).getPharmacyId();

            if (relationProducerId == producerId && relationPharmacyId == pharmacyId) {
                return true;
            }
        }

        return isRealtionAlready;
    }

    private boolean doesProducerExist(int producerId) {
        for (int i = 0; i < this.vaccineProducerList.size(); i++) {
            if (this.vaccineProducerList.get(i).getProducerId() == producerId) {
                return true;
            }
        }

        return false;
    }

    private boolean doesPharmacyExist(int pharmacyId) {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            if (this.pharmaciesList.get(i).getPharmacyId() == pharmacyId) {
                return true;
            }
        }

        return false;
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

}
