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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 * i. Line one: The starting dimension of Spot (int)
 * ii. Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has
 * visited (space separated)
 * 
 * @author Seth Kelley
 */
public class TrackSpot {

    public static void main(String[] args) {

        if (args.length < 4) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
            return;
        }
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String spotInputFile = args[2];
        String trackSpotOutputFile = args[3];
        Node[] clusterT = Clusters.readInput(dimensionInputFile);
        // connect
        ArrayList<LinkedList<Node>> adjList = readDim(dimensionInputFile);
        Clusters.connectClusters(clusterT);
        connectDimInAdj(adjList, clusterT);
        // set the spot file to take the values
        StdIn.setFile(spotInputFile);
        int startDim = StdIn.readInt();
        int finalDim = StdIn.readInt();

        // use the dfs implementation
        // and iterate till th marked value didnt reach the final destination
        // print the values
        ArrayList<Integer> marked = new ArrayList<>();
        dfs(startDim, marked, adjList);
        StdOut.setFile(trackSpotOutputFile);
        for (int i : marked) {
            if (i == finalDim) {
                StdOut.print(i + " ");
                break;
            }
            StdOut.print(i + " ");
        }

    }

    // dfs implementation
    public static void dfs(int k, ArrayList<Integer> marked, ArrayList<LinkedList<Node>> adj) {
        marked.add(k);
        int j = traverse(adj, k);
        for (int i = 0; i < adj.get(j).size(); i++) {
            if (!marked.contains(adj.get(j).get(i).getData().getDimNum())) {
                dfs(adj.get(j).get(i).getData().getDimNum(), marked, adj);
            }
        }
    }

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

    // traversing to find the position using dfs
    public static int traverse(ArrayList<LinkedList<Node>> adjList, int d) {
        int position = -1;
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).get(0).getData().getDimNum() == d) {
                position = i;
                break;
            }
        }
        // if (position < 0)
        // throw new IllegalArgumentException("No such node");
        return position;
    }

    // int dimNumFirst = clusterFirst.getData().getDimNum();
    // int dimNumPtr = dim.getData().getDimNum();

    // // Find the cluster in adjList that corresponds to clusterFirst
    // LinkedList<Node> clusterListFirst = null;
    // for (LinkedList<Node> clusterList : adjList) {
    // if (clusterList.getFirst().getData().getDimNum() == dimNumFirst) {
    // clusterListFirst = clusterList;
    // break;
    // }
    // }

    // // Find the cluster in adjList that corresponds to dim
    // LinkedList<Node> clusterListPtr = null;
    // for (LinkedList<Node> clusterList : adjList) {
    // if (clusterList.getFirst().getData().getDimNum() == dimNumPtr) {
    // clusterListPtr = clusterList;
    // break;
    // }
    // }

    // // Connect dim to clusterFirst and clusterFirst to dim in adjList
    // if (clusterListFirst != null && clusterListPtr != null) {
    // clusterListFirst.add(dim);
    // clusterListPtr.add(clusterFirst);
    // }
    // }

}

// String dimensionInputFile = args[0];
// String spiderverseInputFile = args[1];
// String spotInputFile = args[2];
// String trackSpotOutputFile = args[3];
// StdIn.setFile(dimensionInputFile);

// Collider.readDim(dimensionInputFile);
// ArrayList<LinkedList<Node>> adjList = readDim(dimensionInputFile);
// Node[] clusterT = Clusters.readInput(dimensionInputFile);
// //connect
// Clusters.connectClusters(clusterT);

// Collider.addPeople(spiderverseInputFile);

// StdIn.setFile(spotInputFile);

// // Create a graph from the input data
// List<List<Integer>> graph = new ArrayList<>();
// for (int i = 0; i < numDimensions; i++) {
// graph.add(new ArrayList<>());
// }
// for (int i = 0; i < numDimensions; i++) {
// int dimension = dimensions[i];
// for (int j = i + 1; j < numDimensions; j++) {
// int otherDimension = dimensions[j];
// if (dimensionWeights[i] + dimensionWeights[j] < 20) {
// graph.get(dimension).add(otherDimension);
// graph.get(otherDimension).add(dimension);
// }
// }
// }

// // Perform a depth-first search from the starting dimension
// boolean[] visited = new boolean[numDimensions];
// List<Integer> path = new ArrayList<>();
// int startDimension = 0;
// int destinationDimension = 0;
// StdIn.setFile(spotInputFile);
// startDimension = StdIn.readInt();
// destinationDimension = StdIn.readInt();

// dfs(graph, startDimension, destinationDimension, visited, path);

// // Output the path to the output file
// StdOut.setFile(trackSpotOutputFile);
// for (int i = 0; i < path.size(); i++) {
// StdOut.print(path.get(i));
// if (i != path.size() - 1) {
// StdOut.print(" ");
// }
// }
// StdOut.println();
// }

// private static void dfs(List<List<Integer>> graph, int startDimension, int
// destinationDimension, boolean[] visited, List<Integer> path) {
// if (startDimension == destinationDimension) {
// path.add(startDimension);
// return;
// }
// visited[startDimension] = true;
// path.add(startDimension);
// for (int neighbor : graph.get(startDimension)) {
// if (!visited[neighbor]) {
// dfs(graph, neighbor, destinationDimension, visited, path);
// }
// }
// path.remove(path.size() - 1);
// }
// }
