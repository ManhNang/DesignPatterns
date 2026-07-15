import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TaxiThread implements Runnable {
    private final TaxiPool taxiPool;

    public TaxiThread(TaxiPool taxiPool) {
        this.taxiPool = taxiPool;
    }

    @Override
    public void run() {
        takeATaxi();
    }

    private void takeATaxi() {
        String threadName = Thread.currentThread().getName();
        try {
            System.out.println("New Client: " + threadName);
            Taxi taxi = taxiPool.getTaxi();

            System.out.println(threadName + " caught " + taxi.getName());
            TimeUnit.MILLISECONDS.sleep(randInt(1000, 1500));

            taxiPool.release(taxi);
            System.out.println("Served the client: " + threadName);
        } catch (InterruptedException e) {
            System.out.println(">>> Interrupted the client: " + threadName);
            Thread.currentThread().interrupt();
        } catch (TaxiNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}