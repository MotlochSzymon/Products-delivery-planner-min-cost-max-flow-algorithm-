package fordFurkensonWithBellManFordAlgorithm;

import inputElements.Pharmacy;
import inputElements.ProducerPharmacyConnection;
import inputElements.VaccineProducer;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import outputElements.OrdersSet;
import minCostMaxFlowProblem.MinCostMaxFlowProblemInterface;

public class MinCostMaxFlowFordFulkerson implements MinCostMaxFlowProblemInterface {

    Graph graphResidual;
    List<VaccineProducer> vaccineProducersList;
    List<Pharmacy> pharmaciesList;
    List<ProducerPharmacyConnection> producerPharmacyConnectionList;
    static final int FirstProducerNodeNumber = 1;
    int firstPharmacyNodeNumber;
    HashMap<Integer, VaccineProducer> nodeNumberProducer;
    HashMap<Integer, Integer> producerIdNodeNumber;
    HashMap<Integer, Pharmacy> nodeNumberPharmacy;
    HashMap<Integer, Integer> pharmacyIdNodeNumber;
    double totalCost;
    int totalFlow;

    public MinCostMaxFlowFordFulkerson(List<VaccineProducer> vaccineProducersList, List<Pharmacy> pharmaciesList, List<ProducerPharmacyConnection> producerPharmacyConnectionList) throws Exception {
        validInputData(vaccineProducersList, pharmaciesList, producerPharmacyConnectionList);
        this.vaccineProducersList = vaccineProducersList;
        this.pharmaciesList = pharmaciesList;
        loadProducerPharmacyConnectionList(producerPharmacyConnectionList);
        firstPharmacyNodeNumber = vaccineProducersList.size() + FirstProducerNodeNumber;

        nodeNumberProducer = new HashMap();
        producerIdNodeNumber = new HashMap();
        nodeNumberPharmacy = new HashMap();
        pharmacyIdNodeNumber = new HashMap();
    }

    @Override
    public List<OrdersSet> solveProblem() {
        System.out.println("Trwa wyznaczanie optymalnego rozwiązania...");
        initGraphElements();
        createGraphEdges();
        return minCostMaxFlow();
    }

    void loadProducerPharmacyConnectionList(List<ProducerPharmacyConnection> producerPharmacyConnectionList) {
        for (int i = 0; i < producerPharmacyConnectionList.size(); i++) {
            double cost = producerPharmacyConnectionList.get(i).getVaccineCost();
            producerPharmacyConnectionList.get(i).setVaccineCost(100 * cost);
        }

        this.producerPharmacyConnectionList = producerPharmacyConnectionList;
    }

    void initGraphElements() {
        int amountOfProducers = this.vaccineProducersList.size();
        int amountOfPharmacies = this.pharmaciesList.size();
        int amountOfGaprhNodes = amountOfProducers + amountOfPharmacies + 2;

        int edgeAmount = this.producerPharmacyConnectionList.size();

        graphResidual = new Graph(amountOfGaprhNodes, edgeAmount, amountOfGaprhNodes - 1, 0);
        setProducersNodeNumbers();
        setPharmaciesNodeNumbers();
    }

    void createGraphEdges() {
        createSourceProducerEdges();
        createProducerPharmacyEdges();
        createPharmacySinkEdges();
    }

    void setProducersNodeNumbers() {
        for (int i = 0; i < this.vaccineProducersList.size(); i++) {
            nodeNumberProducer.put(FirstProducerNodeNumber + i, this.vaccineProducersList.get(i));
            producerIdNodeNumber.put(this.vaccineProducersList.get(i).getProducerId(), FirstProducerNodeNumber + i);
        }
    }

