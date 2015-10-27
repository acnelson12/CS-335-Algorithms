/**
 * Class to handle image resizing by seam-carving.
 **/

public class Seam
{
    /* Instance Constants */
    /**
     * The time to wait after drawing a seam (in milliseconds).
     **/
    private final int WAIT;
    
    /*== Constructors ==*/
    
    /**
     * Constructs a Seam with 0 for its wait time.
     **/
    public Seam()
    {
        this(0);
    }
    
    /**
     * Constructs a Seam with a given wait time.
     * 
     * @param WAIT
     *      time (in ms) to wait after drawing a seam.
     **/
    public Seam( final int WAIT )
    {
        this.WAIT = WAIT;
    }
    
    /*== Accessors ==*/
    
    /**
     * Shrinks a given image by removing a vertical seam.
     * 
     * @param im
     *      the image.
     **/
    public void verticalSeamShrink( UWECImage im )
    {
        /* Local Variables */
        int[][] energy      = getEnergy( im );
        int[][] pathWeights = getPathWeights( energy );
        int[]   seam        = getSeam( pathWeights );
        
        /* Draw Seam */
        for ( int j = 0; j < im.getHeight(); j++ )
            im.setRGB( seam[j], j, 255, 00, 00 );
        im.repaintCurrentDisplayWindow();
        try
        {
            Thread.sleep( WAIT );
        }
        catch ( Exception e )
        {}
        
        /* Remove Seam */
        im.switchImage( im.shrinkImage( seam ) );
        im.repaintCurrentDisplayWindow();
    }
    
    /**
     * Shrinks a given image by removing a horizontal seam.
     * 
     * @param im
     *      the image.
     **/
    public void horizontalSeamShrink( UWECImage im )
    {
        /* Local Variables */
        UWECImage imT       = im.transpose();
        int[][] energy      = getEnergy( imT );
        int[][] pathWeights = getPathWeights( energy );
        int[]   seam        = getSeam( pathWeights );
        
        /* Draw Seam */
        for ( int i = 0; i < im.getWidth(); i++ )
            im.setRGB( i, seam[i], 255, 00, 00 );
        im.repaintCurrentDisplayWindow();
        try
        {
            Thread.sleep( WAIT );
        }
        catch ( Exception e )
        {}
        
        /* Remove Seam */
        im.switchImage( imT.shrinkImage( seam ).transpose() );
        im.repaintCurrentDisplayWindow();
    }
    
    /**
     * Shrinks a given image by removing a given number
     * of vertical seams.
     * <p>
     * Unlike verticalSeamShrink, this method is oriented
     * towards resizing the image as quickly as possible,
     * rather than showcasing the process.
     * 
     * @param im
     *      the image.
     * @param DEC_WIDTH
     *      the number of vertical seams to remove.
     **/
    public void fastVSShrink( UWECImage im, final int DEC_WIDTH )
    {
        /* Local Variables */
        int[][] energy      = null;
        int[][] pathWeights = null;
        int[]   seam        = null;
        
        /* Run initial calculations */
        if ( 0 < DEC_WIDTH )
        {
            energy      = getEnergy( im );
            pathWeights = getPathWeights( energy );
            seam        = getSeam( pathWeights );
            
            /* Remove Seam */
            im.switchImage( im.shrinkImage( seam ) );
        }
        
        /* Run optimized calculations */
        for ( int i = 1; i < DEC_WIDTH; i++ )
        {
            energy      = getEnergy( im, energy, seam );
            pathWeights = getPathWeights( energy, pathWeights, seam );
            seam        = getSeam( pathWeights );
            
            /* Remove Seam */
            im.switchImage( im.shrinkImage( seam ) );
        }
        im.repaintCurrentDisplayWindow();
    }
    
    /**
     * Shrinks a given image by removing a given number
     * of horizontal seams.
     * <p>
     * Unlike horizontalSeamShrink, this method is oriented
     * towards resizing the image as quickly as possible,
     * rather than showcasing the process.
     * 
     * @param im
     *      the image.
     * @param DEC_HEIGHT
     *      the number of horizontal seams to remove.
     **/
    public void fastHSShrink( UWECImage im, final int DEC_HEIGHT )
    {
        /* Local Variables */
        UWECImage imT = im.transpose();
        int[][] energy      = null;
        int[][] pathWeights = null;
        int[]   seam        = null;
        
        /* Run initial calculations */
        if ( 0 < DEC_HEIGHT )
        {
            energy      = getEnergy( imT );
            pathWeights = getPathWeights( energy );
            seam        = getSeam( pathWeights );
            
            /* Remove Seam */
            imT.switchImage( imT.shrinkImage( seam ) );
        }
        
        /* Run optimized calculations */
        for ( int i = 1; i < DEC_HEIGHT; i++ )
        {
            energy      = getEnergy( imT, energy, seam );
            pathWeights = getPathWeights( energy, pathWeights, seam );
            seam        = getSeam( pathWeights );
            
            /* Remove Seam */
            imT.switchImage( imT.shrinkImage( seam ) );
        }
        im.switchImage( imT.transpose() );
        im.repaintCurrentDisplayWindow();
    }
    
