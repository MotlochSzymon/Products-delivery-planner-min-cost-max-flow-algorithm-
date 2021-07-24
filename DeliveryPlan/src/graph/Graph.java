package graph;

import java.util.ArrayList;
import java.util.Iterator;

public class Graph implements Iterable<Edge> {

    int nodesAmount;
    int edgesAmount;
    int sink;
    int source;

    ArrayList<ArrayList<Edge>> edges = new ArrayList<ArrayList<Edge>>();

    public Graph(int nodesAmount, int edgesAmount, int sink, int source) {
        super();
        this.nodesAmount = nodesAmount;
        this.edgesAmount = edgesAmount;
        this.sink = sink;
        this.source = source;

        for (int i = 0; i < nodesAmount; i++) {
            edges.add(new ArrayList<Edge>());
        }
    }

    public int getNodesAmount() {
        return this.nodesAmount;
    }

    public int getEdgesAmount() {
        return this.edgesAmount;
    }

    public int getSink() {
        return this.sink;
    }

    public int getSource() {
        return this.source;
    }

    public void setEdges(ArrayList<ArrayList<Edge>> edges) {
        this.edges = edges;
    }

    public ArrayList<ArrayList<Edge>> getEdges() {
        return edges;
    }

    public void setNodesAmount(int nodesAmount) {
        this.nodesAmount = nodesAmount;
    }

    public void setEdgesAmount(int edgesAmount) {
        this.edgesAmount = edgesAmount;
    }

    public void addEdge(Edge edge) {
        edges.get(edge.from).add(edge);
    }

    void removeEdge(Edge e) {
        edges.get(e.from).remove(e);
    }

    public Edge getAdjacentWithCap(int from, int start) {
        ArrayList<Edge> arlEdges = edges.get(from);
        for (int i = start; i < arlEdges.size(); i++) {
            if (arlEdges.get(i).flow != 0) {
                return arlEdges.get(i);
            }
        }
        return null;
    }

    class EdgeIterator implements Iterator<Edge> {

        Edge next = null;
        int currBucket = 0;

        EdgeIterator() {

            next = getNextEdgeWithCap();
        }

        @Override
        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            return false;
        }

        @Override
        public Edge next() {
            Edge prevNext = next;
            next = getNextEdgeWithCap();
            return prevNext;
        }

        @Override
        public void remove() {
            // do nothing 

        }

        private Edge getNextEdgeWithCap() {

            ArrayList<Edge> edgeBuck = edges.get(currBucket);
            int idx = -1;
            if (next != null) {
                idx = edgeBuck.indexOf(next);

                if (idx == edgeBuck.size()) {
                    currBucket++;
                    edgeBuck = edges.get(currBucket);
                    idx = -1;
                }
            }
            if (currBucket >= nodesAmount) {
                return null;
            }
            while (true) {
                Edge e;
                for (int i = idx + 1; i < edgeBuck.size(); i++) {
                    e = edgeBuck.get(i);
                    if (e.flow > 0) {
                        return e;
                    }
                }
                currBucket++;
                idx = -1;
                if (currBucket == nodesAmount) {
                    return null;
                }
                edgeBuck = edges.get(currBucket);
            }
        }
    }

    @Override
    public Iterator<Edge> iterator() {

        return new EdgeIterator();
    }
}
