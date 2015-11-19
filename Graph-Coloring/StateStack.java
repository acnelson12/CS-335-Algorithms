import java.util.Stack;

public class StateStack extends Stack<State> implements Data
{
	public StateStack()
	{
		<State>super();
	}
	
	public boolean add( State element )
	{
		push( element );
		return true;
	}
}
