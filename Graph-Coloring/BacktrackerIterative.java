public class BacktrackerIterative<T extends Data>
{
	private int numberExpanded = 0;
	private Class<T> t;
	
	public BacktrackerIterative( Class<T> t )
	{
		this.t = t;
	}
	
	public State backtrack(State s)
	{
		State soln = null;

		int bestSolnCost = Integer.MAX_VALUE;
		
		try{
		T statesToProcess = t.newInstance();
		statesToProcess.add(s);

		while( !statesToProcess.empty() )
		{
			// Pop a feasible state from the list
			State currentState = statesToProcess.pop();
			numberExpanded++;

			if (currentState.isSolved())
			{
				if (currentState.getBound() < bestSolnCost)
				{
					bestSolnCost = currentState.getBound();
					soln = currentState;
				}
			}
			else
			{
				while((currentState.hasMoreChildren()) &&
            	      (currentState.getBound() < bestSolnCost))
            	{
					State child = currentState.nextChild();

					if (child.isFeasible() && (child.getBound() < bestSolnCost) )
						statesToProcess.add( child );
				}
			}
		}}
		catch ( InstantiationException e )
		{
			System.out.println( "Aw Snap!" );
			System.exit(1);
		}
		catch ( IllegalAccessException e )
		{
			System.out.println( "Aw Snap!" );
			System.exit(1);
		}
		return soln;
	}
	
	public int getNumberExpanded()
	{
		return numberExpanded;
	}
}
