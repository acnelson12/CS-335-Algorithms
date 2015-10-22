/*
 * Class to carry out basic testing of RSA-related classes.
 */

import java.math.*;

public class RSADriver
{
	public static void main( String[] args )
	{
		/* Local Variables */
		String plainText1 = "Hello, World!";
		RSAKey testKey1 = new RSAKey();
		RSAEncryptor encryptTest1 = new RSAEncryptor( testKey1.findPQ(), testKey1.findE() );
		RSADecryptor decryptTest1 = new RSADecryptor( testKey1.findPQ(), testKey1.findD() );
		
		BigInteger[] cipherText2 = { new BigInteger( "576322461849" ),
									 new BigInteger( "122442824098" ),
									 new BigInteger(  "34359738368" ),
									 new BigInteger(  "29647771149" ),
									 new BigInteger( "140835578744" ),
									 new BigInteger( "546448062804" ),
									 new BigInteger( "120078454173" ),
									 new BigInteger(  "42618442977" ) };
		RSAKey testKey2 = RSAKey.crackRSA( new BigInteger( "608485549753" ) );
		RSAEncryptor encryptTest2 = new RSAEncryptor( testKey2.findPQ(), testKey2.findE() );
		RSADecryptor decryptTest2 = new RSADecryptor( testKey2.findPQ(), testKey2.findD() );
		
		/* Testing */
		System.out.println( "\n** Test 1 **\n" );
		System.out.println( testKey1 );
		System.out.println( encryptTest1.encryptToString( plainText1 ) );
		System.out.println( decryptTest1.decrypt( encryptTest1.encrypt( plainText1 ) ) );
		
		System.out.println( "\n** Test 2 **\n" );
		System.out.println( testKey2 );
		System.out.println( decryptTest2.decrypt( cipherText2 ) );
		System.out.println( encryptTest2.encryptToString( decryptTest2.decrypt( cipherText2 ) ) );
	}
}
