public class SeamDemo
{
	public static void main( String... args )
	{
		/* Local Variables */
		String input;
		UWECImage im;
		Seam s;
		int decWidth  = -1;
		int decHeight = -1;
		int wait      = -1;
		boolean fast  = false;
		String output = null;
		
		/* Get Parameters */
		if ( args[0].equals( "-h" ) )
			help();
		
		input = args[0];
		im = new UWECImage( input );
		
		for ( int i = 1; i < args.length; i++ )
		{
			switch ( args[i] )
			{
				case "-dim":
					decWidth  = im.getWidth()  - Integer.parseInt( args[++i] );
					decHeight = im.getHeight() - Integer.parseInt( args[++i] );
					break;
				case "-f":
					fast = true;
					break;
				case "-h":
					help();
				case "-o":
					output = args[++i];
					break;
				case "-s":
					decWidth  = Integer.parseInt( args[++i] );
					decHeight = Integer.parseInt( args[++i] );
					break;
				case "-w":
					wait = Integer.parseInt( args[++i] );
					break;
				default:
					System.out.println( "unknown option: " + args[i] + "\n" +
					                    "Run SeamDemo -h for help." );
					System.exit(1);
			}
		}
		
		if ( 0 > decWidth )
			decWidth  = im.getWidth()  / 4;
		if ( 0 > decHeight )
			decHeight = im.getHeight() / 4;
		if ( 0 > wait )
			s = new Seam();
		else
			s = new Seam( wait );
		
		/* Demonstrate Seam */
		im.openNewDisplayWindow();
		if ( fast )
		{
			s.fastVSShrink( im, decWidth );
			s.fastHSShrink( im, decHeight );
		}
		else
		{
			for ( int i = 0; i < decWidth; i++ )
				s.verticalSeamShrink( im );
			
			for ( int i = 0; i < decHeight; i++ )
				s.horizontalSeamShrink( im );
		}
		
		if ( null != output )
			im.write( output );
	}
	
	public static void help()
	{
		String output =
		"Usage: SeamDemo file [-options]\n" +
		"   Or\n" +
		"       SeamDemo -h\n" +
		"where options include:\n\t" +
		"-dim <width> <height>\n\t\t" +
		        "The desired new dimensions of the image (in pixels).\n\t\t" +
		        "Defaults are 75% of the original.\n\t" +
		"-f\n\t\t" +
				"Shrink image as fast as possible.\n\t" +
		"-h\n\t\t" +
		        "This help text.\n\t" +
		"-o <file>\n\t\t" +
		        "Write the new image to a file.\n\t" +
		"-s <width> <height>\n\t\t" +
		        "Shrink image by the given dimensions (in pixels).\n\t\t" +
		        "Default shrinks to 75% of the original.\n\t" +
		"-w <time>\n\t\t" +
		        "Set a wait time between each modification (in ms).\n\t";
		System.out.println( output );
		System.exit( 0 );
	}
}
