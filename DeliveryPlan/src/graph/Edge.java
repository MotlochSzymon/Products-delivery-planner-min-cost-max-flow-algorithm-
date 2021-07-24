
package graph;

public class Edge {

    int from;
    int to;
    int flow;
    double cost;
    int flowCost;
    Edge reverse;

    public Edge(int from, int to, int flow, double cost) {
        super();
        this.from = from;
        this.to = to;
        this.flow = flow;
        this.cost = cost;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getFlow() {
        return flow;
    }

    public double getCost() {
        return cost;
    }

    public Edge getReverse() {
        return reverse;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setFlow(int flow) {
        if (flow < 0) {
            this.flow = 0;
        } else {
            this.flow = flow;
        }
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setReverse(Edge reverse) {
        this.reverse = reverse;
    }

    public String toString() {
        return "" + from + "->" + to + " : f=" + flow + "c=" + String.valueOf(cost);
    }

}
