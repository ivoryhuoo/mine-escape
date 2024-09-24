/**
 * This class represents the escape path out of the mines
 * @author Ivory Huo
 *
 */
public class MineEscape {

	/**
	 * Private instance variables provided
	 */
	private Map map;//The map of the current mine
	private int numGold; //Count of how many chunks of gold you are holding
	private int[] numKeys; //Count of how many red, green, and blue keys you are holding
	
	/**
	 * Constructor that initializes the map variable using the given file name
	 * @param filename
	 */
	public MineEscape (String filename) {
		
		try {
	        // Initialize the map variable using the given filename
	        map = new Map(filename); // Create a map object and pass it the filename variable as a parameter
	        // Initialize numGold to 0 and numKeys as an array with 3 cells, all containing 0
	        numGold = 0;
	        numKeys = new int[3];
	    } catch (Exception e) { //Catch any exceptions that may occur from the Map class (use Exception as it catches all)
	        System.out.println(e.getMessage()); //Print the message from the exception
	    }
		
	}
	
	/**
	 * Method that determines the next call to walk onto from the current cell
	 * @param cell
	 * @return the MapCell object representing the next cell to walk onto from the current cell if one exists (otherwise return null)
	 */
	private MapCell findNextCell(MapCell cell) {
		
	    for (int i = 0; i < 4; i++) { //Iterate through each of the 4 directions
	        MapCell neighbor = cell.getNeighbour(i); //Get the neighbouring cell in the current direction
	        //Check if the neighbouring cells are NOT empty, marked, a wall, or lava! Iterate through if NOT
	        if (neighbor != null && !neighbor.isMarked() && !neighbor.isWall() && !neighbor.isLava()) {
	            if (neighbor.isExit()) { //If neighbour is exit, return it as the next cell to walk into
	                return neighbor;
	            } else if (isCollectible(cell, neighbor)) { //If neighbour is a collectible (key or gold), return it --> INVOKE PRIVATE HELPER METHOD
	                return neighbor;
	            }
	        }
	    }

	    for (int i = 0; i < 4; i++) { //Iterate through each of the 4 directions
	        MapCell neighbor = cell.getNeighbour(i); //Get the neighbouring cell in the current direction
	        //Check if the neighbouring cells are NOT empty, marked, a wall, or lava! Iterate through if NOT
	        if (neighbor != null && !neighbor.isMarked() && !neighbor.isWall() && !neighbor.isLava()) {
	            if (neighbor.isFloor()) { //Check if neighboring cell exists & is floor cell --> INVOKE PRIVATE HELPER METHOD
	                return neighbor;
	            }
	        }
	    }

	    for (int i = 0; i < 4; i++) { //Iterate through each of the 4 directions
	        MapCell neighbor = cell.getNeighbour(i); //Get the neighbouring cell in the current direction
	        //Check if the neighbouring cells are NOT empty, marked, a wall, or lava! Iterate through if NOT
	        if (neighbor != null && !neighbor.isMarked() && !neighbor.isWall() && !neighbor.isLava()) {
	            if (isLockedDoor(neighbor) && hasKey(cell, neighbor)) {  //Check if neighboring cell exists & is a locked door cell that can be unlocked (HAS KEY TOO) --> INVOKE PRIVATE HELPER METHOD
	                return neighbor; 
	            }
	        }
	    }

	    return null; //Return null if no suitable cell to move onto is found 
	}

	/**
	 * Private helper method that checks that if the cell is adjacent to one or more cells that contain a collectible item (a key or gold), go to the neighbour with the smallest index containing a collectible 
	 * @param cell
	 * @return neighbour with the smallest index containing a collectible 
	 */
	private boolean isCollectible(MapCell cell, MapCell neighbor) {
	    //Check if the cell or the neighbor contains a collectible item (gold, key, etc.)
	    boolean isCellCollectible = cell.isGoldCell() || cell.isKeyCell(); //Check cell
	    boolean isNeighborCollectible = neighbor.isGoldCell() || neighbor.isKeyCell(); //Check neighbour 

	    //IF TRUE, Go to neighbour with smallest index containing a collectible 
	    if (isCellCollectible && isNeighborCollectible) {
	        return neighbor.getID() < cell.getID();
	    }

	    //Return true only if the neighbor contains a collectible (and cell does not)
	    return isNeighborCollectible;
	}
	
	/**
	 * Private helper method that checks if there is a key for its respective coloured door (remember to decrement the key if it's been used!)
	 * @param cell
	 * @param door
	 * @return
	 */
	private boolean hasKey(MapCell cell, MapCell door) {
	    if (door.isRed() && numKeys[0] > 0) { //Check if the door is red and if there is a respective key for it
	        numKeys[0]--; //Decrement the count of red keys after each use of the key
	        return true;
	    } else if (door.isGreen() && numKeys[1] > 0) { //Check if the door is green and if there is a respective key for it
	        numKeys[1]--; //Decrement the count of green keys after each use of the key
	        return true;
	    } else if (door.isBlue() && numKeys[2] > 0) { //Check if the door is blue and if there is a respective key for it
	        numKeys[2]--; //Decrement the count of blue keys after each use of the key
	        return true;
	    }
	    return false;
	}

