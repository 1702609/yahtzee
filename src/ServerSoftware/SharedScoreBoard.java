package ServerSoftware;

public class SharedScoreBoard
    {
    private static int[] everyoneScore;
    private static int threadsWaiting=0;
    private static boolean accessing=false; // true a thread has a lock, false otherwise

        public void startScoreBoard(int spaceRequired)
            {
            everyoneScore = new int[spaceRequired];
            }

        public synchronized int[] getCurrentScoreBoard() throws InterruptedException
        {
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName()+" is attempting to acquire a lock!");
        ++threadsWaiting;
        while (true) {
            if (accessing == false) {
                acquireLock();
                System.out.println(me.getName() + " got a lock!");
                releaselock();
                --threadsWaiting;
                notifyAll();
                return everyoneScore;
            } else {
                wait();
                System.out.println(me.getName() + " is waiting!");
            }
        }
        }

        private void releaselock()
            {
            this.accessing = false;
            }

        private void acquireLock()
            {
            this.accessing = true;
            }

        public void updateScoreBoard()
        {

        }
    }
