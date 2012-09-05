package utility;

public class Counter {

	private int counter;
	
	public Counter(){
		counter = 0;
	}
	
	public synchronized int incr(){
		counter++;
		//System.out.println("INC:"+counter);
		return counter;
	}
	public synchronized int decr(){
		counter--;
		//System.out.println("DEC:"+counter);
		return counter;
	}

	public synchronized void reset(){
		counter = 0;
	}
}
