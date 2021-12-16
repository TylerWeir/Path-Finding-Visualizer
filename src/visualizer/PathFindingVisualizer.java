package visualizer;

import java.awt.*;
import java.awt.event.*;
import graph.*;
import util.*;

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
        setSize(400, 300);
        add("Center", new CvApp());
        setCursor(Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR));
        setVisible(true);
    }

    public static void main(String[] args) {
        new PathFindingVisualizer();
    }

    /**
     * Returns a Node[][]
     * @return Node[][]
     */
    public Node[][] getBoard() {
        return this.appCanvas.board;
    }

    public Graph<Node> getGraph() {
        Node[][] board = this.getBoard();

        //Generate the graph
        Graph<Node> graph = new Graph<Node>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                graph.addVertex(board[i][j]);

                if (i > 0) {
                    // Add edge to node above
                    graph.addEdge(board[i][j], board[i-1][j]);
                    graph.addEdge(board[i-1][j], board[i][j]);
                }

                if (j > 0) {
                    // Add edge to node behind
                    graph.addEdge(board[i][j], board[i][j-1]);
                    graph.addEdge(board[i][j-1], board[i][j]);
                }
            }
        } 

        return graph;
    }

    public void visitNode(Node node) {
        node.visit();
        System.out.println("visited a node");
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.appCanvas.repaint();
    }
}

/**
 * This class is an extention of canvas.  It maps logical coordinates to device coordinates for 
 * easy and intuitive drawing.  This class will be used for drawing the n x n grid that the 
 * algorithm will traverse. Eventually it will handle the mouse input to add and remove nodes and such. 
 */
class CvApp extends Canvas {
    int centerX, centerY;
    float pixelSize, rWidth = 10.0F, rHeight = 10.0F;
    Node[][] board;
    Node starterNode;
    int gridSize = 20;
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

    void ProgramLoop() {
        
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

    public void paint(Graphics g) {
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
