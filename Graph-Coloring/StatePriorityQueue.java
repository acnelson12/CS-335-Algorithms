import java.util.PriorityQueue;

public class StatePriorityQueue extends PriorityQueue<State> implements Data
{
	int nextIndex = 0;
	
	public StatePriorityQueue()
	{
		<State>super();
	}
	
	public State pop()
	{
		return poll();
	}
	
	public boolean empty()
	{
		return size() == 0;
	}
}
