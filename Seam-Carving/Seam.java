//Implementation is secret for now.
public class Seam
{
	public  void      verticalSeamShrink( UWECImage im );
	
	public  void      horizontalSeamShrink( UWECImage im );
	
	private int[]     getSeam( UWECImage im, int[][] pathWeights );
	
	private int[][]   getEnergy( UWECImage im );
	
	private int[][]   getPathWeights( UWECImage im, int[][] energy );
	
	private UWECImage shrinkImage( UWECImage im, int[] seam );
}
