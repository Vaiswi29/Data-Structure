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
 * Read from the SpotInputFile with the format:
 * One integer
 * i. The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 * i. The Name of the anomaly which will go from the hub dimension to their home
 * dimension (String)
 * ii. The time allotted to return the anomaly home before a canon event is
 * missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 * i. The number of canon events at that anomalies home dimensionafter being
 * returned
 * ii. Name of the anomaly being sent home
 * iii. SUCCESS or FAILED in relation to whether that anomaly made it back in
 * time
 * iv. The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {

    public static void main(String[] args) {

        if (args.length < 5) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String anomaliesInputFile = args[3];
        String outputFile = args[4];

        ArrayList<Person> per = Collider.addPeople(spiderverseInputFile);
        //
        Node[] clusterT = Clusters.readInput(dimensionInputFile);
        // connect
        ArrayList<LinkedList<Node>> adjList = readDim(dimensionInputFile);
        Clusters.connectClusters(clusterT);
        connectDimInAdj(adjList, clusterT);

        StdIn.setFile(hubInputFile);
        int hubDim = StdIn.readInt();

        // StdIn.setFile(anomaliesInputFile);
        // int numA = StdIn.readInt();

        Djikstra djikstra = new Djikstra(adjList, hubDim);
        // StdOut.setFile(outputFile);
        // for(int i = 0 ; i < djikstra.pathTo(42).size(); i++){
        // StdOut.print(djikstra.pathTo(42).get(i)+ " ");

        // }

        GoHome(per, adjList, djikstra, anomaliesInputFile, outputFile);

    }

    public static void GoHome(ArrayList<Person> people, ArrayList<LinkedList<Node>> adjList,
            Djikstra djikstra, String anomaliesInputFile, String reportOutputFile) {

        StdIn.setFile(anomaliesInputFile);
        StdOut.setFile(reportOutputFile);

        int numA = StdIn.readInt();
        for (int i = 0; i < numA; i++) {
            String name = StdIn.readString();
            int time = StdIn.readInt();

            Person anomaly = null;
            for (Person p : people) {
                if (p.name().equals(name)) {
                    anomaly = p;
                    break;
                }
            }
            if (anomaly == null) {
                continue;
            }

            int homeD = anomaly.getSigDim();
            int current = anomaly.getSigDim();

            if (current == djikstra.s) {
                anomaly.setcurrentDim(homeD);
                // StdOut.print(Djikstra.geData(homeD, adjList).getCanonEvents() + " "+ name + "
                // SUCCESS "+ djikstra.s+ " "+ homeD);
            } else {
                LinkedList<Integer> path = djikstra.pathTo(homeD);

                if (path == null) {
                    continue;
                }

                int pathCost = 0;
                for (int j = 0; j < path.size() - 1; j++) {
                    int d1 = path.get(j);
                    int d2 = path.get(j + 1);
                    int index1 = TrackSpot.traverse(adjList, d1);
                    int index2 = TrackSpot.traverse(adjList, d2);

                    if (index1 == -1 || index2 == -1) {
                        // System.out.println("Error: Dimension not found in the adjacency list.");
                        continue;
                    }

                    LinkedList<Node> neighbors1 = adjList.get(index1);
                    LinkedList<Node> neighbors2 = adjList.get(index2);
                    //to add the pathCost from both the neighbors
                    boolean found = false;
                    for (Node n1 : neighbors1) {
                        if (n1.getData().getDimNum() == d2) {
                            pathCost += n1.getData().getDimWeight();
                            for (Node n2 : neighbors2) {
                                if (n2.getData().getDimNum() == d1) {
                                    pathCost += n2.getData().getDimWeight();
                                    found = true;
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    // boolean found = false;
                    // for (Node n1 : neighbors1) {
                    // if (n1.getData().getDimNum() == d2) {
                    // pathCost += n1.getData().getDimWeight();
                    // found = true;
                    // break;
                    // }
                    // break;
                    // }

                    // if (!found) {
                    // for (Node n2 : neighbors2) {
                    // if (n2.getData().getDimNum() == d1) {
                    // pathCost += n2.getData().getDimWeight();
                    // break;
                    // }
                    // }
                    // }
                }
                // var f = djikstra.pathTo(anomaly.getCurrentDim());
                // for (var v : djikstra.pathTo(anomaly.getCurrentDim())) {
                // pathCost += Djikstra.geData(v, adjList).getDimWeight();

                // }
                // pathCost = -(pathCost * 2) + (f.getFirst() + f.getLast());;
                // pathCost = pathCost
                // StdOut.print(pathCost + "pc" + " " + time +" " + anomaly.name());
                //if the total weight is greater than time then its failing else successing 
                if (pathCost > time) {
                    Data homeDimData = Djikstra.geData(homeD, adjList);
                    int newCanon = homeDimData.getCanonEvents() - 1;
                    homeDimData.setCanonEvents(newCanon);
                    StdOut.print((Djikstra.geData(homeD, adjList).getCanonEvents()) + " " + name + " FAILED ");
                    for (int j = 0; j < path.size(); j++) {
                        StdOut.print(" " + path.get(j));
                    }
                    StdOut.println();
                } else {
                    //for success
                    anomaly.setcurrentDim(homeD);

                    StdOut.print((Djikstra.geData(homeD, adjList).getCanonEvents()) + " " + name + " SUCCESS ");
                    for (int j = 0; j < path.size(); j++) {
                        StdOut.print(" " + path.get(j));
                    }
                    StdOut.println();

                }

            }

        }
    }

    //////////
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

// int pathCost = 0;
// for (int j = 0; j < path.size() - 1; j++) {
// int d1 = path.get(j);
// int d2 = path.get(j + 1);
// int index1 = TrackSpot.traverse(adjList, d1);
// int index2 = TrackSpot.traverse(adjList, d2);

// if (index1 == -1 || index2 == -1) {
// continue;
// }

// LinkedList<Node> neighbors1 = adjList.get(index1);
// LinkedList<Node> neighbors2 = adjList.get(index2);

// boolean found = false;
// for (Node n1 : neighbors1) {
// if (n1.getData().getDimNum() == d2) {
// pathCost += n1.getData().getDimWeight();
// found = true;
// break;
// }

// if (!found) {
// for (Node n2 : neighbors2) {
// if (n2.getData().getDimNum() == d1) {
// pathCost += n2.getData().getDimWeight();
// break;
// }
// }
// }

// }
// }
// initialize data structures
// Map<Integer, Integer> dimensionWeights = new HashMap<>();
// Map<Integer, List<Integer>> dimensionNeighbors = new HashMap<>();
// Map<Integer, Integer> dimensionCanonEvents = new HashMap<>();
// Map<Integer, Integer> spiderDimension = new HashMap<>();
// List<Anomaly> anomalies = new ArrayList<>();

// // read in data from input files
// readDimensions(dimensionWeights, dimensionNeighbors, dimensionCanonEvents,
// StdIn.readAllStrings());
// readSpiderverse(spiderDimension, StdIn.readAllStrings());
// readHub(dimensionWeights, dimensionNeighbors, StdIn.readInt());
// readAnomalies(anomalies, StdIn.readAllStrings());

// // compute shortest path for each anomaly
// for (Anomaly anomaly : anomalies) {
// int shortestPath = dijkstra(dimensionWeights, dimensionNeighbors,
// spiderDimension.get(0), anomaly.dimension);
// int canonEvents = dimensionCanonEvents.get(anomaly.dimension);
// if (shortestPath > anomaly.time) {
// StdOut.printf("%d %s FAILED\n", anomaly.dimension, anomaly.name);
// StdOut.println(canonEvents);
// } else {
// StdOut.printf("%d %s SUCCESS\n", anomaly.dimension, anomaly.name);
// StdOut.println(canonEvents);
// List<Integer> path = findPath(dimensionWeights, dimensionNeighbors,
// spiderDimension.get(0), anomaly.dimension);
// for (int i = 1; i < path.size(); i++) {
// StdOut.print(path.get(i) + " ");
// }
// StdOut.println();
// }
// }
// }

// private static void readDimensions(Map<Integer, Integer> dimensionWeights,
// Map<Integer, List<Integer>> dimensionNeighbors, Map<Integer, Integer>
// dimensionCanonEvents, String[] strings) {

// int numDimensions = StdIn.readInt();
// int initialSize = StdIn.readInt();
// double threshold = StdIn.readDouble();

// for (int i = 0; i < numDimensions; i++) {

// int dimension = StdIn.readInt();
// int canonEvents = StdIn.readInt();
// int weight = StdIn.readInt();

// dimensionWeights.put(dimension, weight);
// dimensionCanonEvents.put(dimension, canonEvents);

// dimensionNeighbors.putIfAbsent(dimension, new ArrayList<>());
// }

// for (int i = 0; i < numDimensions; i++) {

// int dimension =StdIn.readInt();
// int neighbor = StdIn.readInt();

// dimensionNeighbors.get(dimension).add(neighbor);
// dimensionNeighbors.get(neighbor).add(dimension);
// }
// }

// private static void readSpiderverse(Map<Integer, Integer> spiderDimension,
// String[] strings) {

// int numPeople = StdIn.readInt();

// for (int i = 0; i < numPeople; i++) {

// int dimension = StdIn.readInt();
// int signature = StdIn.readInt();

// spiderDimension.put(signature, dimension);
// }
// }

// private static void readHub(Map<Integer, Integer> dimensionWeights,
// Map<Integer, List<Integer>> dimensionNeighbors, int hub) {
// // do nothing, hub is passed as an argument
// }

// private static void readAnomalies(List<Anomaly> anomalies, String[] strings)
// {

// int numAnomalies = StdIn.readInt();

// for (int i = 0; i < numAnomalies; i++) {

// int dimension = StdIn.readInt();
// int time = StdIn.readInt();

// anomalies.add(new Anomaly(dimension, null, time));
// }
// }

// private static int dijkstra(Map<Integer, Integer> dimensionWeights,
// Map<Integer, List<Integer>> dimensionNeighbors, int start, int end) {
// Map<Integer, Integer> dist = new HashMap<>();
// Map<Integer, Integer> prev = new HashMap<>();

// for (int dimension : dimensionNeighbors.keySet()) {
// dist.put(dimension, Integer.MAX_VALUE);
// prev.put(dimension, null);
// }

// dist.put(start, 0);
// PriorityQueue<Integer> pq = new PriorityQueue<>(dimensionNeighbors.size(),
// (a, b) -> dist.get(a) - dist.get(b));
// pq.add(start);

// while (!pq.isEmpty()) {
// int curr = pq.poll();

// if (curr == end) {
// break;
// }

// for (int neighbor : dimensionNeighbors.get(curr)) {
// int alt = dist.get(curr) + dimensionWeights.get(neighbor);
// if (alt < dist.get(neighbor)) {
// dist.put(neighbor, alt);
// prev.put(neighbor, curr);
// pq.add(neighbor);
// }
// }
// }

// List<Integer> path = new ArrayList<>();
// int curr = end;
// while (curr != null) {
// path.add(0, curr);
// curr = prev.get(curr);
// }

// return dist.get(end);
// }

// private static List<Integer> findPath(Map<Integer, Integer> dimensionWeights,
// Map<Integer, List<Integer>> dimensionNeighbors, int start, int end) {
// Map<Integer, Integer> dist = new HashMap<>();
// Map<Integer, Integer> prev = new HashMap<>();

// for (int dimension : dimensionNeighbors.keySet()) {
// dist.put(dimension, Integer.MAX_VALUE);
// prev.put(dimension, null);
// }

// dist.put(start, 0);
// PriorityQueue<Integer> pq = new PriorityQueue<>(dimensionNeighbors.size(),
// (a, b) -> dist.get(a) - dist.get(b));
// pq.add(start);

// while (!pq.isEmpty()) {
// int curr = pq.poll();

// if (curr == end) {
// break;
// }

// for (int neighbor : dimensionNeighbors.get(curr)) {
// int alt = dist.get(curr) + dimensionWeights.get(neighbor);
// if (alt < dist.get(neighbor)) {
// dist.put(neighbor, alt);
// prev.put(neighbor, curr);
// pq.add(neighbor);
// }
// }
// }

// List<Integer> path = new ArrayList<>();
// int curr = end;
// while (curr != null) {
// path.add(0, curr);
// curr = prev.get(curr);
// }

// return path;
// }

// private static class Anomaly {
// int dimension;
// String name;
// int time;

// public Anomaly(int dimension, String name, int time) {
// this.dimension = dimension;
// this.name = name;
// this.time = time;
// }
