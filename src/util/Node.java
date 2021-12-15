package util;

public class Node {
    boolean visited;
    int value;
    
    public Node(int value) {
        this.value = value;
        this.visited = false;
    }

    public int getValue() {
        return this.value;
    }

    public void visit() {
        this.visited = true;
    }

    public boolean isVisited() {
        return this.visited;
    }

    @Override
    public boolean equals(Object obj) {
        // Check for same type
        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return ((Node)obj).getValue() == this.getValue();
    }
}
