import java.util.List;

public class Main
{
	static class Type extends StatePriorityQueue
		implements Data {}

	public static void main(String[] args)
	{
		boolean[][] graph = {{false,  true, false, false, false,  true},
                             { true, false,  true, false, false,  true},
                             {false,  true, false,  true,  true, false},
                             {false, false,  true, false,  true, false},
                             {false, false,  true,  true, false,  true},
                             { true,  true, false, false,  true, false}};
		boolean[][] graph2 = {{false,  true,  true},
		                      { true, false,  true},
		                      { true,  true, false}};
		String[] colors = { "red", "green", "blue", "yellow" };
        int[]   weights = { 2, 3, 5, 2 };
        int[]  weights2 = { 5, 3, 3, 6 };
        
		BacktrackerIterative<Type> bt =
			new BacktrackerIterative<Type>( Type.class );
		//Backtracker bt = new Backtracker();

		GraphColoringTask gc = new GraphColoringTask( graph, colors, weights );
		GraphColoringTask gc2 = new GraphColoringTask( graph2, colors, weights );
		State s = gc2.new GraphColoringState();

		State result = bt.backtrack(s);
		//bt.backtrack(s);

		System.out.println(result);
		System.out.println( bt.getNumberExpanded() );
	}
}
