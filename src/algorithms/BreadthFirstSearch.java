package algorithms;

import graph.*;
import java.util.*;
import util.*;
import visualizer.*;

public class BreadthFirstSearch {

    public static void main(String[] args) {
        PathFindingVisualizer app = new PathFindingVisualizer();
        Graph<Node> graph = app.getGraph();
        Node s = app.getBoard()[1][1];

        Stack<Node> S = new Stack<Node>();
        S.push(s);

        while (!S.empty()) {
            Node u = S.pop();
            if(!u.isVisited()) {
                app.visitNode(u);
                for (Node n : graph.getNeighbors(u)) {
                    if(!n.isVisited()) {
                        S.push(n);
                    }
                }
            }
        }
    }
}
