import java.util.List;
import java.awt.geom.Point2D;
public interface ConvexHullFinder
{
	public List<Point2D> computeHull( List<Point2D> points );
}
