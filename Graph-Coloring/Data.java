/**
 * Interface to ensure consistency between data
 * structures used by BacktrackerIterative.
 **/

public interface Data
{
    /**
     * Adds an element to the data structure.
     *
     * @return
     *      True if successful.
     **/
    public boolean add( State element );

	/**
	 * Removes and returns the first element.
	 *
	 * @return
	 *      The first element.
	 **/
    public State pop();

    /**
     * Checks if the data structure is empty.
     *
     * @return
     *      True if the data structure is empty.
     **/
    public boolean empty();
}
