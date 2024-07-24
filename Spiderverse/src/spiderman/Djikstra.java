package spiderman;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Djikstra {
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;
    public final int s;
    private ArrayList<LinkedList<Node>> adjList;
    //get the marked and edgeTo and distTo values similar to bfs
    public Djikstra(ArrayList<LinkedList<Node>> adjList, int s) {
        this.adjList = adjList;
        marked = new boolean[adjList.size()];
        edgeTo = new int[adjList.size()];
        this.s = s;
        distTo = new int[adjList.size()];
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = -1;
        }
        dijkstra(s);
    }

    //get the data from the number input and adjList
    public static Data geData(int number, ArrayList<LinkedList<Node>> adjList) {
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).get(0).getData().getDimNum() == number) {
                return adjList.get(i).get(0).getData();
            }

        }
        return null;
    }
    //the source given to the method
    private void dijkstra(int s) {
        PriorityQueue<Data> queue = new PriorityQueue<>();
        //get the marked values based on traversal
        marked[TrackSpot.traverse(adjList, s)] = true;
        //add it to the queue and while its not empty poll() and then traverse and all the values
        queue.add(geData(s, adjList));
        while (!queue.isEmpty()) {
            Data v = queue.poll();
            int p = TrackSpot.traverse(adjList, v.getDimNum());
            LinkedList<Node> link = adjList.get(p);
            for (var w : link) {
                int index2 = TrackSpot.traverse(adjList, w.getData().getDimNum());
                int index = TrackSpot.traverse(adjList, v.getDimNum());
                if (distTo[index2] == -1 || w.getData().getDimWeight() + distTo[index] < distTo[index2]) {
                    // if (!marked[index2]) {
                    edgeTo[index2] = v.getDimNum();
                    StdOut.print(edgeTo[index2]);
                    distTo[index2] = w.getData().getDimWeight() + distTo[index];
                    marked[index2] = true;
                    queue.add(w.getData());
                    // for(int i = 0; i <  queue.size(); i++){
                        StdOut.println(queue.peek().getDimNum() + " ");
                    // }

                    // }
                }
            }
            // if (!marked[index2]) {
            // edgeTo[index2] = v.getDimNum();
            // // marked[index2] = true;
            // queue.add(geData(s, adjList));
            // }
        }
    }


    //similar to bfs
    public boolean hasPathTo(int v) {
        return marked[TrackSpot.traverse(adjList, v)];
    }

    public LinkedList<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }

        LinkedList<Integer> path = new LinkedList<>();
        for (int x = v; x != s; x = edgeTo[TrackSpot.traverse(adjList, x)]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }
}
