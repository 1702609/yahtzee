package ServerSoftware;

import java.util.Arrays;

public class SharedScoreBoard {
	
	private SharedScoreBoard mySharedObj;
	private String myThreadName;
	private String everyoneScore="";
	private boolean accessing=false; // true a thread has a lock, false otherwise

	public void setScoreBoardSize(int size)
		{
		everyoneScore += "[";
		for (int i = 0; i<size; i++)
			{
			everyoneScore += "0,";
			}
		everyoneScore=everyoneScore.substring(0, everyoneScore.length() - 1);
		everyoneScore += "]";
		}


//Attempt to aquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    /* The processInput method */

	public synchronized String getScoreBoard()
		{
		return everyoneScore;
    	}	

	public void setScoreBoard(int[] latestscore)
		{
		everyoneScore = Arrays.toString(latestscore);
		
		}
}

