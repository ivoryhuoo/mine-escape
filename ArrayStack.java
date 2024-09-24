/**
 * This class represents a Stack collection implemented using an array
 * @author Ivory Huo
 *
 */
public class ArrayStack<T> implements StackADT<T> {
	
	/**
	 * Private instance variables provided 
	 */
	private T[] array; //Array to hold items in the stack
	private int top; //Integer that keeps track of where the top of the stack is 
	
	/**
	 * Constructor 
	 */
	public ArrayStack() {
		array = (T[])new Object [10]; //Initialize the array with a capacity of 10
		top = -1; //Initialize top to -1 
	}
	
	/**
	 * Method that adds an element to the top of the stack and updates the value of top
	 */
	public void push (T element) {
		//Check if the array is at 75% capacity or over
		if (size() >= 0.75 * array.length) {
			expandCapacity(); //Expand the capacity by adding 10 more spots
		}
		top++; //Increment top
		array[top] = element; //Update the value of top
	}
	
	/**
	 * Method that returns the element from the top of the stack and updates the value of top 
	 */
	public T pop() throws StackException {
		//Check if the stack is empty 
		if (isEmpty()) {
			throw new StackException("Stack is empty"); //Throw exception if stack empty
		}

        //Check if the array is at 25% capacity or less AND has a capacity greater than or equal to 20
        if (size() <= 0.25 * array.length && array.length >= 20) {
            // If so, shrink the capacity by removing 10 spots
            shrinkCapacity();
        }
        //Remove and return the element from the top of the stack
        T poppedElement = array[top--]; 
        return poppedElement; //Return the element from the top of the stack 
	}
	
	/**
	 * Method that returns the element from the top of the stack without removing it
	 */
	public T peek() throws StackException {
		//Check if the stack is empty
		if (isEmpty()) {
			throw new StackException("Stack is empty"); //Throw exception if stack empty
		}
		return array[top]; //Return the element from the top of the stack without removing it
	}
	
	/**
	 * Method that returns true if stack is empty and false otherwise
	 */
	public boolean isEmpty() {
		return top == -1; //Top variable must be -1 when the stack is empty 
	}
	
	/**
	 * Return the number of elements in the stack
	 */
	public int size() {
		return top + 1; //Index starts at 0, so add one 
	}
	
	/**
	 * Method that clears out all the elements from the stack and restores it to its original state
	 */
	public void clear() {
		//Clear out all elements from the stack
        array = (T[]) new Object[10]; //Return to original state: 10 empty spaces in the array
        //Return to original state: top = -1
        top = -1;
	}
	
	/**
	 * Method that returns the length (capacity) of the array
	 * @return array length/capacity
	 */
	public int getCapacity() {
		return array.length;
	}
	
	/**
	 * Method that returns the top index
	 * @return top index
	 */
	public int getTop() {
		return top; 
	}
	
	/**
	 * Method that builds and returns a string of the entries
	 */
	public String toString() {
		if (isEmpty()) { //If the stack is empty, return "Empty stack"
			return ("Empty stack.");
		} else { //Otherwise, build and return a string
			String result = "Stack: "; //Begin the string with "Stack: " 
			
            for (int i = top; i >= 0; i--) { //Follow up with all the items in the stack from the top (first) to the bottom (last)
                result += array[i]; //Iterate through the array  
                if (i > 0) {
                    result += ", "; //Entries should be separated by a comma with a single space
                } else {
                    result += "."; //Last entry should be followed by a period (but no space)
                }
            }
            
            return result;
		}
	}
	
	/**
	 * Method that expands the array
	 */
	private void expandCapacity() {
		//Check the fraction of the array being used: Number of items in the array divided by the capacity
		if ((double) size() / array.length >= 0.75) { //If fraction < 0.75, do nothing and skip this step, cast ints to doubles
            //Expand the capacity by adding 10 additional spots in the array
            T[] newArray = (T[]) new Object[array.length + 10]; 
            
            //Copy elements from the old array to the new array
            for (int i = 0; i <= top; i++) {
                newArray[i] = array[i];
            }
            
            // Update the array reference to the new array
            array = newArray;
        }
	}
	
	/**
	 * Method that shrinks the array
	 */
	private void shrinkCapacity() {
		//Check the fraction of the array being used and the capacity conditions
        if ((double) size() / array.length <= 0.25 && array.length >= 20) { //If fraction > 0.25 and/or the array's capacity is < 20, do nothing and skip this step, cast ints to doubles
            //Shrink the capacity by removing 10 spots from the array
            T[] newArray = (T[]) new Object[array.length - 10]; 

            //Copy elements from the old array to the new array
            for (int i = 0; i <= top; i++) {
                newArray[i] = array[i];
            }

            //Update the array reference to the new array
            array = newArray;
        }
	}
}
