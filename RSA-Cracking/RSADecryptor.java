/*
 * Class to handle RSA decryption
 * 
 * It is only concerned with operations using the private key.
 */

import java.math.*;

public class RSADecryptor
{
	/* Instance Constants */
	private final BigInteger PQ;
	private final BigInteger D;
	
	/*== Constructors ==*/
	
	/**
	 * Constructs RSADecryptor object given pq and d.
	 * 
	 * @param PQ the modulus for the keys
	 * @param D the private decryption key
	 **/
	public RSADecryptor( final BigInteger PQ, final BigInteger D )
	{
		this.PQ = PQ;
		this.D  = D;
	}
	
	/*== Accessors ==*/
	
	/**
	 * Decrypts a BigInteger array
	 * 
	 * @param CIPHER_TEXT the array containing the cipher text as BigIntegers
	 * @return String of decrypted text
	 **/
	public String decrypt( final BigInteger[] CIPHER_TEXT )
	{
		/* Local Variables */
	    String plainText = "";
	    
	    /* Decrypt Text */
	    for ( int i = 0; i < CIPHER_TEXT.length; i++ )
	    {
	    	BigInteger c = CIPHER_TEXT[i];
	    	BigInteger m = c.modPow(D, PQ);
	    	plainText += (char)(m.intValue());
	    }
	    
	    return plainText;
	}
}
