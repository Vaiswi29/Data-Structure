package climate;

import java.util.ArrayList;


/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine(); // Skips header
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] index = inputLine.split(",");
        String state = index[2];
        //checks if the first state is null, if yes, then creates a new node
        if(firstState == null){
            firstState = new StateNode();
            firstState.setName(state);
        }
        //will check if the state already exists
        StateNode ptr = firstState;
        while (ptr.next != null) {
            if(ptr.getName().equals(state)){
                return;
            }
            ptr = ptr.next;
        }
        //this will check the last node
        if(ptr.getName().equals(state)) return;

        //create a new node and add
        StateNode newState = new StateNode();
        newState.setName(state);
        ptr.setNext(newState);
    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] index = inputLine.split(",");
        String state = index[2];
        String county = index[1];
        //checks if the ptr is not at null
        if(firstState == null) return;

        //sets the pointer to null and will traverse till the county
        StateNode ptr = firstState;
        while(ptr != null){
            if(ptr.getName().equals(state)) {
                CountyNode ptrCounty = ptr.getDown();
                //creates a new county and sets the ptr down
                if(ptrCounty == null){
                    CountyNode newCounty = new CountyNode();
                    newCounty.setName(county);
                    ptr.setDown(newCounty);
                } else {
                    //returns if found 
                    while(ptrCounty.getNext() != null){
                        if(ptrCounty.getName().equals(county)){
                            return;
                        }
                        ptrCounty = ptrCounty.getNext();
                    }
                    if(ptrCounty.getName().equals(county)){
                        return;
                    }
                    //creates a new node for the county
                    CountyNode newCounty = new CountyNode();
                    newCounty.setName(county);
                    ptrCounty.setNext(newCounty);
                    
                } 
            }
            ptr = ptr.getNext();  
        }
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] index = inputLine.split(",");
        String state = index[2];
        String county = index[1];
        String community = index[0];

        Data data = new Data();
        data.setPrcntAfricanAmerican(Double.parseDouble(index[3]));
        data.setPrcntNative(Double.parseDouble(index[4]));
        data.setPrcntAsian(Double.parseDouble(index[5]));
        data.setPrcntWhite(Double.parseDouble(index[8]));
        data.setPrcntHispanic(Double.parseDouble(index[9]));
        data.setAdvantageStatus(index[19]);
        data.setPMlevel(Double.parseDouble(index[49]));
        data.setChanceOfFlood(Double.parseDouble(index[37]));
        data.setPercentPovertyLine(Double.parseDouble(index[121]));
        //new node will set the info based on the data
    CommunityNode newNode = new CommunityNode();
    newNode.setName(community);
    newNode.setInfo(data);
        //same will traverse till community and add new community
        StateNode ptr = firstState;
        while(ptr != null){
            if(ptr.getName().equals(state)){
                CountyNode ptrCounty = ptr.getDown();

                while(ptrCounty != null){
                    if(ptrCounty.getName().equals(county)){
                        
                        if(ptrCounty.getDown() == null){
                            ptrCounty.setDown(newNode);
                            return;
                        } else {
                            CommunityNode currentCommunity = ptrCounty.getDown();
                            while(currentCommunity.getNext() != null){
                                currentCommunity = currentCommunity.getNext();
                            }

                            currentCommunity.setNext(newNode);
                            return;
                        }
                    }
                   ptrCounty = ptrCounty.getNext(); 
                }
            }
            ptr = ptr.getNext();
        }
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        // WRITE YOUR CODE HERE
        int count = 0;
        //will traverse till community and then will check if its true and then would check the race entered
        StateNode ptr = firstState;
        while(ptr != null){
            CountyNode ptrCounty = ptr.getDown();
            while(ptrCounty != null){
                CommunityNode ptrCommunity = ptrCounty.getDown();
                while(ptrCommunity != null){
                    Data data = ptrCommunity.getInfo();
                    if (data.getAdvantageStatus().equalsIgnoreCase("True")){
                        
                        double racePercent = 0.0;
                        switch(race){
                            case "African American":
                            racePercent = ptrCommunity.getInfo().getPrcntAfricanAmerican();
                            break;
                            case "Native American":
                            racePercent = ptrCommunity.getInfo().getPrcntNative();
                            break;
                            case "Asian American":
                            racePercent = ptrCommunity.getInfo().getPrcntAsian();
                            break;
                            case "White American":
                            racePercent = ptrCommunity.getInfo().getPrcntWhite();
                            break;
                            case "Hispanic American":
                            racePercent = ptrCommunity.getInfo().getPrcntHispanic();
                            break;
                        }
                        //will break and increment 
                        if(racePercent * 100 >= userPrcntage){
                            count++;
                        }

                    }
                    ptrCommunity = ptrCommunity.getNext();
                }
                ptrCounty = ptrCounty.getNext();
            }
            ptr = ptr.getNext();
        }
        return count; // update this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

	//WRITE YOUR CODE HERE
    int count = 0;
        //same as disadvantaged but will check if false
    StateNode ptr = firstState;
    while(ptr != null){
        CountyNode ptrCounty = ptr.getDown();
        while(ptrCounty != null){
            CommunityNode ptrCommunity = ptrCounty.getDown();
            while(ptrCommunity != null){
                Data data = ptrCommunity.getInfo();
                if (data.getAdvantageStatus().equalsIgnoreCase("False")){
                    
                    double racePercent = 0.0;
                    switch(race){
                        case "African American":
                        racePercent = ptrCommunity.getInfo().getPrcntAfricanAmerican();
                        break;
                        case "Native American":
                        racePercent = ptrCommunity.getInfo().getPrcntNative();
                        break;
                        case "Asian American":
                        racePercent = ptrCommunity.getInfo().getPrcntAsian();
                        break;
                        case "White American":
                        racePercent = ptrCommunity.getInfo().getPrcntWhite();
                        break;
                        case "Hispanic American":
                        racePercent = ptrCommunity.getInfo().getPrcntHispanic();
                        break;
                    }

                    if(racePercent * 100 >= userPrcntage){
                        count++;
                    }

                }
                ptrCommunity = ptrCommunity.getNext();
            }
            ptrCounty = ptrCounty.getNext();
        }
        ptr = ptr.getNext();
    }
        return count; // update this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {
        
        // WRITE YOUR METHOD HERE
        //create an arraylist and then traverse 
        ArrayList<StateNode> statesAbove = new ArrayList<>();
        StateNode ptr = firstState;
        while(ptr != null){
            CountyNode ptrCounty = ptr.getDown();
            while(ptrCounty != null){
                CommunityNode ptrCommunity = ptrCounty.getDown();
                while(ptrCommunity != null){
                    //if the data is greater than the value entered, the state is added
                    Data data = ptrCommunity.getInfo();
                    if(data.getPMlevel() >= PMlevel){
                        if(!statesAbove.contains(ptr)){
                            statesAbove.add(ptr);
                        } 
                    }
                    ptrCommunity = ptrCommunity.getNext();
                }
                ptrCounty = ptrCounty.getNext();
            }
            ptr = ptr.getNext();
        }
        return statesAbove; // update this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

	// WRITE YOUR METHOD HERE
    int count = 0;
    StateNode ptr = firstState;
    //traverses and checks if the data is greater than entered, if yes increases the count
    while(ptr != null){
        CountyNode ptrCounty = ptr.getDown();
        while(ptrCounty != null){
            CommunityNode ptrCommunity = ptrCounty.getDown();
            while(ptrCommunity != null){
                Data data = ptrCommunity.getInfo();
                if(data.getChanceOfFlood() >= userPercntage){
                    count++;
                }
                ptrCommunity = ptrCommunity.getNext();
            }
            ptrCounty = ptrCounty.getNext();
        }
        ptr = ptr.getNext();
    }
        return count; // update this line
    }


    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

	//WRITE YOUR METHOD HERE
    //creates an arraylist and then check if the state is equal to the one entered
    ArrayList<CommunityNode> list = new ArrayList<>();
        StateNode ptr = firstState;
        //get the state name
    while (ptr != null && !ptr.getName().equals(stateName)) {
        ptr = ptr.getNext();
    }
    
   //once the state is found, tranverse till the community
    if (ptr != null) {
        CountyNode ptrCounty = ptr.getDown();
       
        while (ptrCounty != null) {
            CommunityNode ptrCommunity = ptrCounty.getDown();
            
            while (ptrCommunity != null) {
                //if the list is not full
                if (list.size() < 10) {
                    list.add(ptrCommunity);
                } else {
                    //find the community with highest poverty line percentage
                   //lower the income higher the poverty
                    double highestPercent = Double.MAX_VALUE;
                    int indexToRemove = -1;
                    for (int i = 0; i < list.size(); i++) {
                    //check if the info is less than the percent
                        double povertyLine = list.get(i).getInfo().getPercentPovertyLine();
                        if (povertyLine < highestPercent) {
                            highestPercent = povertyLine;
                            indexToRemove = i;
                        }
                    }
                    //if ptr community has a lower percent then replace if the arraylist is not empty
                    double ptrPovertyLine = ptrCommunity.getInfo().getPercentPovertyLine();
                    if (ptrPovertyLine > highestPercent) {
                        if(indexToRemove != -1){
                            list.set(indexToRemove, ptrCommunity);
                        }
                        
                    }
                }
                ptrCommunity = ptrCommunity.getNext();
            }
            ptrCounty = ptrCounty.getNext();
        }
    }


    return list;
    }
}

