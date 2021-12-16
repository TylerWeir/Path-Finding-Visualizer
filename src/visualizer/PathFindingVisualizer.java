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

    // Default constructor for the application
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
        System.out.println("[Option 4] A-Star");
        System.out.println();
        System.out.print("Chose an algorithm to visualize (1-4): ");
        Scanner userInput = new Scanner(System.in);

        pfv.appCanvas.runAlgorithm(userInput.nextInt());

        userInput.close();
    }
}

/**
 * This class is an extention of canvas.  It maps logical coordinates to device coordinates for 
 * easy and intuitive drawing.  This class will be used for drawing the n x n grid that the 
 * algorithm will traverse. Eventually it will handle the mouse input to add and remove nodes and such. 
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
                this.board[i][j] = new Node(i+j);
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
            case 4:
                astar();
                break;
            default:
                System.out.println("Invalid option. Exiting...");
                System.exit(0);
        }
    
    }
    
    /**
     * 
     * @param graph
     * @param s
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
     * 
     * @param graph
     * @param s
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
     * 
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
     * 
     */
    void astar() {

    }
    
    void visitNode(Node n) {
        n.visit();
        repaint();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initGraphics() {
        Dimension d = getSize();
        int maxX = d.width - 1;
        int maxY = d.height - 1;
        pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
        centerX = maxX / 2;
        centerY = maxY / 2;
    }

    int iX(float x) {
        return Math.round(centerX + x / pixelSize);
    }

    int iY(float y) {
        return Math.round(centerY - y / pixelSize);
    }

    float fx(int x) {
        return (x - centerX) * pixelSize;
    }

    float fy(int y) {
        return (centerY - y) * pixelSize;
    }

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
