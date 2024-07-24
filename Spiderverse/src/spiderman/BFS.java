package spiderman;

import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class BFS {
    private boolean[] marked;
    private int[] edgeTo;
    public final int s;
    private ArrayList<LinkedList<Node>> adjList;

    public BFS(ArrayList<LinkedList<Node>> adjList, int s) {
        this.adjList = adjList;
        marked = new boolean[adjList.size()];
        edgeTo = new int[adjList.size()];
        this.s = s;
        bfs(s);
    }

    private void bfs(int s) {
        Queue<Integer> queue = new LinkedList<>();
        marked[TrackSpot.traverse(adjList, s)] = true;
        queue.add(s);
        while (!queue.isEmpty()) {
            int v = queue.poll();
            int p = TrackSpot.traverse(adjList, v);
            LinkedList<Node> link = adjList.get(p);
            for (var w : link) {
                int index2 = TrackSpot.traverse(adjList, w.getData().getDimNum());
                if (!marked[index2]) {
                    edgeTo[index2] = v;
                    marked[index2] = true;
                    queue.add(w.getData().getDimNum());
                }
            }
        }
    }

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