package algorithms;

import graph.*;
import java.util.*;
import util.*;

public class BreadthFirstSearch {

    public static void bfs(Graph<Node> graph, Node s) {
        Stack<Node> S = new Stack<Node>();
        S.push(s);

        while (!S.empty()) {
            Node u = S.pop();
            if(!u.isVisited()) {
                u.visit();
                for (Node n : graph.getNeighbors(u)) {
                    if(!n.isVisited()) {
                        S.push(n);
                    }
                }
            }
        }
    }
}
