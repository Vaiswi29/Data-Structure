package spiderman;

public class Person {
    private int currentDim;
    private String name;
    private int sigDim;

    /*
     * Default constructor
     */
    public Person() {
        currentDim = 0;
        name = null;
        sigDim = 0;
    }

    public Person(int currentDim, String name, int sigDim) {
        this.currentDim = currentDim;
        this.name = name;
        this.sigDim = sigDim;
    }

    // Getter and Setter Methods
    public int getCurrentDim() {
        return currentDim;
    }

    public String name() {
        return name;
    }

    public int getSigDim() {
        return sigDim;
    }

    public void setcurrentDim(int currentDim) {
        this.currentDim = currentDim;
    }

    public void setname(String name) {
        this.name = name;
    }

    public void setsigDim(int sigDim) {
        this.sigDim = sigDim;
    }
}
