package spiderman;

import java.util.*;

/**
 * Steps to implement this class main method:
 *
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 * i. a (int): number of dimensions in the graph
 * ii. b (int): the initial size of the cluster table prior to rehashing
 * iii. c (double): the capacity(threshold) used to rehash the cluster table
 * 2. a lines, each with:
 * i. The dimension number (int)
 * ii. The number of canon events for the dimension (int)
 * iii. The dimension weight (int)
 *
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 * i. The dimension they are currently at (int)
 * ii. The name of the person (String)
 * iii. The dimensional signature of the person (int)
 *
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 * i. The dimensional number of the starting hub (int)
 *
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 * is at the same Dimension (if one exists, space separated) followed by
 * the Dimension number for each Dimension in the route (space separated)
 *
 * @author Seth Kelley
 */

public class CollectAnomalies {

    public static void main(String[] args) {

        if (args.length < 4) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String collectedOutputFile = args[3];

        ArrayList<Person> per = Collider.addPeople(spiderverseInputFile);
        //
        Node[] clusterT = Clusters.readInput(dimensionInputFile);
        // connect
        ArrayList<LinkedList<Node>> adjList = readDim(dimensionInputFile);
        Clusters.connectClusters(clusterT);
        connectDimInAdj(adjList, clusterT);

        StdIn.setFile(hubInputFile);
        int hubDim = StdIn.readInt();
        // bfs implementation using BFS obj
        BFS bfs = new BFS(adjList, hubDim);
        // bfs.pathTo(50101);
        // LinkedList<Integer> l = bfs.pathTo(50101);
        collectA(per, bfs, collectedOutputFile);

        // for(int i = 0; i < l.size(); i++){
        // StdOut.print(l.get(i) + " ");
        // }
        // boolean[] visited = new boolean[adjList.size()];
        // for(int i = 0; i < visited.length; i++){
        // visited[i] = false;
        // }

        // ArrayList<Integer> pathTo = new ArrayList<>();
        // Queue<Integer> queue = new LinkedList<>();
        // int index = TrackSpot.traverse(adjList, hubDim);
        // visited[TrackSpot.traverse(adjList, hubDim)] = true;
        // queue.add(hubDim);
        // while(queue.size() != 0){
        // int s = queue.remove();
        // pathTo.add(s);
        // int p = TrackSpot.traverse(adjList, s);
        // LinkedList<Node> link = adjList.get(p);
        // for(int j = 0; j < link.size(); j++){
        // int index2 = TrackSpot.traverse(adjList, link.get(j).getData().getDimNum());
        // if(!visited[index2]){
        // // get no.
        // // get index
        // edgeTo[index2] = s;
        // marked[index2] = true;
        // queue.add(link.get(j).getData().getDimNum());
        // }
        // }
        // }

        // get the isSpider
        // then get isAnomaly
        // then work on the edgeTo which is the hub inout 928
        // get in the bfs
        // and then if its spider, it will print both name
        // else it will print the name and the path

    }

    // public void bfs(Person p, ArrayList<LinkedList<Node>> adjList, int hubDim,
    // boolean hasSpider ){
    // edgeTo = new int[adjList.size()];
    // marked = new boolean[adjList.size()];

    // for(int i = 0; i < adjList.size(); i++){
    // marked[i] = false;

    // int currentDim = p.getCurrentDim();
    // Queue<Integer> queue = new LinkedList<>();

    // queue.add(hubDim);
    // marked[adjList.indexOf(hubDim)] = true;
    // int dim;
    // while(!queue.isEmpty()){
    // dim = queue.poll();
    // if(dim == currentDim){
    // break;
    // }

    // // for(Node ptr = adjList.get(adjList.indexOf(c.get)))
    // }
    // }
    // }