    /**
     * Creates an energy map for a given image.
     * Energy is computed using a dual-gradient energy function.
     * 
     * @param IM
     *      the given image.
     * @return
     *      the energy map.
     **/
    private int[][] getEnergy( final UWECImage IM )
    {
        /* Local Constants */
        final int HEIGHT = IM.getHeight();
        final int WIDTH  = IM.getWidth();
        
        /* Local Variables */
        int[][] energy = new int[WIDTH][HEIGHT];
        
        /* Generate Energy Map */
        for ( int i = 0; i < WIDTH; i++ )
            for ( int j = 0; j < HEIGHT; j++ )
            {
                /* Block Variables */
                int deltaRed;
                int deltaGreen;
                int deltaBlue;
                
                int prevI = ( i + WIDTH - 1 ) % WIDTH;
                int nextI = ( i + 1 ) % WIDTH;
                
                /* Analyze Image */
                deltaRed   = IM.getRed(   prevI, j ) - IM.getRed(   nextI, j );
                deltaGreen = IM.getGreen( prevI, j ) - IM.getGreen( nextI, j );
                deltaBlue  = IM.getBlue(  prevI, j ) - IM.getBlue(  nextI, j );
                
                energy[i][j] = deltaRed   * deltaRed +
                               deltaGreen * deltaGreen +
                               deltaBlue  * deltaBlue;
            }
        return energy;
    }
    
    /**
     * Creates an energy map for a given image based on a
     * previous energy map and the removed seam.
     * Energy is computed using a dual-gradient energy function.
     * 
     * @param IM
     *      the given image.
     * @param LAST_ENERGY
     *      the previous energy map.
     * @param LAST_SEAM
     *      the last seam removed.
     * @return
     *      the energy map.
     **/
    private int[][] getEnergy( final UWECImage IM,
                               final int[][]   LAST_ENERGY,
                               final int[]     LAST_SEAM )
    {
        /* Local Constants */
        final int HEIGHT = IM.getHeight();
        final int WIDTH  = IM.getWidth();
        
        /* Local Variables */
        int[][] energy = new int[WIDTH][HEIGHT];
        
        /* Generate Energy Map */
        for ( int j = 0; j < HEIGHT; j++ )
        {
            /* Copy Values from Old Map */
            for ( int i = 0; i < LAST_SEAM[0]-j-1; i++ )
                energy[i][j] = LAST_ENERGY[i][j];
            
            for ( int i = LAST_SEAM[0]+j+1; i < WIDTH; i++ )
                energy[i][j] = LAST_ENERGY[i+1][j];
            
            /* Generate New Values */
            for ( int i = LAST_SEAM[0]-j-1; i <= LAST_SEAM[0]+j &&
                                            i < WIDTH; i++ )
            {
                if ( i < 0 )
                    i = 0;
                /* Block Variables */
                int deltaRed;
                int deltaGreen;
                int deltaBlue;
                
                int prevI = ( i + WIDTH - 1 ) % WIDTH;
                int nextI = ( i + 1 ) % WIDTH;
                
                /* Analyze Image */
                deltaRed   = IM.getRed(   prevI, j ) - IM.getRed(   nextI, j );
                deltaGreen = IM.getGreen( prevI, j ) - IM.getGreen( nextI, j );
                deltaBlue  = IM.getBlue(  prevI, j ) - IM.getBlue(  nextI, j );
                
                energy[i][j] = deltaRed   * deltaRed +
                               deltaGreen * deltaGreen +
                               deltaBlue  * deltaBlue;
            }
        }
        return energy;
    }
    
