//Implementation is secret for now.
public class Seam
{
	public  void verticalSeamShrink( UWECImage im );
	
	public  void horizontalSeamShrink( UWECImage im );
	
	/**
	 * Fast Vertical Seam Shrink.
	 * Shrinks the image by removing vertical seams and attempts
	 * to maximize speed and efficiency rather than showcasing the
	 * process. (not required by specs)
	 * 
	 * @param im
	 * 	the image to operate on.
	 * @param decWidth
	 * 	the amount to decrease the width.
	 **/
	public void fastVSShrink( UWECImage im, int decWidth )
	{
		int[][] energy      = null;
		int[][] pathWeights = null;
		int[]   seam        = null;
		
		if ( 0 < decWidth )
		{
			energy      = getEnergy( im );
			pathWeights = getPathWeights( im, energy );
			seam        = getSeam( im, pathWeights );
			im.switchImage( im.shrinkImage( seam ) );
		}
		
		for ( int i = 1; i < decWidth; i++ )
		{
			energy      = getEnergy( im, energy, seam );
			pathWeights = getPathWeights( im, energy, pathWeights, seam );
			seam        = getSeam( im, pathWeights );
			im.switchImage( im.shrinkImage( seam ) );
		}
		im.repaintCurrentDisplayWindow();
	}
	
	/**
	 * Fast Horizontal Seam Shrink.
	 * Shrinks the image by removing horizontal seams and attempts
	 * to maximize speed and efficiency rather than showcasing the
	 * process. (not required by specs)
	 * 
	 * @param im
	 * 	the image to operate on.
	 * @param decHeight
	 * 	the amount to decrease the height.
	 **/
	public void     fastHSShrink( UWECImage im, int decHeight )
	{
		UWECImage imT = im.transpose();
		int[][] energy      = null;
		int[][] pathWeights = null;
		int[]   seam        = null;
		
		if ( 0 < decHeight )
		{
			energy = getEnergy( imT );
			pathWeights = getPathWeights( imT, energy );
			seam = getSeam( imT, pathWeights );
			imT.switchImage( imT.shrinkImage( seam ) );
		}
		
		for ( int i = 1; i < decHeight; i++ )
		{
			energy = getEnergy( imT, energy, seam );
			pathWeights = getPathWeights( imT, energy, pathWeights, seam );
			seam = getSeam( imT, pathWeights );
			imT.switchImage( imT.shrinkImage( seam ) );
		}
		im.switchImage( imT.transpose() );
		im.repaintCurrentDisplayWindow();
	}
	
	private int[]   getSeam( UWECImage im, int[][] pathWeights );
	
	private int[][] getEnergy( UWECImage im );
	
	private int[][] getEnergy( UWECImage im, int[][] lastEnergy, int[] lastSeam );
		// only called by fast methods
	
	private int[][] getPathWeights( UWECImage im, int[][] energy );
	
	private int[][] getPathWeights( UWECImage im, int[][] energy, int[][] lastPaths, int[] lastSeam );
		// only called by fast methods
}
