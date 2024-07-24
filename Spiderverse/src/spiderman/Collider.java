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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 * all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE

        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String colliderOutputFile = args[2];

        // read
        StdIn.setFile(dimensionInputFile);

        // created an arraylist of LL and read the file
        ArrayList<LinkedList<Node>> adjList = readDim(dimensionInputFile);
        // table for the clusters
        Node[] clusterT = Clusters.readInput(dimensionInputFile);
        // connect
        Clusters.connectClusters(clusterT);

        // read people from spiderverse and add in arraylist
        // ArrayList<Person> people = readSpiderVerse(spiderverseInputFile);

        // Map<Integer, List<Integer>> dimensionPeople =
        // readSpiderverseFile(spiderverseInputFile);
        // connectPeople(adjList, dimensionPeople);
        // connectPeople(adjList, people);
        // addPeople(adjList, people);
        connectDimInAdj(adjList, clusterT); // connect the dimensions
        printOutput(colliderOutputFile, adjList); // print the output
        addPeople(spiderverseInputFile);

        // int num = StdIn.readInt();
        // String[] names = new String[num];
        // int[][] dimenesions = new int[num][2];
        // int count = 0;
        // while(count < num){
        // int dim = StdIn.readInt();
        // String name = StdIn.readString();
        // int sigDim = StdIn.readInt();
        // names[count] = name;
        // dimenesions[count][0] = dim;
        // dimenesions[count][1] = sigDim;
        // count++;
        // }

    }

    // add the people to the adjacency list

    public static ArrayList<Person> addPeople(String spiderverseInputFile) {
        StdIn.setFile(spiderverseInputFile);
        int num = StdIn.readInt();
        String[] names = new String[num];
        int[][] dimensions = new int[num][2];
        ArrayList<Person> person = new ArrayList<>(num);
        int count = 0;
        while (count < num) {
            int dim = StdIn.readInt();
            String name = StdIn.readString();
            int sigDim = StdIn.readInt();
            Person p = new Person(dim, name, sigDim);
            person.add(p);
            count++;
        }
        return person;
    }
    // private static Map<Integer, List<Integer>> readSpiderverseFile(String
    // spiderverseInputFile) {
    // StdIn.setFile(spiderverseInputFile);
    // int numPeople = StdIn.readInt();
    // Map<Integer, List<Integer>> dimPeople = new HashMap<>();
    // for(int i = 0; i < numPeople; i++){
    // int dim = StdIn.readInt();
    // String name = StdIn.readString();
    // int sig = StdIn.readInt();

    // if(!dimPeople.containsKey(dim)){
    // dimPeople.put(dim, new ArrayList<>());
    // }
    // }
    // return dimPeople;
    // }

    // private static void connectPeople(ArrayList<LinkedList<Node>> adjList,
    // Map<Integer, List<Integer>> dimensionPeople) {
    // for (Map.Entry<Integer, List<Integer>> entry : dimensionPeople.entrySet()) {
    // int dimension = entry.getKey();
    // List<Integer> people= entry.getValue();

    // for (int person : people) {
    // Node personNode = new Node(new Data(person, 0, 0), null);
    // adjList.get(dimension).add(personNode);
    // }
    // }
    // }
    // read the dimensions as arraylist from dimensionINputFile // same as
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
    // connect the dimenesions into the adjacency list
    // get the first and ptr iterate through it and connect it via dim

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

    // arraylist for person which will store the information about people
    // private static ArrayList<Person> readSpiderVerse(String spiderverseInputFile)
    // {
    // ArrayList<Person> people = new ArrayList<>();
    // StdIn.setFile(spiderverseInputFile);
    // int numPeople = StdIn.readInt();
    // for(int i = 0; i < numPeople; i++){
    // int currentDim = StdIn.readInt();
    // String name = StdIn.readString();
    // int sigDim = StdIn.readInt();
    // people.add(new Person(currentDim, name, sigDim));
    // }
    // return people;
    // }

    // private static void connectPeople(ArrayList<LinkedList<Node>> adjList,
    // ArrayList<Person> people){
    // for(Person person: people){
    // int currentDimIndex = person.getCurrentDim();
    // int dimSigIndex = person.getSigDim();

    // Node currentDimNode = new Node(new Data(currentDimIndex, 0, 0), null);
    // Node sigDimNode = new Node(new Data(dimSigIndex, 0, 0), null);

    // if(adjList.get(currentDimIndex).contains(sigDimNode)){
    // Node temp = adjList.get(currentDimIndex).getFirst();
    // while(temp != null){
    // if(temp.getData().getDimNum() == dimSigIndex){
    // break;
    // }
    // temp = temp.getNext();
    // }
    // if(temp == null){
    // adjList.get(currentDimIndex).add(sigDimNode);
    // }
    // } else{
    // adjList.get(currentDimIndex).add(sigDimNode);
    // }
    // if(adjList.get(dimSigIndex).contains(currentDimNode)){
    // Node temp = adjList.get(dimSigIndex).getFirst();
    // while(temp != null){
    // if(temp.getData().getDimNum() == currentDimIndex)break;
    // temp = temp.getNext();
    // }
    // if(temp == null){
    // adjList.get(dimSigIndex).add(currentDimNode);
    // }
    // } else {
    // adjList.get(dimSigIndex).add(currentDimNode);
    // }
    // }
    // }

    // private static void addPeople(ArrayList<LinkedList<Node>> adjList,
    // ArrayList<Person> people ){
    // for(Person person: people){
    // int currentDimIndex = person.getCurrentDim();
    // int dimSigIndex = person.getSigDim();

    // adjList.get(currentDimIndex).add(dimSigIndex, null);
    // adjList.get(dimSigIndex).add(currentDimIndex, null); }
    // }

    // print the output
    private static void printOutput(String colliderOutputFile, ArrayList<LinkedList<Node>> adjList) {
        StdOut.setFile(colliderOutputFile);
        for (int k = 0; k < adjList.size(); k++) {
            LinkedList<Node> out = adjList.get(k);
            for (int p = 0; p < out.size(); p++) {
                StdOut.print(out.get(p).getData().getDimNum() + " ");
            }
            StdOut.println();
        }
    }
}