	/**
	 * Private helper method that checks if the door is locked 
	 * @param cell
	 * @return
	 */
	private boolean isLockedDoor(MapCell cell) {
	    // Check if the cell is a locked door
	    return cell.isLockCell();
	}
    
	/**
	 * Method that determines the path from the starting point to the exit cell if one exists
	 * @return
	 */
	public String findEscapePath() {
	    if (map == null) { //First check if the map is loaded 
	        return "Error: Map not loaded.";
	    }
	    //Initialize stack and starting cell, mark the starting cell as part of the path
	    ArrayStack<MapCell> S = new ArrayStack<>(); //Initialize ArrayStack S to store MapCell 
	    MapCell startCell = map.getStart();
	    S.push(startCell); //Push the starting cell onto S
	    startCell.markInStack(); //Mark the starting variable as in-stack

	    String pathString = "Path: " + startCell.getID() + " "; //Include the starting cell in the path string!
	    int currentGoldCount = 0; //Track gold count
	    boolean running = true; //Track loop status --> Set boolean variable running to be true 

	    //While S is not empty & running is true, loop
	    while (!S.isEmpty() && running) { 
	        MapCell curr = S.peek(); //Get the current cell from the top of the stack --> curr = peek at S

	        if (curr.isExit()) { //If current cell is an exit
	            running = false; //Set running = false 
	            break; //End the loop immediately 
	        } else if (curr.isKeyCell()) { //If current cell is a key cell
	            updateNumKeys(curr); //Determine its colour and update numKeys accordingly 
	            //When picking up a piece of gold, a key, or unlocking a door it is important to call the method changeToFloor 
	            //The method changes it to a floor cell to vidually indicate that the item has been removed 
	            curr.changeToFloor(); //Change cell containing key to floor cell
	        } else if (curr.isGoldCell()) { //If current cell contains gold, update count & change cell to floor 
	            currentGoldCount++; 
	            curr.changeToFloor(); //Change cell containing gold to floor cell
	        } else if (isAdjacentToLava(curr)) { //If current cell is adjacent to lava, LOSE THE GOLD (reset gold count)
	            currentGoldCount = 0; 
	        }

	        MapCell next = findNextCell(curr); //Find next cell to move onto

	        if (next == null) { //If there no next cell to move onto, backtrack by removing current cell from stack
	            curr = S.pop();
	            curr.markOutStack(); //Unmark cell as part of path 
	        } else { //Move to the next cell, update path string, mark cell as part of the path
	            pathString += next.getID() + " "; //Update path string 
	            S.push(next); //Push next onto S
	            next.markInStack(); //Mark next as in-stack
	            //If the next cell is a locked door and has a key, unlock the door and update keys!
	            if (next.isLockCell() && hasKey(curr, next)) {
	                next.changeToFloor(); //Change the cell containing the locked door to a floor cell
	                updateNumKeys(next); //Update number of keys (door is unlocked through that key now)
	            }
	        }
	    }
	    //If exit found, return the path string with gold count
	    if (!running) { 
	        pathString += currentGoldCount + "G"; //Print string + current gold count in the proper formatting
	        return pathString;
	    } else { //No exit found = No solution found 
	        return "No solution found";
	    }
	}

	/**
	 * Private helper method to update the number of keys based on type of key
	 * @param keyCell
	 */
	private void updateNumKeys(MapCell keyCell) { 
	    if (keyCell.isRed()) { //If red key
	        numKeys[0]++; //Increment count 
	    } else if (keyCell.isGreen()) { //If green key
	        numKeys[1]++; //Increment count
	    } else if (keyCell.isBlue()) { //If blue key
	        numKeys[2]++; //Increment count
	    }
	}

	/**
	 * Private helper method to check if a cell is adjacent to lava
	 * @param cell
	 * @return
	 */
	private boolean isAdjacentToLava(MapCell cell) {
	    for (int i = 0; i < 4; i++) { //Iterate through each direction 
	        MapCell neighbor = cell.getNeighbour(i); //Get the neighbour in the current direction
	        //Check if neighbour exists and if it contains lava
	        if (neighbor != null && neighbor.isLava()) {
	            return true; //Return true if adjacent to lava
	        }
	    }
	    return false; //Return false if NOT adjacent with lava
	}
	
	/**
	 * Method added directly from the assignment to run one map at a time!
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
		System.out.print("Map file not given in the arguments.");
		} else {
		MineEscape search = new MineEscape(args[0]);
		String result = search.findEscapePath();
		System.out.println(result);
		}
	}
}
