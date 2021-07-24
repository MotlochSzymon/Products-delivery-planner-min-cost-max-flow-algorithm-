package fileLogic;

import inputAndOutputHandling.PharmacyProducerInputDataInterface;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntryFileReaderTest {

    static final String AllFileFolderPath = "Test files/fileLogic/EntryFileReader/AllFile/";
    static final String ProducersFolderPath = "Test files/fileLogic/EntryFileReader/Producers/";
    static final String PharmaciesFolderPath = "Test files/fileLogic/EntryFileReader/Pharmacies/";
    static final String ProdcerPharmaciesConnectionFolderPath = "Test files/fileLogic/EntryFileReader/ProdcerPharmaciesConnection/";

    public EntryFileReaderTest() {
    }

    @Test
    public void readValidFile() throws Exception {
        // given
        String fileName = "Valid file";
        String filePath = AllFileFolderPath + fileName;
        int exceptedProducersTableLenght = 3;
        int exceptedPharmacyTableLenght = 3;
        int exceptedProducentPharmacyTableLenght = 9;

        //when
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //then
        boolean correctAmountOfRecords;
        int producersTableLenght = fileReader.getVaccineProducerList().size();
        int pharmacyTableLenght = fileReader.getPharmacyList().size();
        int producentPharmacyTableLenght = fileReader.getProducerPharmacyList().size();

        if (exceptedProducersTableLenght == producersTableLenght && exceptedPharmacyTableLenght == pharmacyTableLenght && exceptedProducentPharmacyTableLenght == producentPharmacyTableLenght) {
            correctAmountOfRecords = true;
        } else {
            correctAmountOfRecords = false;
        }

        assertEquals(true, correctAmountOfRecords);

    }

    @Test(expected = java.lang.Exception.class)
    public void readNotExistingFile() throws Exception {
        // given
        String fileName = "Dont exist!";
        String filePath = AllFileFolderPath + fileName;

        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();

        //then
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readEmptyFile() throws Exception {
        // given
        String fileName = "Empty file";
        String filePath = AllFileFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithEmptyRowsInTheEnd() throws Exception {
        // given
        String fileName = "Extra empty row in the end";
        String filePath = AllFileFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithEmptyRowsInTheMiddle() throws Exception {
        // given
        String fileName = "Extra empty row in the middle";
        String filePath = AllFileFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithEmptyRowsInTheBegining() throws Exception {
        // given
        String fileName = "Extra empty row on the begin";
        String filePath = AllFileFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithTooLittleTables() throws Exception {
        // given
        String fileName = "Too little tables";
        String filePath = AllFileFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithTooManyTables() throws Exception {
        // given
        String fileName = "Too many tables";
        String filePath = AllFileFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithproducentNegativeDayilyProduction() throws Exception {
        // given
        String fileName = "Producers - negative dayily production";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithDoubleDayilyProductionType() throws Exception {
        // given
        String fileName = "Producers - wrong dayily production type - double";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithStringDayilyProductionType() throws Exception {
        // given
        String fileName = "Producers - wrong dayily production type - String";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithStringIdType() throws Exception {
        // given
        String fileName = "Producers - wrong id type  - String";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithDoubleIdType() throws Exception {
        // given
        String fileName = "Producers - wrong id type - double";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithTooLittleProducersColumns() throws Exception {
        // given
        String fileName = "Too little columns -  producers";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithTooManyProducersColumns() throws Exception {
        // given
        String fileName = "Too many columns - producers";
        String filePath = ProducersFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithNegativePharmacyNeeds() throws Exception {
        // given
        String fileName = "Pharamcies - negative dayily needs value";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithDoublePharmacyNeeds() throws Exception {
        // given
        String fileName = "Pharmacies - wrong dayily need type - double";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithStringPharmacyNeeds() throws Exception {
        // given
        String fileName = "Pharmacies - wrong dayily need type - String";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithDoubleId() throws Exception {
        // given
        String fileName = "Pharmacies - wrong id type  - double";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithStringId() throws Exception {
        // given
        String fileName = "Pharmacies - wrong id type  - String";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithToLittlePharmacyColumns() throws Exception {
        // given
        String fileName = "Too little columns - pharmacies";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithToManyPharmacyColumns() throws Exception {
        // given
        String fileName = "Too many columns - pharmacies";
        String filePath = PharmaciesFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducerPharamcyStringProducentId() throws Exception {
        // given
        String fileName = "Wrong producer id type - String";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducerPharamcyDoubleProducentId() throws Exception {
        // given
        String fileName = "Wrong producer id type - double";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducerPharamcyStringPharmacyId() throws Exception {
        // given
        String fileName = "Wrong pharmacy id type - String";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducerPharamcyDoublePharmacyId() throws Exception {
        // given
        String fileName = "Wrong pharmacy id type - double";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyStringMaxDelivery() throws Exception {
        // given
        String fileName = "Wrong max dayily delivered type - String";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyDoubleMaxDelivery() throws Exception {
        // given
        String fileName = "Wrong max dayily delivered type - String";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyTooManyColumns() throws Exception {
        // given
        String fileName = "Too many columns - producers pharmacies connection";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyTooLittleColumns() throws Exception {
        // given
        String fileName = "Too little columns - producents pharmacies connection";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyNegativeCost() throws Exception {
        // given
        String fileName = "Negative vaccume cost";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyNegativeMaxDelivery() throws Exception {
        // given
        String fileName = "Negative max dayily delivered";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyNotExistingProducer() throws Exception {
        // given
        String fileName = "Not existing producent";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacyNotExistingPharmacy() throws Exception {
        // given
        String fileName = "Not existing pharmacy";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacWithTooManyRows() throws Exception {
        // given
        String fileName = "Too many rows";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

    @Test(expected = java.lang.Exception.class)
    public void readFileWithProducentPharmacWithTooLittleRows() throws Exception {
        // given
        String fileName = "Too little rows";
        String filePath = ProdcerPharmaciesConnectionFolderPath + fileName;

        //then
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);
    }

}
