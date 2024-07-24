package spiderman;

public class Node {
    private Data data; // data portion of node
    private Node next; // link to next node in the list

    /*
     * Default constructor
     */
    public Node() {
        data = null;
        next = null;
    }

    /*
     * Constructor
     * 
     * @param data object holding information about a transaction
     * 
     * @param next link to the next node in the list
     */
    public Node(Data data, Node next) {
        this.data = data;
        this.next = next;
    }

    // "To Get" and "To Set" Methods
    public Data getData() {
        return data;
    }

    public Node getNext() {
        return next;
    }

    public void setData(Data dataInput) {
        data = dataInput;
    }

    public void setNext(Node nextInput) {
        next = nextInput;
    }

}
