package visualizer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import graph.*;
import util.*;
import util.PriorityQueue;

/**
 *  This class creates the window frame that holds the application UI.
 *  It was created using 'Computer Graphics for Java Programmers' by L.Ammeral and K.Zhang.
 * 
 *  @author Tyler Weir
 */
public class PathFindingVisualizer extends Frame{

    public CvApp appCanvas;

    /**
     * Defualt constructor.
     */
    public PathFindingVisualizer() {
        // Init up the graphical ui
        super("Path Finding Algorithm Visualizer");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        setSize(500, 500);
        setResizable(true);
        this.appCanvas = new CvApp();
        add("Center", this.appCanvas);
        setCursor(Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR));
        setVisible(true);
    }

    public static void main(String[] args) {
        PathFindingVisualizer pfv = new PathFindingVisualizer();
        System.out.println("========== Welcome to Pathfinding Visualizer ==========");
        System.out.println(" - left mouse click to toggle squares");
        System.out.println(" - right mouse click to set starting square");
        System.out.println();
        System.out.println("[Option 1] Depth First Search");
        System.out.println("[Option 2] Breadth First Search");
        System.out.println("[Option 3] Dijkstra's Algorithm");
        System.out.println();
        System.out.print("Chose an algorithm to visualize (1-3): ");
        Scanner userInput = new Scanner(System.in);

        pfv.appCanvas.runAlgorithm(userInput.nextInt());

        userInput.close();
    }
}

/**
 * This class is an extention of Canvas. It maps logical coordinates to device coordinates for 
 * easy and intuitive drawing. This class displays an n x n grid and then allows 
 * a path finding algorithm to explore the grid as though each square were vertex 
 * in a graph. Each square is connected to its adjacent squares. Squares may be turned 
 * off and on by clicking on them. The green square is the starting position of the algorithms. 
 * The greeen square position may be set by right clicking. 
 * 
 * @author Tyler Weir
 */
class CvApp extends DoubleBuffer {
    int centerX, centerY;
    float pixelSize, rWidth = 10.0F, rHeight = 10.0F;
    Node[][] board;
    Node starterNode;
    int gridSize = 40;
    boolean isRunning;

    // Default constructor
    CvApp() {
        this.isRunning = false;

        // Generate Board
        this.board = new Node[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.board[i][j] = new Node();
            }
        }

