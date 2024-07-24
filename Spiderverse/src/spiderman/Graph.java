package spiderman;

import java.util.ArrayList;

public class Graph {

    private final int V;
    private ArrayList<ArrayList<Integer>> adj;
    public Graph(int V){
        this.V = V;
        adj = new ArrayList<>(V);
        for(int v  = 0; v < V; v++){
            adj.add(new ArrayList<>());
        }
    }
    public int V(){
        return V;
    }

    public void addEdge(int v, int w){
        adj.get(v).add(w);
        adj.get(w).add(v);
    }

    public Iterable<Integer> adj(int v){
        return adj.get(v);
    }

    
}