    /**
     * Creates a map of the minimum energy required to
     * get to every pixel of a given energy map.
     * 
     * @param ENERGY
     *      the image's energy map.
     * @return
     *      the path weight map.
     **/
    private int[][] getPathWeights( final int[][] ENERGY )
    {
        /* Local Constants */
        final int HEIGHT = ENERGY[0].length;
        final int WIDTH  = ENERGY.length;
        
        /* Local Variables */
        int[][] pathWeight = new int[WIDTH][HEIGHT];
        
        /* Calculate Minimum Energy Paths */
        for ( int i = 0; i < WIDTH; i++ )
            pathWeight[i][0] = ENERGY[i][0];
        
        for ( int j = 1; j < HEIGHT; j++ )
        {
            /*
             * Calculate Side Path Weights
             * (special case)
             */
            pathWeight[0][j]       = pathWeight[0][j-1] < pathWeight[1][j-1] ?
                                     pathWeight[0][j-1] : pathWeight[1][j-1];
            pathWeight[WIDTH-1][j] = pathWeight[WIDTH-1][j-1] <
                                     pathWeight[WIDTH-2][j-1] ?
                                     pathWeight[WIDTH-1][j-1] :
                                     pathWeight[WIDTH-2][j-1];
            pathWeight[0][j] += ENERGY[0][j];
            pathWeight[WIDTH-1][j] += ENERGY[WIDTH-1][j];
            
            /* Calculate Middle Path Weights */
            for ( int i = 1; i < WIDTH-1; i++ )
            {
                /* Block Variables */
                int weightNW = 0;
                int weightN  = 0;
                int weightNE = 0;
                
                /* Calculate Weights */
                weightN  = pathWeight[i][j-1];
                weightNW = pathWeight[i-1][j-1];
                weightNE = pathWeight[i+1][j-1];
                
                pathWeight[i][j] = weightN < weightNW ? weightN : weightNW;
                pathWeight[i][j] = pathWeight[i][j] > weightNE ?
                                   weightNE : pathWeight[i][j];
                pathWeight[i][j] += ENERGY[i][j];
            }
        }
        return pathWeight;
    }
    
    /**
     * Creates a map of the minimum energy required to
     * get to every pixel of a given image based on the
     * previous path weight map and the removed seam.
     * 
     * @param ENERGY
     *      the image's energy map.
     * @param LAST_PATHS
     *      the previous path weight map.
     * @param LAST_SEAM
     *      the last seam removed.
     * @return
     *      the path weight map.
     **/
    private int[][] getPathWeights( final int[][] ENERGY,
                                    final int[][] LAST_PATHS,
                                    final int[]   LAST_SEAM )
    {
        /* Local Constants */
        final int HEIGHT = ENERGY[0].length;
        final int WIDTH  = ENERGY.length;
        
        /* Local Variables */
        int[][] pathWeights = new int[WIDTH][HEIGHT];
        int start;
        int end;

        /* Calculate Minimum Energy Paths */
        for ( int j = 0; j < HEIGHT; j++ )
        {
            start = LAST_SEAM[0]-j;
            end = LAST_SEAM[0]+j;
            if ( end > WIDTH )
                end = WIDTH;
            if ( start < 0 )
                start = 0;
            
            /* Copy Values from Old Map */
            for ( int i = 0; i < start; i++ )
                pathWeights[i][j] = LAST_PATHS[i][j];
            
            for ( int i = end; i < WIDTH; i++ )
                pathWeights[i][j] = LAST_PATHS[i+1][j];
            
            /* Calculate New Values */
            for ( int i = start; i < end; i++ )
            {
                /* Block Variables */
                int weightNW = 0;
                int weightN  = 0;
                int weightNE = 0;
                
                /* Calculate Weights */
                weightN  = pathWeights[i][j-1];
                weightNW = i > 0 ? pathWeights[i-1][j-1] : Integer.MAX_VALUE;
                weightNE = i+1 < end ? pathWeights[i+1][j-1] :
                                       Integer.MAX_VALUE;
                
                pathWeights[i][j] = weightN < weightNW ? weightN : weightNW;
                pathWeights[i][j] = pathWeights[i][j] > weightNE ?
                                   weightNE : pathWeights[i][j];
                pathWeights[i][j] += ENERGY[i][j];
            }
        }
        return pathWeights;
    }
    
    /**
     * Calculates the minimum path from the top
     * to the bottom of a given image.
     * 
     * @param PATH_WEIGHTS
     *      the minimum energy required to get to every pixel.
     * @return
     *      the minimum path.
     **/
    private int[] getSeam( final int[][] PATH_WEIGHTS )
    {
        /* Local Constants */
        final int HEIGHT = PATH_WEIGHTS[0].length;
        final int WIDTH  = PATH_WEIGHTS.length;
        final int LAST   = HEIGHT-1;
        
        /* Local Variables */
        int[] seam = new int[HEIGHT];
        
        /* Find Minimum Path Energy for Bottom Pixel */
        seam[LAST] = 0;
        for ( int j = 0; j < WIDTH; j++ )
            if ( PATH_WEIGHTS[j][LAST] < PATH_WEIGHTS[seam[LAST]][LAST] )
                seam[LAST] = j;
        
        /* Trace Seam */
        for ( int i = seam.length-2; i >= 0; i-- )
        {
            seam[i] = seam[i+1] > 0 ? seam[i+1]-1 : seam[i+1];
            for ( int j = seam[i+1]; j <= seam[i+1]+1 && j < WIDTH; j++ )
                if ( PATH_WEIGHTS[j][i] < PATH_WEIGHTS[seam[i]][i] )
                    seam[i] = j;
        }
        return seam;
    }
}
