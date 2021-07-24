package deliveryPlan;

import fileLogic.EntryFileReader;
import fileLogic.ResulFileSavier;
import java.io.IOException;
import java.util.List;
import fordFurkensonWithBellManFordAlgorithm.MinCostMaxFlowFordFulkerson;
import inputAndOutputHandling.PharmacyProducerOutputInterface;
import outputElements.OrdersSet;
import minCostMaxFlowProblem.MinCostMaxFlowProblemInterface;
import inputAndOutputHandling.PharmacyProducerInputDataInterface;

public class DeliveryPlan {
        private static final String InputFolderName = "Pliki wejściowe";
        private static final String OutputFolderName = "Wyniki";

    public static void main(String[] args) throws IOException, Exception {
        String fileName = args[0];
        PharmacyProducerInputDataInterface fileReader = new EntryFileReader();
        fileReader.loadAllFileRows(InputFolderName + "/" + fileName);

        MinCostMaxFlowProblemInterface minCostMaxFlowProblem = new MinCostMaxFlowFordFulkerson(fileReader.getVaccineProducerList(),
                 fileReader.getPharmacyList(), fileReader.getProducerPharmacyList());

        List<OrdersSet> orders = minCostMaxFlowProblem.solveProblem();

        PharmacyProducerOutputInterface resultSavier = new ResulFileSavier();
        resultSavier.saveResults(orders, OutputFolderName + "/" + fileName);
    }

}
