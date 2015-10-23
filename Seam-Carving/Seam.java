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
	 * 		time (in ms) to wait after drawing a seam.
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
	 * 		the image.
	 **/
	public void verticalSeamShrink( UWECImage im );
	
	/**
	 * Shrinks a given image by removing a horizontal seam.
	 * 
	 * @param im
	 * 		the image.
	 **/
	public void horizontalSeamShrink( UWECImage im );
	
	/**
	 * Shrinks a given image by removing a given number
	 * of vertical seams.
	 * <p>
	 * Unlike verticalSeamShrink, this method is oriented
	 * towards resizing the image as quickly as possible,
	 * rather than showcasing the process.
	 * 
	 * @param im
	 * 		the image.
	 * @param DEC_WIDTH
	 * 		the number of vertical seams to remove.
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
	 * 		the image.
	 * @param DEC_HEIGHT
	 * 		the number of horizontal seams to remove.
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
	 * 		the given image.
	 * @return
	 * 		the energy map.
	 **/
	private int[][] getEnergy( final UWECImage IM );
	
	/**
	 * Creates an energy map for a given image based on a
	 * previous energy map and the removed seam.
	 * Energy is computed using a dual-gradient energy function.
	 * 
	 * @param IM
	 * 		the given image.
	 * @param LAST_ENERGY
	 * 		the previous energy map.
	 * @param LAST_SEAM
	 * 		the last seam removed.
	 * @return
	 * 		the energy map.
	 **/
	private int[][] getEnergy( final UWECImage IM, final int[][] LAST_ENERGY, final int[] LAST_SEAM );
		// Only called by fast methods.
	/**
	 * Creates a map of the minimum energy required to
	 * get to every pixel of a given energy map.
	 * 
	 * @param ENERGY
	 * 		the image's energy map.
	 * @return
	 * 		the path weight map.
	 **/
	private int[][] getPathWeights( final int[][] ENERGY );
	
	/**
	 * Creates a map of the minimum energy required to
	 * get to every pixel of a given image based on the
	 * previous path weight map and the removed seam.
	 * 
	 * @param ENERGY
	 * 		the image's energy map.
	 * @param LAST_PATHS
	 * 		the previous path weight map.
	 * @param LAST_SEAM
	 * 		the last seam removed.
	 * @return
	 * 		the path weight map.
	 **/
	private int[][] getPathWeights( final int[][] ENERGY, final int[][] LAST_PATHS, final int[] LAST_SEAM );
		// Only called by fast methods.
	/**
	 * Calculates the minimum path from the top
	 * to the bottom of a given image.
	 * 
	 * @param PATH_WEIGHTS
	 * 		the minimum energy required to get to every pixel.
	 * @return
	 * 		the minimum path.
	 **/
	private int[] getSeam( final int[][] PATH_WEIGHTS );
}
