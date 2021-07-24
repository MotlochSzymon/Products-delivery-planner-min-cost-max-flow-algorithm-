package minCostMaxFlowAlgorithm;

import inputElements.Pharmacy;
import inputElements.ProducerPharmacyConnection;
import inputElements.VaccineProducer;
import graph.Edge;
import graph.Graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import outputElements.OrdersSet;

public class MinCostMaxFlowFordFulkerson {

    Graph graphResidual;
    List<VaccineProducer> vaccineProducersList;
    List<Pharmacy> pharmaciesList;
    List<ProducerPharmacyConnection> producentPharmacyConnectionList;
    static final int FirstProducentNodeNumber = 1;
    int firstPharmacyNodeNumber;
    HashMap<Integer, VaccineProducer> nodeNumberProducent;
    HashMap<Integer, Integer> producentIdNodeNumber;
    HashMap<Integer, Pharmacy> nodeNumberPharmacy;
    HashMap<Integer, Integer> pharmacyIdNodeNumber;
    double totalCost;
    int totalFlow;

    public MinCostMaxFlowFordFulkerson(List<VaccineProducer> vaccineProducentsList, List<Pharmacy> pharmaciesList, List<ProducerPharmacyConnection> producentPharmacyConnectionList) {
        this.vaccineProducersList = vaccineProducentsList;
        this.pharmaciesList = pharmaciesList;
        loadProducentPharmacyConnectionList(producentPharmacyConnectionList);
        firstPharmacyNodeNumber = vaccineProducentsList.size() + FirstProducentNodeNumber;

        nodeNumberProducent = new HashMap();
        producentIdNodeNumber = new HashMap();
        nodeNumberPharmacy = new HashMap();
        pharmacyIdNodeNumber = new HashMap();
    }

    public List<OrdersSet> solveProblem() {
        initGraphElements();
        createGraphEdges();
        return minCostMaxFlow();
    }

    void loadProducentPharmacyConnectionList(List<ProducerPharmacyConnection> producentPharmacyConnectionList) {
        for (int i = 0; i < producentPharmacyConnectionList.size(); i++) {
            double cost = producentPharmacyConnectionList.get(i).getVaccineCost();
            producentPharmacyConnectionList.get(i).setVaccineCost(100 * cost);
        }

        this.producentPharmacyConnectionList = producentPharmacyConnectionList;
    }

    void initGraphElements() {
        int amountOfProducents = this.vaccineProducersList.size();
        int amountOfPharmacies = this.pharmaciesList.size();
        int amountOfGaprhNodes = amountOfProducents + amountOfPharmacies + 2;

        int edgeAmount = this.producentPharmacyConnectionList.size();

        graphResidual = new Graph(amountOfGaprhNodes, edgeAmount, amountOfGaprhNodes - 1, 0);
        setProducentsNodeNumbers();
        setPharmaciesNodeNumbers();
    }

    void createGraphEdges() {
        createSourceProducentEdges();
        createProducentPharmacyEdges();
        createPharmacySinkEdges();
    }

    void setProducentsNodeNumbers() {
        for (int i = 0; i < this.vaccineProducersList.size(); i++) {
            nodeNumberProducent.put(FirstProducentNodeNumber + i, this.vaccineProducersList.get(i));
            producentIdNodeNumber.put(this.vaccineProducersList.get(i).getProducentId(), FirstProducentNodeNumber + i);
        }
    }

    void setPharmaciesNodeNumbers() {
        for (int i = 0; i < this.pharmaciesList.size(); i++) {
            nodeNumberPharmacy.put(firstPharmacyNodeNumber + i, this.pharmaciesList.get(i));
            pharmacyIdNodeNumber.put(this.pharmaciesList.get(i).getPharmacyId(), firstPharmacyNodeNumber + i);
        }
    }

    void createSourceProducentEdges() {
        for (int i = 0; i < this.vaccineProducersList.size(); i++) {
            VaccineProducer producent = this.vaccineProducersList.get(i);
            int from = 0;
            int to = this.producentIdNodeNumber.get(producent.getProducentId());

            addEdgeToGraph(from, to, producent.getDayilyProduction(), 0);
        }
    }

    void createProducentPharmacyEdges() {
        for (int i = 0; i < this.producentPharmacyConnectionList.size(); i++) {
            ProducerPharmacyConnection producentPharmacyConnection = this.producentPharmacyConnectionList.get(i);
            int producentId = producentPharmacyConnection.getProducentId();
            int pharmacyId = producentPharmacyConnection.getPharmacyId();

            int from = this.producentIdNodeNumber.get(producentId);
            int to = this.pharmacyIdNodeNumber.get(pharmacyId);
            int flow = producentPharmacyConnection.getMaxDailyDeliveredVaccines();
            double cost = producentPharmacyConnection.getVaccineCost();

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

        countTotalFlowAndCost(graphResidual);
        System.out.println("Max Flow " + this.totalFlow + " Min Cost " + this.totalCost / 100);
        return getFinalResult();

        //BellMancycle.printProblemSolvedResults(graphResidual);
    }

    List<OrdersSet> getFinalResult() {
       ArrayList<OrdersSet> resultSet = new ArrayList<OrdersSet>();
     
        for (Edge edge : this.graphResidual) {
            if ((edge.getFlow() > 0) && (edge.getCost() < 0)) {
                int producentNodeNumber = edge.getTo();
                int pharmacyNodeNumber = edge.getFrom();
                
                int producentId = this.nodeNumberProducent.get(producentNodeNumber).getProducentId();
                String producentName = this.nodeNumberProducent.get(producentNodeNumber).getProducentName();
                String pharmacyName = this.nodeNumberPharmacy.get(pharmacyNodeNumber).getPharmacyName();

                resultSet.add(new OrdersSet(producentId, producentName, pharmacyName, -edge.getCost() / 100, edge.getFlow()));
                 System.out.println(producentName + "  " + pharmacyName + "  " + -edge.getCost() / 100 + " " + edge.getFlow());
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

}
