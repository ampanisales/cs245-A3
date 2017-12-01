/**
 * Class Purpose: Used in the findPath() method 
 * in Graph.java
 * 
 * @author Anthony Panisales
 */

public class ArrayQueue implements Queue {
	
	private int head; //Place where we remove from queue
	private int tail; //Place where we add to queue
	private Object[] arr;

	public ArrayQueue() {
		head = 0;
		tail = 0;
		arr = new Object[10];
	}

	/**
	 * Function Purpose: Adds an item to the queue.
	 */
	public void enqueue(Object item) {
		if (full()) {
			growQueue();
		}
		arr[tail] = item;
		tail = (tail + 1) % arr.length; //Prevents out of bounds
	}

	/**
	 * Function Purpose: Removes an item from the queue.
	 */
	public Object dequeue() {
		if (empty()) {
			return null;
		}
		Object temp = arr[head];
		head = (head + 1) % arr.length; //Prevents out of bounds
		return temp;
	}

	/**
	 * Function Purpose: Doubles the size of the queue. Used when the
	 * queue is almost full.
	 */
	public void growQueue() {
		Object[] newArr = new Object[2 * arr.length];

		System.arraycopy(arr, head, newArr, 0, arr.length-head);
		if (tail < head) {
			System.arraycopy(arr, 0, newArr, arr.length-head, tail);
		}
		head = 0;
		tail = arr.length-1;
		arr = newArr;
	}

	/**
	 * Function Purpose: Indicates whether the queue is full or not
	 */
	public boolean full() {
		if ((tail+1) % arr.length == head) { //Prevents out of bounds
			return true;
		} 
		return false;
	}

	/**
	 * Function Purpose: Indicates whether the queue is empty or not
	 */
	public boolean empty() {
		if (head == tail) {
			return true;
		} 
		return false;
	}
}
