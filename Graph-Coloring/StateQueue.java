import java.util.Queue;
import java.util.Vector;

public class StateQueue extends Vector<State> implements Data
{
	public StateQueue()
	{
		<State>super();
	}
	
	public boolean add( State element )
	{
		addElement( element );
		return true;
	}
	
	public State pop()
	{
		return remove(0);
	}
	
	public boolean empty()
	{
		return isEmpty();
	}
}