    public static void collectA(ArrayList<Person> people, BFS bfs, String filename) {
        // set the file
        StdOut.setFile(filename);
        // read the people and get the dimensions and send to bfs
        for (Person p : people) {
            if (p.getCurrentDim() == bfs.s) {
                continue;
            }
            if (p.getCurrentDim() == p.getSigDim()) {
                continue;
            }
            // print the names
            StdOut.print(p.name() + " ");
            boolean mark = false;
            // iterate through people and get the dimensions
            for (Person p1 : people) {
                // if the dimensions match the person's signature dimension
                if (p1.getCurrentDim() == p1.getSigDim() && p.getCurrentDim() == p1.getCurrentDim()) {
                    StdOut.print(p1.name() + " ");
                    LinkedList<Integer> reversedPath = new LinkedList<>();
                    LinkedList<Integer> originalPath = bfs.pathTo(p.getCurrentDim());
                    //reverse printing the numbers
                    for (int i = originalPath.size() - 1; i >= 0; i--) {
                        reversedPath.add(originalPath.get(i));
                    }
                    //
                    for (int y : reversedPath) {
                        StdOut.print(y + " ");
                    }

                    StdOut.println();
                    // for (int y : bfs.pathTo(p.getCurrentDim()).reversed()) {

                    // StdOut.print(y + " ");
                    // }
                    // StdOut.println();
                    mark = true;

                    break;
                }

            }

            if (mark == true)
                continue;

            for (var v : bfs.pathTo(p.getCurrentDim())) {
                StdOut.print(v + " ");
            }
            // for (int y : bfs.pathTo(p.getCurrentDim()).reversed()) {
            // if (y != p.getCurrentDim())
            // StdOut.print(y + " ");
            // }
            LinkedList<Integer> path = bfs.pathTo(p.getCurrentDim());
            if (path != null) {
                for (int i = path.size() - 1; i >= 0; i--) {
                    int y = path.get(i);
                    if (y != p.getCurrentDim()) {
                        StdOut.print(y + " ");
                    }
                }
                StdOut.println();
            }

            
        }
    }

    //
    //////////////////
    // from collider.java

    public static ArrayList<LinkedList<Node>> readDim(String dimensionInputFile) {
        StdIn.setFile(dimensionInputFile);
        ArrayList<LinkedList<Node>> adjList = new ArrayList<>();
        // get a,b,c
        int numDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();
        // new node for the clusterTable

        int n = 0;
        while (n < numDimensions) {
            int dimNum = StdIn.readInt();
            int canonEvents = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            // hash function

            // get the data
            Data data = new Data(dimNum, canonEvents, dimWeight);
            // set the new node
            Node newNode = new Node(data, null);
            LinkedList<Node> tempList = new LinkedList<>();
            tempList.add(newNode);
            adjList.add(tempList);
            n++;
        }

        return adjList;

    }

    private static void connectDimInAdj(ArrayList<LinkedList<Node>> adjList, Node[] clusterT) {
        for (int i = 0; i < clusterT.length; i++) {
            Node cluster = clusterT[i];
            Node clusterFirst = new Node(cluster.getData(), null);
            Node ptr = cluster.getNext();
            while (ptr != null) {
                Node dim = new Node(ptr.getData(), null);
                connectDimensionInAdj(adjList, clusterFirst, dim);
                ptr = ptr.getNext();
            }
        }
    }

    // connect the dim with dim values

    private static void connectDimensionInAdj(ArrayList<LinkedList<Node>> adjList, Node clusterFirst, Node dim) {
        for (int j = 0; j < adjList.size(); j++) {
            int dimNumFirst = clusterFirst.getData().getDimNum();
            int dimNumPtr = dim.getData().getDimNum();
            int adjCurrent = adjList.get(j).get(0).getData().getDimNum();
            if (dimNumFirst == adjCurrent) {
                adjList.get(j).add(dim);
            }
            if (dimNumPtr == adjCurrent) {
                adjList.get(j).add(clusterFirst);

            }
        }
    }
}

// public class BreadthFirstPaths{
// private boolean[] marked;
// private int[] edgeTo;
// private final int s;

// public BreadthFirstPaths(Graph G, int s){
// marked = new boolean[G.V()];
// edgeTo = new int[G.V()];
// this.s = s;
// bfs(G, s);
// }

// private void bfs(Graph G, int s){
// Queue<Integer> queue = new Queue<Integer>();
// marked[s] = true;
// queue.enqueue(s);
// while(!q.isEmpty()){
// int v = queue.dequeue();
// for(int w : G.adj(V)){
// edgeTo[w] = v;
// marked[w] = true;
// queue.enqueue(w);
// }
// }
// }
// public boolean hasPathTo(int v){ return marked[v];}

// public Iterable<Integer> pathTo(int v){
// if(!hasPathTo(v)) return null;
// Stack<Integer> path = new Stack<Integer>();
// for(int x = v; x != s; x = edgeTo[x])
// path.push(x);
// path.push(s);
// return path;
// }

