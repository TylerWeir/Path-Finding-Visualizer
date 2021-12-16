package visualizer;

import java.awt.*;

/**
 * In full disclosure I did not write this class.  This solves the flickering problems with java.awt graphics.
 * 
 * Source: https://www.codeproject.com/articles/2136/double-buffer-in-standard-java-awt#:~:text=Double%20buffer%20in%20standard%20Java%20AWT%201%20Introduction.,Image.%20...%206%20Extending%20the%20new%20class.%20
 * 
 */
public class DoubleBuffer extends Canvas {

    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;

    public DoubleBuffer() {
        super();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        //    checks the buffersize with the current panelsize
        //    or initialises the image with the first paint
        if(bufferWidth!=getSize().width ||
        bufferHeight!=getSize().height ||
        bufferImage==null || bufferGraphics==null)
        resetBuffer();

        if(bufferGraphics!=null){
            //this clears the offscreen image, not the onscreen one
            bufferGraphics.clearRect(0,0,bufferWidth,bufferHeight);

            //calls the paintbuffer method with
            //the offscreen graphics as a param
            paintBuffer(bufferGraphics);

            //we finaly paint the offscreen image onto the onscreen image
            g.drawImage(bufferImage,0,0,this);
        }
    }

    private void resetBuffer(){
        // always keep track of the image size
        bufferWidth=getSize().width;
        bufferHeight=getSize().height;

        //    clean up the previous image
        if(bufferGraphics!=null){
            bufferGraphics.dispose();
            bufferGraphics=null;
        }
        if(bufferImage!=null){
            bufferImage.flush();
            bufferImage=null;
        }
        System.gc();

        //    create the new image with the size of the panel
        bufferImage=createImage(bufferWidth,bufferHeight);
        bufferGraphics=bufferImage.getGraphics();
    }

    public void paintBuffer(Graphics g){
    //in classes extended from this one, add something to paint here!
    //always remember, g is the offscreen graphics
    }

}
