package spiderman;

import java.util.List;

public class Data implements Comparable<Data> {
    private int dimNum;
    private int canonEvents;
    private int dimWeight;
    private List<String> characters;
    private Person person;

    /*
     * Default constructor
     */
    public Data() {
        dimNum = 0;
        canonEvents = 0;
        dimWeight = 0;
        person = null;
    }

    public Data(int dimNum, int canonEvents, int dimWeight) {
        this.dimNum = dimNum;
        this.canonEvents = canonEvents;
        this.dimWeight = dimWeight;
        this.person = null;
    }

    // Getter and Setter Methods
    public int getDimNum() {
        return dimNum;
    }

    public int getCanonEvents() {
        return canonEvents;
    }

    public int getDimWeight() {
        return dimWeight;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public Person getPerson() {
        return person;
    }

    public void setDimNum(int dimNum) {
        this.dimNum = dimNum;
    }

    public void setCanonEvents(int canonEvents) {
        this.canonEvents = canonEvents;
    }

    public void setDimWeight(int dimWeight) {
        this.dimWeight = dimWeight;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int compareTo(Data o) {
        return Integer.compare(dimWeight, o.dimWeight);
    }
}