// Map<Integer, Person> spiderverse = readSpiderverse(spiderverseInputFile);

// Graph graph = new Graph(adjList.size());
// for (LinkedList<Node> dimension : adjList) {
// int source = dimension.get(0).getData().getDimNum();
// for (int i = 1; i < dimension.size(); i++) {
// int destination = dimension.get(i).getData().getDimNum();
// graph.addEdge(source, destination);
// }
// }

// StdOut.setFile(collectedOutputFile);
// collectAnomalies(graph, spiderverse, hubDim);
// }

// private static Map<Integer, Person> readSpiderverse(String file) {
// Map<Integer, Person> spiderverse = new HashMap<>();
// StdIn.setFile(file);
// int numPeople = StdIn.readInt();
// for (int i = 0; i < numPeople; i++) {
// int dimension = StdIn.readInt();
// String name = StdIn.readString();
// int dimensionalSignature = StdIn.readInt();
// spiderverse.put(dimension, new Person(dimension, name,
// dimensionalSignature));
// }
// return spiderverse;
// }

// private static void collectAnomalies(Graph graph, Map<Integer, Person>
// spiderverse, int hubDim) {
// for (Map.Entry<Integer, Person> entry : spiderverse.entrySet()) {
// int dimension = entry.getKey();
// Person person = entry.getValue();
// if (person.getCurrentDim() != person.getSigDim() && dimension != hubDim) {
// boolean hasSpider = false;
// Person spider = null;
// for (Person other : spiderverse.values()) {
// if (other.getCurrentDim() == dimension && other.getSigDim() == dimension) {
// hasSpider = true;
// spider = other;
// break;
// }
// }

// List<Integer> route = bfs(graph, hubDim, dimension);
// if (hasSpider) {
// Collections.reverse(route);
// StdOut.print(person.name() + " " + spider.name());
// } else {
// route = bfs(graph, hubDim, dimension);
// Collections.reverse(route);
// StdOut.print(person.name());
// route = bfs(graph, dimension, hubDim);
// }
// for (int dim : route) {
// StdOut.print(" " + dim);
// }
// StdOut.println();
// }
// }
// }

// private static List<Integer> bfs(Graph graph, int source, int destination) {
// Queue<Integer> queue = new LinkedList<>();
// boolean[] visited = new boolean[graph.getAdjList().size()];
// List<Integer> route = new ArrayList<>();
// queue.add(source);
// visited[source] = true;

// while (!queue.isEmpty()) {
// int current = queue.poll();
// route.add(current);

// if (current == destination) {
// return route;
// }

// for (int neighbor : graph.getAdjList().get(current)) {
// if (!visited[neighbor]) {
// queue.add(neighbor);
// visited[neighbor] = true;
// }
// }
// }

// return null;
// }

// List<LinkedList<Integer>> dimensions = readDimensions(dimensionInputFile);
// Map<Integer, Person> spiderverse = readSpiderverse(spiderverseInputFile);
// int hubDim = readHub(hubInputFile);

// List<Anomaly> anomalies = new ArrayList<>();
// for (Person person : spiderverse.values()) {
// if (person.getDimension() != person.getDimensionalSignature()) {
// anomalies.add(new Anomaly(person));
// }
// }

// Graph graph = new Graph(dimensions.size());
// for (LinkedList<Integer> dimension : dimensions) {
// graph.addEdge(dimension.get(0), dimension.get(1), dimension.get(2));
// }

// StdOut.setFile(collectedOutputFile);
// for (Anomaly anomaly : anomalies) {
// List<Integer> route = graph.bfs(hubDim, anomaly.getDimension());
// if (route.size() > 1 && spiderverse.containsKey(anomaly.getDimension())) {
// Collections.reverse(route);
// StdOut.print(anomaly.getName() + " " +
// spiderverse.get(anomaly.getDimension()).getName());
// for (Integer dim : route) {
// StdOut.print(" " + dim);
// }
// StdOut.println();
// }
// }
// }

