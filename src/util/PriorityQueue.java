package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A priority queue class implemented using a min heap.
 * Priorities cannot be negative.
 * 
 * @author  Tyler Weir 
 * @version September 27, 2021 
 *
 */
public class PriorityQueue {

    //	protected Map<Integer, Integer> location;
    protected Map<Integer, Integer> location;
	protected List<Pair<Integer, Integer>> heap;
	/**
	 *  Constructs an empty priority queue
	 */
	public PriorityQueue() {
       this.location = new HashMap<Integer, Integer>();
       this.heap = new ArrayList<Pair<Integer, Integer>>();
	}

	/**
	 *  Insert a new element into the queue with the
	 *  given priority.
	 *
	 *	@param priority priority of element to be inserted
	 *	@param element element to be inserted
	 *	<br><br>
	 *	<b>Preconditions:</b>
	 *	<ul>
	 *	<li> The element does not already appear in the priority queue.</li>
	 *	<li> The priority is non-negative.</li>
	 *	</ul>
	 *  
	 */
	public void push(int priority, int element) {
        if (this.location.containsKey(element)) {
			throw new IllegalStateException("The priority queue already contains the element: " + element);
        }
        if (priority < 0) {
            throw new IllegalStateException("The priority must be non-negative.");
        }

        // Tack element to the end of the heap and add to map
        Pair<Integer, Integer> p = new Pair<Integer,Integer>(priority, element);
        this.heap.add(p);
        this.location.put((int)p.element, this.heap.size()-1);
        
        //  Return if this is the root
        if (heap.size() == 1) {
            return;
        }
         
		percolateUp(this.location.get(element)); 
	}

	/**
	 *  Remove the highest priority element
	 *  <br><br>
	 *	<b>Preconditions:</b>
	 *	<ul>
	 *	<li> The priority queue is non-empty.</li>
	 *	</ul>
	 *  
	 */
	public void pop(){
        if (isEmpty()) {
			throw new IllegalStateException("The priority queue is empty, cannot pop an element.");
        } 

        // Swap root with last element and remove the old root from the heap and map
        int element = getElement(0);
        swap(0, this.heap.size()-1);
        this.location.remove(element);
        this.heap.remove(this.heap.size()-1);
        
		percolateDown(0); 
	}

	/**
	 *  Returns the highest priority in the queue
	 *  @return highest priority value
	 *  <br><br>
	 *	<b>Preconditions:</b>
	 *	<ul>
	 *	<li> The priority queue is non-empty.</li>
	 *	</ul>
	 */
	public int topPriority() {		
		if (isEmpty()) {
			throw new IllegalStateException("The priority queue is empty. No priority to return.");
		}
        return (int)heap.get(0).priority;
	}

	/**
	 *  Returns the element with the highest priority
	 *  @return element with highest priority
	 *  <br><br>
	 *	<b>Preconditions:</b>
	 *	<ul>
	 *	<li> The priority queue is non-empty.</li>
	 *	</ul>
	 */
	public int topElement() {
        if (isEmpty()) {
            throw new IllegalStateException("The priority queue is empty. No element to return.");
        }
        return (int)this.heap.get(0).element;
	}

	/**
	 *  Change the priority of an element already in the
	 *  priority queue.
	 *  
	 *  @param newpriority the new priority	  
	 *  @param element element whose priority is to be changed
	 *  <br><br>
	 *	<b>Preconditions:</b>
	 *	<ul>
	 *	<li> The element exists in the priority queue</li>
	 *	<li> The new priority is non-negative </li>
	 *	</ul>
	 */
	public void changePriority(int element, int newPriority) {
		//System.out.println("Changing Priority of element " + element + " to " + newPriority);
		if (!this.location.containsKey(element)) { 
			throw new IllegalStateException("The specified element does not exist in the priority queue.");
		}
		if (newPriority < 0) {
			throw new IllegalStateException("The new priority must be non-negative.");
		}
		
		int index = this.location.get(element);
		int oldPriority = getPriority(element);
		this.heap.get(index).priority = newPriority;

		if (newPriority > oldPriority) {
			//move the element down the tree
			percolateDown(index);
		} else if (newPriority < oldPriority) {
			//move the element up the tree
			percolateUp(index);
		}
		//System.out.println("Done");
	}

	/**
	 *  Gets the priority of the element
	 *  
	 *  @param element the element whose priority is returned
	 *  @return the priority value
	 *  <br><br>
	 *	<b>Preconditions:</b>
	 *	<ul>
	 *	<li> The element exists in the priority queue</li>
	 *	</ul>
	 */
	public int getPriority(int element) {
		if(!this.location.containsKey(element)){
			throw new IllegalStateException(element + " is not contained in the priority queue.");
		}
		
		int index = this.location.get(element);
		return getIndexPriority(index); 
	}

	/**
	 *  Returns true if the priority queue contains no elements
	 *  @return true if the queue contains no elements, false otherwise
	 */
	public boolean isEmpty() {
        return this.heap.size() == 0;
	}

	/**
	 *  Returns true if the element exists in the priority queue.
	 *  @return true if the element exists, false otherwise
	 */
	public boolean isPresent(int element) {
        return this.location.containsKey(element);
	}

	/**
	 *  Removes all elements from the priority queue
	 */
	public void clear() {
		this.location.clear();
        this.heap.clear();
	}

