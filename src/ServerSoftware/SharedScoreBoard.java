package ServerSoftware;

public class SharedScoreBoard
    {
    private int[] everyoneScore;
    private int threadsWaiting=0;
    private boolean accessing=false; // true a thread has a lock, false otherwise

        public void startScoreBoard(int spaceRequired)
            {
            everyoneScore = new int[spaceRequired];
            }

        public synchronized int[] getCurrentScoreBoard() throws InterruptedException
        {
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName()+" is attempting to acquire a lock!");
        ++threadsWaiting;

        --threadsWaiting;
        accessing = true;
        System.out.println(me.getName()+" got a lock!");
        return everyoneScore;
        }
    public void updateScoreBoard()
        {

        }
    }