        this.starterNode = this.board[0][0];

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                // If the simulation is not running then change the state of the clicked square
                if(!isRunning) {
                    float xP = fx(evt.getX()); 
                    float yP = fy(evt.getY());
                    
                    float rectSize = 10F/gridSize;

                    // Calculate which square was hit
                    int i = (int)Math.floor((double)(5f - yP)/rectSize);
                    int j = (-1) * (int)Math.floor((double)(-5f - xP)/rectSize) - 1;

                    if (i >= 0 && i < gridSize && j >= 0 && j < gridSize) {
                        if (evt.getButton() == MouseEvent.BUTTON1){
                            if (board[i][j] != starterNode) {
                                board[i][j].toggleState();
                            }
                        }
                        if (evt.getButton() == MouseEvent.BUTTON3){
                            if(board[i][j].isActive()) {
                                starterNode = board[i][j];
                            }
                        }
                    }
                    repaint();
                }
            }
        });
    }

    /**
     * Builds a graph from the state of the squares in the grid. Black 
     * squares are considered inactive and will not have a vertex represenation.
     * 
     * @return A graph of nodes.
     */
    Graph<Node> buildGraph() {
        Graph<Node> graph = new Graph<Node>();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {

                if (board[i][j].isActive()) {
                    graph.addVertex(board[i][j]);

                    if (i > 0 && board[i-1][j].isActive()) {
                        // Add edge to node above
                        graph.addEdge(board[i][j], board[i-1][j]);
                        graph.addEdge(board[i-1][j], board[i][j]);
                    }

                    if (j > 0 && board[i][j-1].isActive()) {
                        // Add edge to node behind
                        graph.addEdge(board[i][j], board[i][j-1]);
                        graph.addEdge(board[i][j-1], board[i][j]);
                    }
                }
            }
        }

        return graph;
    }


    /**
     * This function starts the specified algorithm at the starter node.
     * 
     * @param n The indicated algorithm. 1 = dfs, 2 = bfs, 3 = dijkstra's
     */
    void runAlgorithm(int n) {
        // This blocks any more mouse input.
        this.isRunning = true;

        // Build the graph from the board
        Graph<Node> graph = buildGraph(); 

        // release the algorithm!
        switch(n) {
            case 1:
                // DFS
                dfs(graph, starterNode);
                break;
            case 2:
                bfs(graph, starterNode);
                break;
            case 3:
                dijkstra(graph, starterNode);
                break;
            default:
                System.out.println("Invalid option. Exiting...");
                System.exit(0);
        }
    
    }
    
    /**
     * An implementation of the Depth First Search graph traversal algorithm.
     * 
     * @param graph The graph the algorithm will explore
     * @param s The node from which the algoithm will start exploring
     */
    void dfs(Graph<Node> graph, Node s) {
        Stack<Node> S = new Stack<Node>();
        S.push(s);

        while (!S.empty()) {
            Node u = S.pop();
            if(!u.isVisited()) {
                visitNode(u);
                for (Node n : graph.getNeighbors(u)) {
                    if(!n.isVisited()) {
                        S.push(n);
                    }
                }
            }
        }

    }

    /**
     * An implementation of the Breadth First Search graph traversal algorithm.
     * 
     * @param graph The graph the algorithm will explore
     * @param s The node from which the algorithm will start exploring
     */
    void bfs(Graph<Node> graph, Node s) {
        Queue<Node> Q = new LinkedList<Node>();
        Q.add(s);

        while(!Q.isEmpty()) {
            Node u = Q.remove();

            for (Node v : graph.getNeighbors(u)) {
                if (!v.isVisited()) {
                    Q.add(v);
                    visitNode(v);
                }
            }
        }
    } 

    /**
     * An implementation of Dijkstra's Algorithm. Note that the edge weights are all 
     * equal to 1.
     * 
     * @param graph The graph to traverse.
     * @param s The node from which the algoithm will start exploring.
     */
    void dijkstra(Graph<Node> graph, Node s) {

        // A minimum priority queue
        PriorityQueue<Node> Q = new PriorityQueue<Node>();

        Map<Node, Integer> dist = new HashMap<Node, Integer>();
        Map<Node, Node> prev = new HashMap<Node, Node>();

        // Initialize distances to infinity and source to zero
        for(Node n : graph.getVertices()) {
            dist.put(n, 99999999);
        }
        dist.put(s, 0);

        // Push all verticies onto the queue with distance as priority
        for(Node n : graph.getVertices()) {
            Q.push(dist.get(n), n);
        }

        while(Q.size() > 1) {
            Node u = Q.topElement();
            Q.pop();

            // Iterate over the neighbors
            for(Node n : graph.getNeighbors(u)) {
                visitNode(n);
                int alt = dist.get(u) + 1; // 1 can be replaced by edge weight

                if (alt < dist.get(n)) {
                    dist.put(n, alt);
                    prev.put(n, u);
                    Q.changePriority(n, alt);
                }
            }
        }
    }


    /**
     * This funciton is used to mark a square as visited on the GUI.  It must
     * be called by the graph traversal algorithm to see a graphical output of
     * the behavior of the output. 
     * 
     * @param n The node that was visited.
     */
    void visitNode(Node n) {
        n.visit();
        repaint();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This funciton calculates graphical variables for easy drawing no matter
     * the window size. 
     */
    void initGraphics() {
        Dimension d = getSize();
        int maxX = d.width - 1;
        int maxY = d.height - 1;
        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
        centerX = maxX / 2;
        centerY = maxY / 2;
    }

     /**
     * Maps a logical float coordinate to the integer device coordinate.
     * @param x
     * @return The device integer coordinate.
     */
   int iX(float x) {
        return Math.round(centerX + x / pixelSize);
    }
    
     /**
     *  Maps the logical flaot coordinate to the integer device coordinate. 
     * @param y
     * @return Th device integer coordinate.
     */
   int iY(float y) {
        return Math.round(centerY - y / pixelSize);
    }

     /**
     * Maps an integer device coordinate to a logical float coordinate
     * @param x
     * @return The logical float value. 
     */
   float fx(int x) {
        return (x - centerX) * pixelSize;
    }

     /**
     * Maps an integer device coordinate to a logical float coordinate
     * @param y
     * @return The Logical float value.
     */
   float fy(int y) {
        return (centerY - y) * pixelSize;
    }
    
    /**
     * Overrides the Canvas paint funciton.  Paints the grid and the state
     * of nodes are represented by their color.  
     */
    public void paintBuffer(Graphics g) {
        initGraphics();
        // int left = iX(-rWidth / 2);
        //int right = iX(rWidth / 2);
        //int bottom = iY(-rHeight / 2);
        //int top = iY(rHeight / 2);
        //int xMiddle = iX(0);
        //int yMiddle = iY(0);

        int rectSize =  (int)(rWidth / gridSize / pixelSize);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                // Set the color of the node
                if (this.board[i][j].isVisited()) {
                    g.setColor((Color.red));
                } else if (!this.board[i][j].isActive()) {
                    g.setColor(Color.BLACK);
                } else if (this.board[i][j] == this.starterNode) {
                    g.setColor(Color.green);
                }else {
                    g.setColor(Color.lightGray);
                }
                g.fillRect(iX(-5f + rWidth / gridSize *j), iY(5f - rHeight / gridSize * i), rectSize, rectSize); 
                g.setColor(Color.black);
                g.drawRect(iX(-5f + rWidth / gridSize *j), iY(5f - rHeight / gridSize * i), rectSize, rectSize); 
            }
        }
    }
}
