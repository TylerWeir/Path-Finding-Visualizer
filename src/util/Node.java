package util;

/**
 * This class represents a node in a graph. The node may be set active 
 * or inactive.  The Node also as a boolean field to tell if the node has
 * been visited or discoverd.  
 * 
 * @author Tyler Weir
 */
public class Node {
    private boolean visited;
    private boolean isActive;
    
    /**
     * Default constructor.  Nodes will be initialized as active and unvisited. 
     */
    public Node() {
        this.visited = false;
        this.isActive = true;
    }

    /**
     * Returns the active state of the node.  True if the node is active,
     * false if the node is not active.
     * 
     * @return isActive True if the node is active, false otherwise.
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Toggles the state of the node from active to inactive and vise versa. 
     */
    public void toggleState() {
        this.isActive = !this.isActive;
    }

    /**
     * Marks the node as visited.
     */
    public void visit() {
        this.visited = true;
    }

    /**
     * Returns the visited state of the node.  
     * 
     * @return isVisited Returns true if the node has been visited, false if not. 
     */
    public boolean isVisited() {
        return this.visited;
    }
}
