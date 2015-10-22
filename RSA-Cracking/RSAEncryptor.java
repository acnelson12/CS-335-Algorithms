/*
 * Class to handle RSA encryption
 * 
 * It is only concerned with operations using the public key.
 */

import java.math.*;

public class RSAEncryptor
{
	/* Instance Constants */
	private final BigInteger PQ;
	private final BigInteger E;
	
	/*== Constructors ==*/
	
	/**
	 * Constructs an RSAEncryptor given pq and e.
	 * 
	 * @param PQ the product of two prime numbers, p and q
	 * @param E an integer e such that 1 < e < φ(pq) and gcd(e, φ(pq)) = 1
	 **/
	public RSAEncryptor( final BigInteger PQ, final BigInteger E )
	{
		this.PQ = PQ;
		this.E  = E;
	}
	
	/*== Accessors ==*/
	
	/**
	 * Encrypts a String of text
	 * 
	 * @param PLAIN_TEXT the message to encrypt
	 * @return a BigInteger array containing the encrypted text
	 **/
	public BigInteger[] encrypt( final String PLAIN_TEXT )
	{
		/* Local Variables */
		BigInteger[] cipherMessage = new BigInteger[PLAIN_TEXT.length()];
		
		/* Encrypt Text */
		for ( int i = 0; i < PLAIN_TEXT.length(); i++ )
		{
			BigInteger m = new BigInteger( ( (int)PLAIN_TEXT.charAt(i) ) + "" );
			BigInteger c = m.modPow( E, PQ );
			cipherMessage[i] = c;
		}
		
		return cipherMessage;
	}
	
	/**
	 * Encrypts a String of text
	 * 
	 * @param PLAIN_TEXT the message to encrypt
	 * @return a String representation of a BigInteger array containing
	 *         the encrypted text
	 **/
	public String encryptToString( final String PLAIN_TEXT )
	{
		/* Local Variables */
		BigInteger[] cipherMessage = encrypt( PLAIN_TEXT );
		String       cipherText    = "";
		
		/* Encrypt Text */
		for ( int i = 0; i < cipherMessage.length; i++ )
			cipherText += cipherMessage[i].toString() + "\n";
		
		return cipherText;
	}
}
