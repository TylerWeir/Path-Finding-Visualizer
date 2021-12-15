package visualizer;

import java.awt.*;
import java.awt.event.*;
import graph.*;
import algorithms.*;
import util.*;

/**
 * 
 */

public class App extends Frame{

    CvApp appCanvas;

    // Default constructor for the application
    App(int n) {
        // Init up the graphical ui
        super("Path Finding Algorithm Visualizer");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        setSize(400, 300);
        appCanvas = new CvApp(10);
        add("Center", appCanvas);
        setCursor(Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR));
        setVisible(true);
        appCanvas.run(n);
    }
    public static void main(String[] args) {
         new App(10);
    }

}

class CvApp extends Canvas {
    int centerX, centerY;
    float pixelSize, rWidth = 10.0F, rHeight = 10.0F, xP = 1000000, yP;
    Node[][] board;

    // Default constructor
    CvApp(int n) {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                xP = fx(evt.getX()); 
                yP = fy(evt.getY());
                repaint();
            }
        });

        this.board = new Node[n][n];
        // Generate Board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.board[i][j] = new Node(i+j);
            }
        }
    }

    /**
     * 
     */
    public void run(int n) {

        //Generate the graph
        Graph<Node> graph = new Graph<Node>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph.addVertex(this.board[i][j]);

                if (i > 0) {
                    // Add edge to node above
                    graph.addEdge(this.board[i][j], this.board[i-1][j]);
                    graph.addEdge(this.board[i-1][j], this.board[i][j]);
                }

                if (j > 0) {
                    // Add edge to node behind
                    graph.addEdge(this.board[i][j], this.board[i][j-1]);
                    graph.addEdge(this.board[i][j-1], this.board[i][j]);
                }
            }
        }
        BreadthFirstSearch.bfs(graph, this.board[1][1]);
        repaint();
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
        int left = iX(-rWidth / 2);
        int right = iX(rWidth / 2);
        int bottom = iY(-rHeight / 2);
        int top = iY(rHeight / 2);
        int xMiddle = iX(0);
        int yMiddle = iY(0);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Set the color of the node
                if (this.board[i][j].isVisited()) {
                    g.setColor((Color.red));
                } else {
                    g.setColor(Color.lightGray);
                }
                g.fillRect(iX(-5f + rWidth / 10 *j), iY(5f - rHeight / 10 * i), (int)(rWidth / 10 / pixelSize), (int)(rWidth / 10 / pixelSize)); 
                g.setColor(Color.black);
                g.drawRect(iX(-5f + rWidth / 10 *j), iY(5f - rHeight / 10 * i), (int)(rWidth / 10 / pixelSize), (int)(rWidth / 10 / pixelSize)); 
            }
        }

        if (xP != 1000000) {
            g.drawString("Logical coordinates of selectedpoint: " + xP + " " + yP, 20, 100);
        }
    }
}
