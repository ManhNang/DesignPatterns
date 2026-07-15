import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TaxiPool {
    private static final long EXPIRED_TIME_IN_MILISECOND = 1200;
    private static final int NUMBER_OF_TAXI = 4;
    private final List<Taxi> available = Collections.synchronizedList(new ArrayList<>());
    private final List<Taxi> inUse = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger count = new AtomicInteger(0);

    public synchronized void release(Taxi taxi) {
        inUse.remove(taxi);
        available.add(taxi);
        System.out.println(taxi.getName() + " is free");
        notifyAll();
    }

    public synchronized Taxi getTaxi() {
        if (!available.isEmpty()) {
            Taxi taxi = available.remove(0);
            inUse.add(taxi);
            return taxi;
        }

        if (count.get() < NUMBER_OF_TAXI) {
            Taxi taxi = this.createTaxi();
            inUse.add(taxi);
            return taxi;
        }

        return waitingUntilTaxiAvailable();
    }

    private Taxi waitingUntilTaxiAvailable() {
        long startTime = System.currentTimeMillis();
        long timeLeft = EXPIRED_TIME_IN_MILLISECOND;

        System.out.println(Thread.currentThread().getName() + " is waiting for a taxi...");

        while (available.isEmpty()) {
            try {
                wait(timeLeft);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TaxiNotFoundException("Thread interrupted while waiting for taxi");
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            timeLeft = EXPIRED_TIME_IN_MILLISECOND - elapsedTime;

            if (timeLeft <= 0 && available.isEmpty()) {
                throw new TaxiNotFoundException(
                        ">>> Timeout! No taxi available for " + Thread.currentThread().getName());
            }
        }

        Taxi taxi = available.remove(0);
        inUse.add(taxi);
        return taxi;
    }

    private Taxi createTaxi() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Taxi taxi = new Taxi("Taxi " + count.incrementAndGet());
        System.out.println(taxi.getName() + " isCreated");
        return taxi;
    }
}