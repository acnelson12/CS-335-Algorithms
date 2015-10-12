import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class QuickHull implements ConvexHullFinder
{
	public List<Point2D> computeHull( List<Point2D> points )
	{
		/* Local Variables */
		Point2D a = findLeftmostPoint( points );
		Point2D b = findRightmostPoint( points );
		
		List<Point2D> h1 = recursiveQuickHull( new Line2D.Double( a, b ),
		                                       points );
		List<Point2D> h2 = recursiveQuickHull( new Line2D.Double( b, a ),
		                                       points );
		
		/* Combine Lists */
		h2.remove(0);
		h1.addAll(h2);
		h1.remove( h1.size()-1 );
		return h1;
	}
	
	private List<Point2D> recursiveQuickHull( Line2D lineAB, List<Point2D> pointsAB )
	{
		/* Base Case */
		List<Point2D> outerPoints = getOutsidePoints( lineAB, pointsAB );
		if ( 0 == outerPoints.size() )
		{
			ArrayList<Point2D> hull = new ArrayList();
			hull.add( lineAB.getP1() );
			hull.add( lineAB.getP2() );
			return hull;
		}
		
		/* Recursive Case */
		Point2D c = getFarthestPoint( lineAB, outerPoints );
		List<Point2D> h1 = recursiveQuickHull( new Line2D.Double( lineAB.getP1(), c), outerPoints );
		List<Point2D> h2 = recursiveQuickHull( new Line2D.Double( c, lineAB.getP2() ), outerPoints );

		h2.remove(0);
		h1.addAll(h2);
		return h1;
	}
	
	private List<Point2D> getOutsidePoints( Line2D line, List<Point2D> points )
	{
		/* Local Variables */
		ArrayList<Point2D> outerPoints = new ArrayList();
		
		/* Generate List */
		for ( Point2D point : points )
			if ( line.relativeCCW( point ) > 0 )
				outerPoints.add( point );
		return outerPoints;
	}
	
	private Point2D getFarthestPoint( Line2D line, List<Point2D> points )
	{
		/* Local Variables */
		Point2D farthestPoint = points.get(0);
		
		/* Find Farthest Point */
		for ( Point2D point : points )
			if ( line.ptLineDist( point ) > line.ptLineDist( farthestPoint ) )
				farthestPoint = point;
		return farthestPoint;
	}
	
	private Point2D findLeftmostPoint( List<Point2D> points )
	{
		/* Local Variables */
		Point2D leftmostPoint = points.get(0);
		
		/* Find leftmost point */
		for ( Point2D point : points )
			if ( point.getX() < leftmostPoint.getX() )
				leftmostPoint = point;
		
		return leftmostPoint;
	}
	
	private Point2D findRightmostPoint( List<Point2D> points )
	{
		/* Local Variables */
		Point2D rightmostPoint = points.get(0);
		
		/* Find rightmost point */
		for ( Point2D point : points )
			if ( point.getX() > rightmostPoint.getX() )
				rightmostPoint = point;
		
		return rightmostPoint;
	}
}
