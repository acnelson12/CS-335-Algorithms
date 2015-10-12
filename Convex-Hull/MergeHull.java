import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class MergeHull implements ConvexHullFinder
{
	class Point2DComparator implements Comparator<Point2D>
	{
		public int compare( Point2D o1, Point2D o2 )
		{
			if ( o1.getX() < o2.getX() )
				return -1;
			else
				if ( o1.getX() > o2.getX() )
					return 1;
				else
					if ( o1.getY() < o2.getY() )
						return -1;
					else
						if ( o1.getY() > o2.getY() )
							return 1;
						else
							return 0;
		}
	}
	
	public List<Point2D> computeHull( List<Point2D> points )
	{
		Collections.sort( points, new Point2DComparator() );
		
		/* Throw out the duplicates */
		for( int i = 0; i < points.size() - 1; i++ )
			if ( points.get(i).getX() == points.get(i+1).getX() )
				while ( points.get(i).getY() == points.get(i+1).getY() )
					points.remove( i+1 );
		
		/* Compute the convex hull */
		return recursiveMergeHull( points );
	}
	
	private List<Point2D> recursiveMergeHull( List<Point2D> points )
	{
		/* Base Case */
		if ( points.size() <= 2 )
			return points;
		
		/* Local Variables */
		List<Point2D> leftHalf  = points.subList( 0, points.size()/2 );
		List<Point2D> rightHalf = points.subList( points.size()/2, points.size() );
		
		/* Recurse */
		leftHalf  = recursiveMergeHull( leftHalf );
		rightHalf = recursiveMergeHull( rightHalf );
		
		return mergeHulls( leftHalf, rightHalf);
	}
	
	private List<Point2D> mergeHulls( List<Point2D> leftHalf, List<Point2D> rightHalf )
	{
		int[] seed = { findRightmostPointIndex( leftHalf ),
		               findLeftmostPointIndex( rightHalf ) };
		int[] lt = getTangent( leftHalf, rightHalf, seed[0], seed[1] );
		int[] ut = getTangent( rightHalf, leftHalf, seed[1], seed[0] );
		
		ArrayList<Point2D> mergedList = new ArrayList();
		int index = ut[1];
		
		while ( index != lt[0] )
		{
			mergedList.add( leftHalf.get(index) );
			index = getNext( leftHalf, index );
		}
		mergedList.add( leftHalf.get( lt[0] ) );
		
		index = lt[1];
		while ( index != ut[0] )
		{
			mergedList.add( rightHalf.get(index) );
			index = getNext( rightHalf, index );
		}
		mergedList.add( rightHalf.get( ut[0] ) );
		
		return mergedList;
	}
	
	private int[] getTangent( List<Point2D> leftHalf, List<Point2D> rightHalf, int a, int b )
	{
		int[] tan = {a,b};
		Line2D t = new Line2D.Double( leftHalf.get(tan[0]), rightHalf.get(tan[1]) );
		boolean repeat;
		
		do
		{
			repeat = false;
			
			while ( t.relativeCCW( leftHalf.get( getNext( leftHalf, tan[0] ) ) ) > 0 ||
			        t.relativeCCW( leftHalf.get( getPrev( leftHalf, tan[0] ) ) ) > 0 )
			{
				tan[0] = getPrev( leftHalf, tan[0] );
				
				repeat = true;
				t = new Line2D.Double( leftHalf.get(tan[0]), rightHalf.get(tan[1]) );
			}
			
			while ( t.relativeCCW( rightHalf.get( getNext( rightHalf, tan[1] ) ) ) > 0 ||
			        t.relativeCCW( rightHalf.get( getPrev( rightHalf, tan[1] ) ) ) > 0 )
			{
				tan[1] = getNext( rightHalf, tan[1] );
				repeat = true;
				t = new Line2D.Double( leftHalf.get(tan[0]), rightHalf.get(tan[1]) );
			}
		} while ( repeat );
		
		return tan;
	}
	
	private int getPrev( List<Point2D> list, int index )
	{
		index+=(list.size()-1);
		index%= list.size();
		return index;
	}
	
	private int getNext( List<Point2D> list, int index )
	{
		index++;
		index%= list.size();
		return index;
	}
	
	private int findLeftmostPointIndex( List<Point2D> points )
	{
		/* Local Variables */
		int index = 0;
		
		/* Find leftmost point */
		for ( int i = 1; i < points.size(); i++ )
			if ( points.get(i).getX() < points.get(index).getX() )
				index = i;
		
		return index;
	}
	
	private int findRightmostPointIndex( List<Point2D> points )
	{
		/* Local Variables */
		int index = 0;
		
		/* Find rightmost point */
		for ( int i = 1; i < points.size(); i++ )
			if ( points.get(i).getX() > points.get(index).getX() )
				index = i;
		
		return index;
	}
}
