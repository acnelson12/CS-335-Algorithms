import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;

public class UWECImage {
    private BufferedImage im;
    private ImagePanel theDisplay;
    
    // Nested class for display
    private class ImagePanel extends JPanel {
        private static final long serialVersionUID = 1L;
        UWECImage im;
        JFrame frame;

        public ImagePanel(UWECImage im) {
            this.im = im;  // Reference to the image - not a copy
                           // So if you call repaint() on this class it will redraw the updated pixels

            this.setSize(im.getWidth(), im.getHeight());

            // Setup the frame that I belong in
            frame = new JFrame("Image Viewer");
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(1);
                }
            });
            frame.getContentPane().add(this);
            
            // Set a fake size for now to get it up - we will never see this show up in these dimensions
            frame.setSize(im.getWidth()+10, im.getHeight()+10);
            frame.setVisible(true);
            
            // Get the insets now that it is visible
            // The insets will be 0 until it is visible
            Insets fInsets = frame.getInsets();
            
            // Resize the image based on the visible insets
            frame.setSize(im.getWidth() + fInsets.left + fInsets.right, im.getHeight() + fInsets.top + fInsets.bottom);
        }
        
        // Resets the image being displayed and redoes the frame to fit it
        public void changeImage(UWECImage im) {
            this.im = im;
            
            Insets fInsets = frame.getInsets();
            
            // Resize the image based on the visible insets
            frame.setSize(im.getWidth() + fInsets.left + fInsets.right, im.getHeight() + fInsets.top + fInsets.bottom);
            
            this.repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            im.draw(g);
        }
    }

    // Make an image from the given filename
    public UWECImage(String filename) {
        File f = new File(filename);

        try {
            this.im = ImageIO.read(f);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Make a blank image given the size
    public UWECImage(int x, int y) {
        this.im = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

        // Create a graphics context for the new BufferedImage
        Graphics2D g2 = this.im.createGraphics();

        // Fill in the image with black
        g2.setColor(Color.black);
        g2.fillRect(0, 0, x, y);
    }

    public int getWidth() {
        return im.getWidth();
    }

    public int getHeight() {
        return im.getHeight();
    }

    public int getRed(int x, int y) {
        int value = 0;

        if ((x >= 0) && (y >= 0) && (x < im.getWidth()) && (y < im.getHeight())) {
            value = ((im.getRGB(x, y) >> 16) & 255);
        }

        return value;
    }

    public int getGreen(int x, int y) {
        int value = 0;

        if ((x >= 0) && (y >= 0) && (x < im.getWidth()) && (y < im.getHeight())) {
            value = ((im.getRGB(x, y) >> 8) & 255);
        }

        return value;
    }

    public int getBlue(int x, int y) {
        int value = 0;

        if ((x >= 0) && (y >= 0) && (x < im.getWidth()) && (y < im.getHeight())) {
            value = ((im.getRGB(x, y) >> 0) & 255);
        }

        return value;
    }

    public void setRGB(int x, int y, int red, int green, int blue) {
        int pixel = (red << 16) + (green << 8) + (blue);
        im.setRGB(x, y, pixel);
    }

    public void draw(Graphics g) {
        g.drawImage(im, 0, 0, null);
    }

    public void write( String filename )
    {
        int formatIndex = filename.lastIndexOf( '.' ) + 1;
        String format = filename.substring( formatIndex );
        File f = new File( filename );
        boolean success;
        try
        {
            success = ImageIO.write( this.im, format, f );
            if ( !success )
            {
                Object[] possibilities = { "bmp", "gif", "jpg", "png" };
                format = (String)JOptionPane.showInputDialog(
                                    null,
                                    "\"" + format + "\"" +
                                    " is not a supported filetype.\n"
                                    + "Please choose an option below:",
                                    "Invalid Format",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    possibilities,
                                    "png" );

                if ( ( format != null ) && ( 0 < format.length() ) )
                {
                    if ( 0 == formatIndex )
                        filename = filename + "." + format;
                    else
                        filename = filename.substring( 0, formatIndex ) +
                                   format;
                    f = new File( filename );
                    ImageIO.write( this.im, format, f );
                }
                else
                    System.out.println( "No output written." );
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public UWECImage transpose()
    {
        /* Local Constants */
        final int HEIGHT = getHeight();
        final int WIDTH  = getWidth();
        
        /* Local Variables */
        UWECImage imT = new UWECImage( HEIGHT, WIDTH );
        
        /* Transpose Image */
        for ( int j = 0; j < HEIGHT; j++ )
            for ( int i = 0; i < WIDTH; i++ )
                imT.im.setRGB( j, i, im.getRGB(i,j) );
        
        return imT;
    }

    public UWECImage transposeR()
    {
        UWECImage imT = new UWECImage( getHeight(), getWidth() );
        transposeR( imT, 0, getWidth(), 0, getHeight() );
        return imT;
    }
    
    private void transposeR( UWECImage imT,
                             int mStart, int mEnd,
                             int nStart, int nEnd )
    {
        int m = mEnd - mStart; // cols
        int n = nEnd - nStart; // rows
        
        if ( 64 >= m && 64 >= n )
            for ( int i = mStart; i < mEnd; i++ )
                for ( int j = nStart; j < nEnd; j++ )
                    imT.im.setRGB( j, i, im.getRGB(i,j) );
        else
            if ( m > n )
            {
                int k = m/2;
                int mMid = mStart + k;
                transposeR( imT, mStart, mMid, nStart, nEnd );
                transposeR( imT, mMid,   mEnd, nStart, nEnd );
            }
            else
            {
                int k = n/2;
                int nMid = nStart + k;
                transposeR( imT, mStart, mEnd, nStart, nMid );
                transposeR( imT, mStart, mEnd, nMid,   nEnd );
            }
    }
    
    public UWECImage shrinkImage( int[] seam )
    {
        UWECImage im2 = new UWECImage( getWidth()-1, getHeight() );
        
        for ( int j = 0; j < getHeight(); j++ )
        {
            for ( int i = 0; i < seam[j]; i++ )
                im2.im.setRGB( i, j, im.getRGB(i,j) );
            for ( int i = seam[j]+1; i < getWidth(); i++ )
                im2.im.setRGB( i-1, j, im.getRGB(i,j) );
        }
        return im2;
    }
    
    // To pop up a new window for this image
    public void openNewDisplayWindow() {
        this.theDisplay = new ImagePanel(this);
    }
    
    // To mutate the existing window to fit the image's current dimensions and pixels
    public void repaintCurrentDisplayWindow() {
        this.theDisplay.changeImage(this);
        this.theDisplay.repaint();      
    }
    
    public void switchImage(UWECImage theNewImage) {
        this.im = theNewImage.im;
    }
}
