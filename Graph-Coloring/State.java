public interface State extends Comparable<State>
{
    /**
     * Checks if there are any possible moves from the
     * current state.
     *
     * @return
     *      true if there are possible moves.
     **/
    public boolean hasMoreChildren();

    /**
     *
     **/
    public State   nextChild();

    /**
     * Checks the validity of the current state.
     *
     * @return
     *      True if the current state is valid.
     **/
    public boolean isFeasible();

    /**
     * Checks if the current state is a solution.
     *
     * @return
     *      True if the current state is a solution.
     **/
    public boolean isSolved();
    public int     getBound();
}
