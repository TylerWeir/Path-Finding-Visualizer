package util;

public class Node {
    private boolean visited;
    private boolean isActive;
    private int value;
    
    
    public Node(int value) {
        this.value = value;
        this.visited = false;
        this.isActive = true;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void toggleState() {
        this.isActive = !this.isActive;
    }

    public void visit() {
        this.visited = true;
    }

    public boolean isVisited() {
        return this.visited;
    }

    /*
    @Override
    public boolean equals(Object obj) {
        // Check for same type
        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return ((Node)obj).getValue() == this.getValue();
    }
    */
}
