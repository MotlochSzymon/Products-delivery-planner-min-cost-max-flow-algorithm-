
package fordFurkensonWithBellManFordAlgorithm;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.HashMap; 

public class BellManFordCycle {

    int searchForNegatvieWeightCycle(Graph graphWithWeightCosts, int totalNodes, int source, ArrayList<Edge> nodesInCycle) { 

        double[] weightArray = initWeightArray(totalNodes, source); 
        ArrayList<Edge> predecessorEdges = new ArrayList<Edge>(); 

        for (int i = 0; i < graphWithWeightCosts.getNodesAmount(); i++) {
            predecessorEdges.add(null); 
        } 
        
        double flowCost = 0;

        for (int i = 0; i < totalNodes; i++) {

            for (Edge edge : graphWithWeightCosts) { 
                if (weightArray[edge.getFrom()] == Double.MAX_VALUE) { 
                    continue; 
                } 
                flowCost = edge.getCost(); 
                if ((flowCost + weightArray[edge.getFrom()]) < weightArray[edge.getTo()]) {
                    weightArray[edge.getTo()] = flowCost + weightArray[edge.getFrom()]; 
                    predecessorEdges.set(edge.getTo(), edge); 
                } 
            } 
        } 

        HashMap<Edge, Integer> nodesInCycleMap = new HashMap<Edge, Integer>(); 
        HashMap<Edge, Integer> nodesInCycleReverseMap = new HashMap<Edge, Integer>(); 

        Edge foundCycle = null; 

        for (Edge edge : graphWithWeightCosts) { 

            if (weightArray[edge.getFrom()] == Double.MAX_VALUE) { 
                continue; 
            } 

            flowCost = edge.getCost(); 

            if ((flowCost + weightArray[edge.getFrom()]) < weightArray[edge.getTo()]) {

                foundCycle = edge; 
                break; 
            } 

        } 
        if (foundCycle == null) {
            return -1; 
        } 

        Edge startEdge = foundCycle; 
        Edge predecessorEdge = null; 

        while (true) { 

            predecessorEdge = predecessorEdges.get(startEdge.getFrom()); 
            if (nodesInCycleMap.get(predecessorEdge) != null) {

                break; 
            } 

            nodesInCycle.add(predecessorEdge); 
            nodesInCycleMap.put(predecessorEdge, 1); 
            nodesInCycleReverseMap.put(predecessorEdge.getReverse(), 1); 

            startEdge = predecessorEdge; 
        } 

        int index = nodesInCycle.indexOf(predecessorEdge); 

        for (int i = 0; i < index; i++) {
            nodesInCycle.remove(i); 
        } 

        int minimumInCycle = Integer.MAX_VALUE; 

        for (Edge edge : nodesInCycle) { 
            if (minimumInCycle > edge.getFlow()) { 
                minimumInCycle = edge.getFlow(); 
            } 
        } 
        return minimumInCycle; 
    } 

    double[] initWeightArray(int totalNodes, int source) {
        double[] weightArray = new double[totalNodes]; 
        Arrays.fill(weightArray, Double.MAX_VALUE); 
        weightArray[source] = 0; 
        return weightArray; 
    } 

   public void printProblemSolvedResults(Graph graph) {
        System.out.println("Wyniki: ");
        for (Edge edge : graph) {
            if ((edge.getFlow() > 0) && (edge.getCost() < 0)) {
                System.out.println( edge.getTo() + " -> " + edge.getFrom()  + 
                        "  Sztuk: " + edge.getFlow() +
                        " Koszt: " +  -edge.getFlow()* edge.getCost()/100);

            }
        }
    }
} 