/**
 * Class to handle graph coloring problems.
 **/

public class GraphColoringTask
{
    /* Instance Constants */
    private final String[]    COLORS;
    private final int[]       WEIGHTS;
    private final int         MIN_WEIGHT;
    private final boolean[][] GRAPH;

    /*== Constructors ==*/

    /**
     * Constructs a new GraphColoringTask with a
     * given adjacency matrix, color list, and a
     * list of color weights.
     *
     * @param GRAPH
     *      The adjacency matrix.
     * @param COLORS
     *      The list of colors.
     * @param WEIGHTS
     *      The weight of each color.
     **/
    public GraphColoringTask( final boolean[][] GRAPH,
                              final String[]    COLORS,
                              final int[]       WEIGHTS )
    {
        this.GRAPH   = GRAPH;
        this.COLORS  = COLORS;
        this.WEIGHTS = WEIGHTS;

        int minWeight = this.WEIGHTS[0];
        for ( int i : this.WEIGHTS )
            minWeight = ( i < minWeight ) ? i : minWeight;
        this.MIN_WEIGHT = minWeight;
    }

    /*== Inner Classes ==*/

    /**
     * Class to handle states in the process of
     * solving graph coloring problems.
     **/
    class GraphColoringState implements State, Cloneable
    {
        /* Instance Variables */
        private int[] nodeColors;
        private int   currentNode;
        private int   nextColor;
        private int   children;

        /*== Constructors ==*/

        /**
         * Constructs the first state of the
         * GraphColoringTask.
         **/
        public GraphColoringState()
        {
            this.nodeColors  = new int[GRAPH.length];
            this.currentNode = -1;
            this.nextColor   =  0;
            this.children    =  0;

            for ( int i = 0; i < GRAPH.length; i++ )
                this.nodeColors[i] = -1;
        }

        /*== Accessors ==*/
        @Override
        public Object clone() //const
        {
            /* Local Variables */
            GraphColoringState s = new GraphColoringState();

            /* Copy Values to New Instance */
            for ( int i = 0; i < this.nodeColors.length; i++ )
                s.nodeColors[i] = this.nodeColors[i];

            s.currentNode = this.currentNode;
            s.children    = this.children;
            return s;
        }

        public boolean hasMoreChildren() //const
        {
            return !isSolved() && ( children < COLORS.length );
        }

        public boolean isFeasible() //const
        {
            for ( int i = 0; i < nodeColors.length; i++ )
                if ( i == currentNode )
                    continue;
                else
                    if ( GRAPH[currentNode][i] &&
                         nodeColors[currentNode] == nodeColors[i] )
                        return false;
            return true;
        }

        public boolean isSolved() //const
        {
            return currentNode == GRAPH.length - 1;
        }

        public int getBound() //const
        {
            /* Local Variables */
            int bound = 0;

            for ( int i = 0; i < nodeColors.length; i++ )
                if ( i <= currentNode )
                    bound+= WEIGHTS[nodeColors[i]];
                else
                    bound+= MIN_WEIGHT;
            return bound;
        }

        @Override
        public String toString() //const
        {
            /* Local Variables */
            String output = "";

            /* Generate Output */
            for ( int i = 0; i < GRAPH.length; i++ )
                output+= ( this.nodeColors[i] >= 0 ?
                           COLORS[this.nodeColors[i]] : "0" ) + "\n";
            return output;
        }

        @Override
        public int compareTo( final State that ) //const
        {
            if ( ( (GraphColoringState)this ).currentNode >
                 ( (GraphColoringState)that ).currentNode )
                return -1;
            if ( this.getBound() > that.getBound() )
                return 1;
            return -1;
        }

        /*== Mutators ==*/
        public State nextChild()
        {
            GraphColoringState child = (GraphColoringState)( this.clone() );
            child.children = 0;
            child.nodeColors[++child.currentNode] = this.nextColor;
            this.nextColor++;
            child.nextColor%= COLORS.length;
            this.children++;
            return child;
        }
    }
}
