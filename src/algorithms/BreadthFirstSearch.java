package algorithms;

import graph.*;
import java.util.*;
import util.*;
import visualizer.*;

public class BreadthFirstSearch {

    public static void bfs(Graph<Node> graph, Node s) {

    }

    public static void main(String[] args) {
        App app = new App(10);
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