// private static List<LinkedList<Integer>> readDimensions(String file) {
// List<LinkedList<Integer>> dimensions = new ArrayList<>();
// StdIn.setFile(file);
// int a = StdIn.readInt();
// int b = StdIn.readInt();
// double c = StdIn.readDouble();
// for (int i = 0; i < a; i++) {
// LinkedList<Integer> dimension = new LinkedList<>();
// dimension.add(StdIn.readInt());
// dimension.add(StdIn.readInt());
// dimension.add(StdIn.readInt());
// dimensions.add(dimension);
// }
// return dimensions;
// }

// private static Map<Integer, Person> readSpiderverse(String file) {
// Map<Integer, Person> spiderverse = new HashMap<>();
// StdIn.setFile(file);
// int d = StdIn.readInt();
// for (int i = 0; i < d; i++) {
// int dimension = StdIn.readInt();
// String name = StdIn.readString();
// int dimensionalSignature = StdIn.readInt();
// spiderverse.put(dimension, new Person(name, dimension,
// dimensionalSignature));
// }
// return spiderverse;
// }

// private static int readHub(String file) {
// StdIn.setFile(file);
// return StdIn.readInt();
// }

// private static class Anomaly {
// private final String name;
// private final int dimension;

// public Anomaly(Person person) {
// this.name = person.getName();
// this.dimension = person.getDimension();
// }

// public int getDimension() {
// return dimension;
// }

// public String getName() {
// return name;
// }
// }

// private static class Graph {
// private List<List<Integer>> adj;
// private final int[] distTo;
// private final int[] edgeTo;

// public Graph(int size) {
// this.adj = new ArrayList<>(size);
// for (int i = 0; i < size; i++) {
// this.adj.add(new ArrayList<>());
// }
// this.distTo = new int[size];
// this.edgeTo = new int[size];
// Arrays.fill(this.distTo, Integer.MAX_VALUE);
// Arrays.fill(this.edgeTo, -1); // Initialize edgeTo with -1
// }

// public void addEdge(int v, int w, int weight) {
// if (v >= adj.size()) {
// resizeAdj(v + 1);
// }
// if (w >= adj.size()) {
// resizeAdj(w + 1);
// }
// adj.get(v).add(w);
// distTo[w] = weight;
// edgeTo[w] = v;
// }
// private void resizeAdj(int newSize) {
// int oldSize = adj.size();
// adj.addAll(Collections.nCopies(newSize - oldSize, new ArrayList<>()));
// }

// public List<Integer> bfs(int source, int destination) {
// boolean[] visited = new boolean[adj.size()];
// LinkedList<Integer> queue = new LinkedList<>();
// queue.addLast(source);
// visited[source] = true;

// while (!queue.isEmpty()) {
// int v = queue.removeFirst();
// if (v == destination) {
// break;
// }
// for (int w : adj.get(v)) {
// if (!visited[w]) {
// visited[w] = true;
// queue.addLast(w);
// edgeTo[w] = v;
// }
// }
// }
// return buildPath(source, destination);
// }
// private List<Integer> buildPath(int source, int destination) {
// Stack<Integer> path = new Stack<>();
// int v = destination;
// while (v != source) {
// path.push(v);
// v = edgeTo[v];
// }
// path.push(source);
// return path;
// }
// }

// private static class Person {
// private final String name;
// private final int dimension;
// private final int dimensionalSignature;

// public Person(String name, int dimension, int dimensionalSignature) {
// this.name = name;
// this.dimension = dimension;
// this.dimensionalSignature = dimensionalSignature;
// }

// public String getName() {
// return name;
// }

// public int getDimension() {
// return dimension;
// }

// public int getDimensionalSignature() {
// return dimensionalSignature;
// }
// }
// }

// public static void main(String[] args) {

// if ( args.length < 4 ) {
// StdOut.println(
// "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file>
// <spiderverse INput file> <hub INput file> <collected OUTput file>");
// return;
// }

// // WRITE YOUR CODE HERE
// String dimensionInputFile = args[0];
// String spiderverseInputFile = args[1];
// String hubInputFile = args[2];
// String collectedOutputFile = args[3];

// List<LinkedList<Integer>> dimensions = readDimensions(dimensionInputFile);
// Map<Integer, Person> spiderverse = readSpiderverse(spiderverseInputFile);
// int hubDim = readHub(hubInputFile);

// List<Anomaly> anomalies = new ArrayList<>();
// for (Person person : spiderverse.values()) {
// if (person.getDimension() != person.getDimensionalSignature()) {
// anomalies.add(new Anomaly(person));
// }
// }

