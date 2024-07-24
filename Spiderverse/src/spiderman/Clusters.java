package spiderman;

// import java.util.*;
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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to
 * that dimension in order (space separated)
 * n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
            return;
        }
        // get the input file and output file from the terminal
        String inputFile = args[0];
        String outputFile = args[1];

        Node[] clusterTable = readInput(inputFile);
        connectClusters(clusterTable);
        printOutput(clusterTable, outputFile);
    }

    public static Node[] readInput(String inputFile) {
        // int clustersSoFar = 0;
        // int dimSoFar = 0;
        // Node[] clusterTable = null;
        // set the input file
        StdIn.setFile(inputFile);

        // get a,b,c
        int numDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();
        // new node for the clusterTable
        Node[] clusterTable = new Node[initialSize];
        // adding
        int h = 0;
        // for (int i = 0; i < numDimensions; i++) {
        while (h < numDimensions) {
            // for loop for the a lines and get the dimNUm, canonEvents and dimWeight
            int dimNum = StdIn.readInt();
            int canonEvents = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            // hash function
            int clusterIndex = dimNum % clusterTable.length;
            // get the data
            Data data = new Data(dimNum, canonEvents, dimWeight);
            // set the new node
            Node current = new Node(data, null);
            // if the table is null
            // set it to current
            if (clusterTable[clusterIndex] == null) {
                clusterTable[clusterIndex] = current;
            } else {
                // else set next
                current.setNext(clusterTable[clusterIndex]);
                clusterTable[clusterIndex] = current;
            }
            h++;
            // get the rehasing
            if ((h / clusterTable.length) >= threshold) {
                clusterTable = rehash(clusterTable);
            }
        }
        return clusterTable;
    }

    // rehasing function
    public static Node[] rehash(Node[] oldTable) {
        int newSize = oldTable.length * 2;
        Node[] newTable = new Node[newSize];

        for (int j = 0; j < oldTable.length; j++) {
            Node node = oldTable[j];
            while (node != null) {
                // get the temp new node
                Node temp = new Node(node.getData(), null);
                // get the data in the tempData
                int tempData = temp.getData().getDimNum();
                // the new index based on new data
                int newIndex = tempData % newTable.length;
                // repeat
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = temp;
                } else {
                    // Node temp = newTable[newIndex];
                    temp.setNext(newTable[newIndex]);
                    newTable[newIndex] = temp;
                }
                node = node.getNext();
            }
        }
        return newTable;
    }

    // rehashing
    // double loadFactor = (double) r;
    public static void connectClusters(Node[] clusterTable) {
        for (int i = 0; i < clusterTable.length; i++) {
            // indexed
            int prevIndex1 = (i - 1 + clusterTable.length) % clusterTable.length;
            int prevIndex2 = (i - 2 + clusterTable.length) % clusterTable.length;
            // nodes for the data
            Data prevNode1 = clusterTable[prevIndex1].getData();
            Data prevNode2 = clusterTable[prevIndex2].getData();
            // the nodes and then to connect them
            Node prev1 = new Node(prevNode1, null);
            Node prev2 = new Node(prevNode2, null);
            prev1.setNext(prev2);
            Node current = clusterTable[i];
            // connecting the nodes
            while (current.getNext() != null) {
                current = current.getNext();
            }
            // current.setNext(prevNode2);
            if (current.getNext() == null) {
                current.setNext(prev1);
            }
        }
    }

    // print the output
    private static void printOutput(Node[] clusterTable, String outputFile) {
        StdOut.setFile(outputFile);
        // output file
        for (int i = 0; i < clusterTable.length; i++) {
            Node ptr = clusterTable[i];
            while (ptr != null) {
                StdOut.print(ptr.getData().getDimNum() + " ");
                ptr = ptr.getNext();
            }
            StdOut.println();
        }
    }

}
