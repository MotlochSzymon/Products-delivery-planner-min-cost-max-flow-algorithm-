package fordFurkensonWithBellManFordAlgorithm;

import fileLogic.EntryFileReader;
import inputAndOutputHandling.PharmacyProducerInputDataInterface;
import java.util.List;
import minCostMaxFlowProblem.MinCostMaxFlowProblemInterface;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import outputElements.OrdersSet;

public class MinCostMaxFlowFordFulkersonTest {

    static final String FordFurknesonFolderPath = "Test files/fordFurkensonWithBellManFordAlgorithm/";
    int maxFlow;
    double totalCost;

    @Test(expected = java.lang.Exception.class)
    public void createMinMacFlowProblemWitNullProducent() throws Exception {
        //given
        String fileName = "Valid data";
        String filePath = FordFurknesonFolderPath + fileName;

        //when
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //then
        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(null,
                fileReader.getPharmacyList(), fileReader.getProducerPharmacyList());
    }

    @Test(expected = java.lang.Exception.class)
    public void createMinMacFlowProblemWitNullPharmacies() throws Exception {
        //given
        String fileName = "Valid data";
        String filePath = FordFurknesonFolderPath + fileName;

        //when
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //then
        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(fileReader.getVaccineProducerList(),
                null, fileReader.getProducerPharmacyList());
    }

    @Test(expected = java.lang.Exception.class)
    public void createMinMacFlowProblemWitNullProducentPharmacy() throws Exception {
        //given
        String fileName = "Valid data";
        String filePath = FordFurknesonFolderPath + fileName;

        //when
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //then
        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(fileReader.getVaccineProducerList(),
                fileReader.getPharmacyList(), null);
    }

    @Test(expected = java.lang.Exception.class)
    public void createMinMacFlowProblemWitSinglePharmacy() throws Exception {
        //given
        String fileName = "SinglePharmacy";
        String filePath = FordFurknesonFolderPath + fileName;

        //when
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //then
        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(fileReader.getVaccineProducerList(),
                fileReader.getPharmacyList(), fileReader.getProducerPharmacyList());
    }

    @Test(expected = java.lang.Exception.class)
    public void createMinMacFlowProblemWitSingleProducent() throws Exception {
        //given
        String fileName = "SingleProducent";
        String filePath = FordFurknesonFolderPath + fileName;

        //when
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //then
        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(fileReader.getVaccineProducerList(),
                fileReader.getPharmacyList(), fileReader.getProducerPharmacyList());
    }

    @Test
    public void solveMinMacFlowProblem() throws Exception {
        //given
        boolean hasCorrectResults;
        String fileName = "Test values";
        String filePath = FordFurknesonFolderPath + fileName;

        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(filePath);

        //when
        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(fileReader.getVaccineProducerList(),
                fileReader.getPharmacyList(), fileReader.getProducerPharmacyList());

        //when
        List<OrdersSet> orders = minCostMaxFlowProblem.solveProblem();
        setMaxFlowAndTotalCost(orders);

        //then
        if (this.maxFlow == 2340 && this.totalCost == 189145.5) {
            hasCorrectResults = true;
        }
        else{
            hasCorrectResults = false;
        }

         assertEquals(true, hasCorrectResults);
    }

    private void setMaxFlowAndTotalCost(List<OrdersSet> orders) {
        int totalFlow = 0;
        double totalCost = 0.0;
        for (int i = 0; i < orders.size(); i++) {
            totalFlow = totalFlow + orders.get(i).getAmountOfOrderedProducts();
            totalCost = totalCost + (orders.get(i).getProductCost() * orders.get(i).getAmountOfOrderedProducts());
        }

        this.maxFlow = totalFlow;
        this.totalCost = totalCost;
    }

}