// Graph graph = new Graph(dimensions.size());
// for (LinkedList<Integer> dimension : dimensions) {
// graph.addEdge(dimension.get(0), dimension.get(1), dimension.get(2));
// }

// for (Anomaly anomaly : anomalies) {
// List<Integer> route = bfs(graph, hubDim, anomaly.getDimension());
// if (route.size() > 1 && spiderverse.containsKey(anomaly.getName())) {
// route = reverse(route);
// StdOut.printf("%s %s", anomaly.getName(),
// spiderverse.get(anomaly.getName()).getName());
// for (Integer dim : route) {
// StdOut.print(" " + dim);
// }
// StdOut.println();
// }
// }

// StdOut.setFile(collectedOutputFile);
// }

// private static List<LinkedList<Integer>> readDimensions(String file) {
// List<LinkedList<Integer>> dimensions = new ArrayList<>();
// StdIn.setFile(file);
// int a = StdIn.readInt();
// int b = StdIn.readInt();
// double c = StdIn.readDouble();
// for (int i = 0; i < a; i++) {
// LinkedList<Integer> dimension = new LinkedList<>();
// dimension.add(StdIn.readInt());
// dimension.add(StdIn.readInt());
// dimension.add(StdIn.readInt());
// dimensions.add(dimension);
// }

// return dimensions;
// }

// private static Map<Integer, Person> readSpiderverse(String file) {
// Map<Integer, Person> spiderverse = new HashMap<>();
// StdIn.setFile(file);
// int d = StdIn.readInt();

// for (int i = 0; i < d; i++) {

// int dimension = StdIn.readInt();
// String name = StdIn.readString();
// int dimensionalSignature = StdIn.readInt();
// spiderverse.put(dimension, new Person(name, dimension,
// dimensionalSignature));
// }

// return spiderverse;
// }

// private static int readHub(String file) {
// StdIn.setFile(file);
// return StdIn.readInt();
// }

// private static class Anomaly {
// private final String name;
// private final int dimension;

// public Anomaly(Person person) {
// this.name = person.getName();
// this.dimension = person.getDimensionalSignature();
// }

// public int getDimension() {
// return dimension;
// }

// public String getName() {
// return name;
// }
// }

// private static class Graph {
// private int[][] adj;
// private final int[] distTo;
// private final int[] edgeTo;

// public Graph(int size) {
// this.adj = new int[size][];
// this.distTo = new int[size];
// this.edgeTo = new int[size];
// for (int i = 0; i < size; i++) {
// this.adj[i] = new int[0];
// this.distTo[i] = Integer.MAX_VALUE;
// }
// }

// public void addEdge(int v, int w, int weight) {
// int[][] newAdj = new int[adj.length][];
// System.arraycopy(adj, 0, newAdj, 0, adj.length);
// newAdj[v] = new int[adj[v].length + 1];
// System.arraycopy(adj[v], 0, newAdj[v], 0, adj[v].length);
// newAdj[v][adj[v].length] = w;
// adj = newAdj;
// distTo[w] = weight;
// edgeTo[w] = v;
// }

// public List<Integer> bfs(int source, int destination) {
// boolean[] visited = new boolean[adj.length];
// LinkedList<Integer> queue = new LinkedList<>();
// queue.addLast(source);
// visited[source] = true;
// while (!queue.isEmpty()) {
// int v = queue.removeFirst();
// if (v == destination) {
// break;
// }
// for (int w : adj[v]) {
// if (!visited[w]) {
// visited[w] = true;
// queue.addLast(w);
// edgeTo[w] = v;
// }
// }
// }
// return buildPath(source, destination);
// }

// private List<Integer> buildPath(int source, int destination) {
// Stack<Integer> path = new Stack<>();
// int v = destination;
// while (v != source) {
// path.push(v);
// v = edgeTo[v];
// }
// path.push(source);
// return path;
// }
// }

// private static class Person {
// private final String name;
// private final int dimension;
// private final int dimensionalSignature;

// public Person(String name, int dimension, int dimensionalSignature) {
// this.name = name;
// this.dimension = dimension;
// this.dimensionalSignature = dimensionalSignature;
// }

// public String getName() {
// return name;
// }

// public int getDimension() {
// return dimension;
// }

// public int getDimensionalSignature() {
// return dimensionalSignature;
// }
// }
// }