    void setPharmaciesNodeNumbers() {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            nodeNumberPharmacy.put(firstPharmacyNodeNumber + i, this.pharmaciesList.get(i));
            pharmacyIdNodeNumber.put(this.pharmaciesList.get(i).getPharmacyId(), firstPharmacyNodeNumber + i);
        }
    }

    void createSourceProducerEdges() {
        for (int i = 0; i < this.vaccineProducersList.size(); i++) {
            VaccineProducer producer = this.vaccineProducersList.get(i);
            int from = 0;
            int to = this.producerIdNodeNumber.get(producer.getProducerId());

            addEdgeToGraph(from, to, producer.getDayilyProduction(), 0);
        }
    }

    void createProducerPharmacyEdges() {
        for (int i = 0; i < this.producerPharmacyConnectionList.size(); i++) {
            ProducerPharmacyConnection producerPharmacyConnection = this.producerPharmacyConnectionList.get(i);
            int producerId = producerPharmacyConnection.getProducerId();
            int pharmacyId = producerPharmacyConnection.getPharmacyId();

            int from = this.producerIdNodeNumber.get(producerId);
            int to = this.pharmacyIdNodeNumber.get(pharmacyId);
            int flow = producerPharmacyConnection.getMaxDailyDeliveredVaccines();
            double cost = producerPharmacyConnection.getVaccineCost();

            addEdgeToGraph(from, to, flow, cost);
        }
    }

    void createPharmacySinkEdges() {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            Pharmacy pharmacy = this.pharmaciesList.get(i);

            int from = this.pharmacyIdNodeNumber.get(pharmacy.getPharmacyId());
            int to = this.graphResidual.getSink();

            addEdgeToGraph(from, to, pharmacy.getDayilyNeeds(), 0);
        }
    }

    void addEdgeToGraph(int from, int to, int flow, double cost) {
        Edge e = new Edge(from, to, flow, cost);
        Edge er = new Edge(to, from, 0, -cost);

        graphResidual.addEdge(e);
        graphResidual.addEdge(er);
        e.setReverse(er);
        er.setReverse(e);
    }

    Edge getVertexNumberWithCapacity(int nodeNumber, int from) {
        return graphResidual.getAdjacentWithCap(nodeNumber, from);
    }

    int findAugmentingPathUsingDFS(int nodeNumber, int currentBottleneckFlow, TreeSet<Integer> markedNodes, ArrayList<Edge> augmentingPath) {
        int currentMinFlow = 0;
        Edge edgeNumber = null;

        int previousBottleneckFlow = currentBottleneckFlow;
        int previousNodeNumber = -1;

        markedNodes.add(nodeNumber);

        while (true) {
            edgeNumber = getVertexNumberWithCapacity(nodeNumber, previousNodeNumber + 1);

            if (edgeNumber == null) {
                return -1;
            }

            previousNodeNumber += 1;

            if (markedNodes.contains(edgeNumber.getTo())) {
                continue;
            }

            if (currentBottleneckFlow > edgeNumber.getFlow()) {
                currentBottleneckFlow = edgeNumber.getFlow();
            }

            augmentingPath.add(edgeNumber);

            if (edgeNumber.getTo() == this.graphResidual.getSink()) {
                return currentBottleneckFlow;
            }

            if ((currentMinFlow = findAugmentingPathUsingDFS(edgeNumber.getTo(), currentBottleneckFlow, markedNodes, augmentingPath)) != -1) {
                return currentMinFlow;
            }

            augmentingPath.remove(augmentingPath.size() - 1);
            currentBottleneckFlow = previousBottleneckFlow;
        }

    }

    void modifyResidualGraph(int flow, ArrayList<Edge> augmentingPath) {
        Edge backEdge = null;

        for (Edge edge : augmentingPath) {
            edge.setFlow(edge.getFlow() - flow);
            backEdge = edge.getReverse();
            backEdge.setFlow(backEdge.getFlow() + flow);
        }
    }

    int executeFordFulkerson() {
        ArrayList<Edge> augmentingPath = new ArrayList<Edge>();
        int bottleNeckFlow = -1;
        int totalMaxFlow = 0;
        TreeSet<Integer> markedNodes = new TreeSet<Integer>();

        while ((bottleNeckFlow = findAugmentingPathUsingDFS(this.graphResidual.getSource(),
                Integer.MAX_VALUE, markedNodes, augmentingPath)) != -1) {

            modifyResidualGraph(bottleNeckFlow, augmentingPath);
            totalMaxFlow += bottleNeckFlow;
            augmentingPath.clear();
            markedNodes.clear();
        }

        return totalMaxFlow;
    }

    void saturateNegativeCycle(ArrayList<Edge> cycle, int minFlow) {
        HashMap<Edge, Integer> nodesInCycleMap = new HashMap<Edge, Integer>();
        Edge reverseEdge = null;

        for (Edge edge : cycle) {
            if (nodesInCycleMap.get(edge) != null) {
                continue;
            }

            edge.setFlow(edge.getFlow() - minFlow);
            reverseEdge = edge.getReverse();
            reverseEdge.setFlow(reverseEdge.getFlow() + minFlow);
            nodesInCycleMap.put(edge, 1);
            nodesInCycleMap.put(reverseEdge, 1);
        }

        return;
    }

    public List<OrdersSet> minCostMaxFlow() {
        executeFordFulkerson();
        BellManFordCycle BellMancycle = new BellManFordCycle();

        ArrayList<Edge> cycle = new ArrayList<Edge>();
        int minimumFlow = 0;

        while (true) {
            minimumFlow = BellMancycle.searchForNegatvieWeightCycle(graphResidual, graphResidual.getNodesAmount(), graphResidual.getSink(), cycle);
            if (minimumFlow == -1) {
                break; // max flow was found!
            } else {
                saturateNegativeCycle(cycle, minimumFlow);
            }

            cycle.clear();
        }

        System.out.println("Rozwiązanie zostało wyznaczone!");
        return getFinalResult();
    }

    List<OrdersSet> getFinalResult() {
        ArrayList<OrdersSet> resultSet = new ArrayList<OrdersSet>();

        for (Edge edge : this.graphResidual) {
            if ((edge.getFlow() > 0) && (edge.getCost() < 0)) {
                int producerNodeNumber = edge.getTo();
                int pharmacyNodeNumber = edge.getFrom();

                int producerId = this.nodeNumberProducer.get(producerNodeNumber).getProducerId();
                String producerName = this.nodeNumberProducer.get(producerNodeNumber).getProducerName();
                String pharmacyName = this.nodeNumberPharmacy.get(pharmacyNodeNumber).getPharmacyName();

                resultSet.add(new OrdersSet(producerId, producerName, pharmacyName, -edge.getCost() / 100, edge.getFlow()));
            }
        }

        resultSet.sort(Comparator.comparing(OrdersSet::getProducerName));
        return resultSet;
    }

    void countTotalFlowAndCost(Graph graph) {
        int flow = 0;
        double cost = 0;
        for (Edge edge : graph) {
            if ((edge.getFlow() > 0) && (edge.getCost() < 0)) {
                flow += edge.getFlow();
                cost += edge.getFlow() * (-edge.getCost());
            }

            this.totalFlow = flow;
            this.totalCost = cost;
        }
    }

    void validInputData(List<VaccineProducer> vaccineProducersList, List<Pharmacy> pharmaciesList, List<ProducerPharmacyConnection> producerPharmacyConnectionList) throws Exception {
        if (vaccineProducersList == null) {
            throw new java.lang.Exception("Vaccine producers list is empty!");
        }
        if (pharmaciesList == null) {
            throw new java.lang.Exception("Pharmacies list is empty!");
        }

        if (producerPharmacyConnectionList == null) {
            throw new java.lang.Exception("Pharmacy connection list is empty!");
        }

        if (vaccineProducersList.size() < 2) {
            throw new java.lang.Exception("Vaccine producers list must have at least 2 records!");
        }
        if (pharmaciesList.size() < 2) {
            throw new java.lang.Exception("Pharmacies list must have at least 2 records!");
        }

        if (producerPharmacyConnectionList.size() < 4) {
            throw new java.lang.Exception("Pharmacies Producent connection must have at least 4 records!");
        }

    }

}