	/**
	 *  Returns the number of elements in the priority queue
	 *  @return number of elements in the priority queue
	 */
	public int size() {
        return this.heap.size();
	}

	/*********************************************************
	 * 				Private helper methods
	 *********************************************************/

	/**
	 * Push down the element at the given position in the heap 
	 * @param start_index the index of the element to be pushed down
	 * @return the index in the list where the element is finally stored
	 */
	private int pushDown(int start_index) {	
		int index = start_index;
		int priority = getIndexPriority(index);
		
		int leftChildPriority;
		int leftChildIndex;

		int rightChildPriority;
		int rightChildIndex;

		int[] children = getChildren(index);
		
        if (children.length == 1) {
            //one child case			
            leftChildIndex = children[0];
            leftChildPriority = getIndexPriority(leftChildIndex);

            if(priority > leftChildPriority) {
                swap(index, leftChildIndex);
                index = leftChildIndex;
                priority = leftChildPriority;
            } 
       } else if (children.length == 2) {
            //two child case
            leftChildIndex = children[0];
            rightChildIndex = children[1];

            leftChildPriority = getIndexPriority(leftChildIndex); 
            rightChildPriority = getIndexPriority(rightChildIndex); 
           
            //Only swap if priority is greate than atleast one child 
            if(priority > leftChildPriority || priority > rightChildPriority) {
                // Swap with smallest child
                if (leftChildPriority <= rightChildPriority) {
                    swap(index, leftChildIndex);
                    index = leftChildIndex;
                } else {
                    swap(index, rightChildIndex);
                    index = rightChildIndex;
                }
            }
        }
		
		return index;
	}

	/**
	 * Percolate up the element at the given position in the heap 
	 * @param start_index the index of the element to be percolated up
	 * @return the index in the list where the element is finally stored
	 */
	private int percolateUp(int start_index) {
		int parent; 
		int index = start_index;

		while(!isParentMoreImportant(index)){
			//System.out.println("Percolating up");
			parent = parent(index);
			swap(parent, index);
			
			index = parent;
			//System.out.println("Swapped up, new index is " + index);
		}
		return index;
	}
	
	/**
	 * Percolate down the element at the given position in the heap
	 * @param start_index the index of the element to be percolated up
	 * @return the index in the list where the element is finally stored
	 */
	private int percolateDown(int start_index) {
        int oldIndex; 
        int newIndex = start_index;

        do {
            oldIndex = newIndex;
            newIndex = pushDown(oldIndex);
           
        } while (oldIndex != newIndex);
        
        return oldIndex;
   }
	
	/**
	 * Swaps two elements in the priority queue by updating BOTH
	 * the list representing the heap AND the map
	 * @param i The index of the element to be swapped
	 * @param j The index of the element to be swapped
	 */
	private void swap(int i, int j) {
        int elementI = getElement(i); 
        int elementJ = getElement(j);

        Collections.swap(this.heap, i, j);
        this.location.put(elementI, j);
        this.location.put(elementJ, i);
	}

	/**
	 * Computes the index of the element's left child
	 * @param parent index of element in list
	 * @return index of element's left child in list
	 */
	private int left(int parent) {
        return 2*parent + 1; 
	}

	/**
	 * Computes the index of the element's right child
	 * @param parent index of element in list
	 * @return index of element's right child in list
	 */
	private int right(int parent) {
        return 2*parent + 2;
	}

	/**
	 * Computes the index of the element's parent
	 * @param child index of element in list
	 * @return index of element's parent in list
	 */
	private int parent(int child) {
        return (child-1)/2;
	}
	

	/*********************************************************
	 * 	These are optional private methods that may be useful
	 *********************************************************/
    /**
     * Returns the element at a given index.
     **/
    private int getElement(int index) {
        return (int)this.heap.get(index).element;
    }

	/**
	 * Returns the priority of the element at an index.
	 **/
	private int getIndexPriority(int index) {
		//System.out.println("Trying to get: " + index + "with size " + this.size());
		return (int)this.heap.get(index).priority;
	}

   /**
    * Returns the parent index of index.
    */
    private static int calcParentIndex(int index) {
        return (index - 1)/2;
    } 

    /**
     * Compares the priority of the element at index to that of it's parent. 
     * Returns true if the parent is more important, false if the child is more important.  
     **/
    private boolean isParentMoreImportant(int index) {
       // Return true if the index is the root and has no parents.
       if (index == 0) {
            return true; 
       }

       int parentIndex = calcParentIndex(index);
       Pair<Integer, Integer> parent = heap.get(parentIndex);
       Pair<Integer, Integer> child = heap.get(index);
       //System.out.println("Parent priority: " + parent.priority);
	   //System.out.println("Child priority: " + child.priority);
       
       if ((int)parent.priority <= (int)child.priority) {
           return true;
        }
       
       	//System.out.println("Parent is more important");
        return false;
    }
    
    /**
     * Returns an array containing the children indexes
     */ 
    private int[] getChildren(int index) {
        int leftChild = left(index);
        int rightChild = right(index);
         
        if (leftChild > this.heap.size()-1) {
            return new int[]{}; 
        } else if (rightChild > this.heap.size()-1) {
            return new int[]{leftChild};
        } else {
            return new int[]{leftChild, rightChild};
        }
    }
}
