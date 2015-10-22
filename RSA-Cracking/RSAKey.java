/*
 * Class to encapsulate functionality of RSA key generation
 */

import java.util.*;
import java.math.*;
import java.io.*;
import java.security.SecureRandom;

public class RSAKey
{
	/* Class Constants */
	public static final int DEFAULT_KEY_LENGTH = 30;
	
	/* Instance Constants */
	private final BigInteger P;
	private final BigInteger Q;
	
	/*== Constructors ==*/
	
	/**
	 * Default Constructor
	 * <p>
	 * Constructs a random RSAKey with the default key length
	 **/
	public RSAKey()
	{		
		this( DEFAULT_KEY_LENGTH );
	}
	
	/**
	 * Constructs a random RSAKey with a given key length
	 * 
	 * @param KEY_LENGTH the length of the key
	 **/
	public RSAKey( final int KEY_LENGTH )
	{
		/* Local Variables */
		BigInteger   p = BigInteger.ZERO;
		BigInteger   q = BigInteger.ZERO;
		SecureRandom r = new SecureRandom();
		
		while ( p.equals(q) )
		{
			p = findPorQ( KEY_LENGTH, r );
			q = findPorQ( KEY_LENGTH, r );
		}
		
		this.P = p;
		this.Q = q;
	}
	
	/**
	 * Full Constructor
	 * 
	 * @param P the first prime number
	 * @param Q the second prime number
	 **/
	public RSAKey( final BigInteger P, final BigInteger Q )
	{
		this.P = new BigInteger( P.toString() );
		this.Q = new BigInteger( Q.toString() );
	}
	
	/*== Accessors ==*/
	
	/**
	 * Calculates a random prime number within the bound
	 * of a key length
	 * 
	 * @param KEY_LENGTH the intended length of the key
	 * @param r a random number generator
	 * @return a value for p or q
	 **/
	private BigInteger findPorQ( final int KEY_LENGTH, SecureRandom r )
	{
		return new BigInteger(KEY_LENGTH / 2, 100, r);
	}
	
	/**
	 * Calculates pq
	 * 
	 * @return p times q
	 **/
	public BigInteger findPQ()
	{
		return P.multiply(Q);
	}
	
	/**
	 * Calculates φ(pq)
	 * 
	 * @return (p-1)(q-1)
	 **/
	public BigInteger findPhiPQ()
	{
		return (P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE));
	}
	
	/**
	 * Calculates e
	 * 
	 * @return an integer e such that 1 < e < φ(pq) and gcd(e, φ(pq)) = 1
	 **/
	public BigInteger findE()
	{
		/* Local Variables */
		BigInteger e = new BigInteger("2");
		BigInteger phiPQ = findPhiPQ();
		
		while (phiPQ.gcd(e).compareTo(BigInteger.ONE) > 0)
		{
			e = e.add(BigInteger.ONE);
		}
		
		return e;
	}
	
	/**
	 * Calculates d
	 * 
	 * @return e⁻¹ mod( φ(pq) )
	 **/
	public BigInteger findD()
	{
		BigInteger e = findE();
		BigInteger d = e.modInverse( findPhiPQ() );
		
		return d;
	}
	
	@Override
	/**
	 * Overrides Object’s toString method
	 * 
	 * @return a String describing the state of the object
	 **/
	public String toString()
	{
		/* Local Variables */
		String output = "";
		
		/* Calculate Output */
		output += " p: " +        P.toString() + "\n";
		output += " q: " +        Q.toString() + "\n";
		output += "pq: " + findPQ().toString() + "\n";
		output += " e: " +  findE().toString() + "\n";
		output += " d: " +  findD().toString() + "\n";
		
		return output;
	}
	
	/*== Helpers ==*/
	
	/**
	 * Generates a new RSAKey by cracking pq
	 * 
	 * @param PQ the common component for the public and private keys, pq
	 * @return an RSAKey from cracking pq
	 **/
	public static RSAKey crackRSA( final BigInteger PQ )
	{
		BigInteger p = findLowestFactor( PQ );
		BigInteger q = PQ.divide( p );
		
		return new RSAKey( p, q );
	}
	
	/**
	 * Finds the lowest factor of a number
	 * <p>
	 * Meant for finding p from pq, it starts at the first
	 * prime number, then checks every odd number to see if it’s a factor.
	 * Since pq is two primes multiplied together, it will only have two
	 * factors (other than one and itself).
	 * <p>
	 * Preconditions: NUM is the product of two prime numbers
	 * 
	 * @param NUM this is pq
	 * @return the lowest of the the two prime factors (finding the second is
	 *         trivial)
	 **/
	public static BigInteger findLowestFactor( final BigInteger NUM )
	{
		final BigInteger TWO = new BigInteger( "2" );
		
		BigInteger factor = TWO;
		
		if ( !NUM.mod( factor ).equals( BigInteger.ZERO ) )
		{
			// Two is not a factor
			factor = factor.add( BigInteger.ONE ); // Make it odd
		
			while ( !NUM.mod( factor ).equals( BigInteger.ZERO ) )
			{
				// Only check odd numbers
				factor = factor.add( TWO );
			}
		}
		return factor;
	}
}
