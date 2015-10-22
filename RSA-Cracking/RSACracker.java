/*
 * Class to rapidly test cracking of different sized RSA keys.
 * 
 * In the interest of good science, multi-threading was used to
 * run multiple simultaneous time trials.
 * 
 * It is interesting to note that a single-threaded approach to
 * cracking a single key runs as much as 75% faster than the
 * multi-threaded approach to cracking multiple keys; however,
 * if more than four keys will be cracked simultaneously, the
 * multi-threaded approach will return results faster.
 * 
 * To simplify plotting of the data, the results are written to
 * a text file.
 */

import java.util.*;
import java.math.*;
import java.io.*;

public class RSACracker extends Thread
{
	/* Instance Constants */
	private final int KEY_LENGTH;
	
	/* Instance Variables */
	private long elapsedTime;
	
	/*== Constructors ==*/
	
	/**
	 * Constructs an RSACracker to test cracking of keys with a
	 * given length
	 * 
	 * @param KEY_LENGTH the length of the key
	 **/
	public RSACracker( final int KEY_LENGTH )
	{
		this.KEY_LENGTH = KEY_LENGTH;
		elapsedTime = 0;
	}
	
	/*== Mutators ==*/
	
	/**
	 * Times the cracking of RSA keys
	 **/
	public void run()
	{
		/* Local Variables */
		RSAKey     key       = new RSAKey( KEY_LENGTH );
		RSAKey     crack     = null;
		BigInteger pq        = key.findPQ();
		BigInteger d         = BigInteger.ZERO;
		long       startTime = 0;
		long       endTime   = 0;
		
		/* Start timer */
		startTime = System.currentTimeMillis();
		
		/* Crack private key */
		crack = RSAKey.crackRSA( pq );
	
		/* Find D */
		d = crack.findD();
		
		/* End timer */
		endTime = System.currentTimeMillis();
		
		elapsedTime = endTime - startTime;
	}
	
	/*== Accessors ==*/
	
	/**
	 * Overrides Object’s toString method
	 * 
	 * @return a String describing the state of the object
	 **/
	@Override
	public String toString()
	{
		/*== Local Variables ==*/
		String output = "";
		
		/* Generate Output */
		output = Integer.toString( KEY_LENGTH ) + '\t' +
		         Long.toString( elapsedTime ) + '\n';
		
		return output;
	}
	
	/*== Driver ==*/
	public static void main( String[] args )
	{
		/* Local Constants */
		final int MAX_KEY_LENGTH    = 50;
		final int NUMBER_OF_THREADS =  8;
		
		/* Local Variables */
		long startTime = 0;
		long endTime   = 0;
		
		try
		{
			File file = new File( "./output.txt" );
			if ( !file.exists() )
				file.createNewFile();
			
			FileWriter fw = new FileWriter( file.getAbsoluteFile() );
			BufferedWriter bw = new BufferedWriter( fw );
			
			/* Test cracking speed with keys
			 * of different lengths
			 */
			for ( int i = 14; i <= MAX_KEY_LENGTH; i++ )
			{
				System.out.println( i );
				RSACracker[] test = new RSACracker[NUMBER_OF_THREADS];
				
				/* Initiate the threads */
				for ( int j = 0; j < test.length; j++ )
				{
					test[j] = new RSACracker( i );
					test[j].start();
				}
				
				/* Join the threads and record results */
				for ( int j = 0; j < test.length; j++ )
				{
					test[j].join();
					bw.write( test[j].toString() );
				}
			}
			bw.close();
		}
		catch ( Exception ex )
		{
			System.out.println( ex );
		}
	}
}
