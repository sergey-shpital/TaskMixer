package xyz.pagenet.concurent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Sergey on 30.04.2017.
 */
public class CallableEntry implements Delayed {
    private static final AtomicLong order = new AtomicLong(0L);
    private final LocalDateTime localDateTime;
    private final Callable callable;
    private final long id;

    public CallableEntry(LocalDateTime localDateTime, Callable callable) {
        this.localDateTime = localDateTime;
        this.callable = callable;
        this.id = order.getAndIncrement();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        int cmpDate = localDateTime.toLocalDate().compareTo(LocalDate.now());
        if( cmpDate == 0 ) {
            long delayNano = localDateTime.toLocalTime().toNanoOfDay() - LocalTime.now().toNanoOfDay();
            return unit.convert(delayNano, TimeUnit.NANOSECONDS);
        }
        if( cmpDate < 0 ) return 0L;
        return unit.convert(1,TimeUnit.DAYS); //toEpochDay()is slowly, 1 day enough
    }

    @Override
    public int compareTo(Delayed o) {

        CallableEntry other = (CallableEntry)o;

        int cmp = localDateTime.compareTo(other.getLocalDateTime());
        if(cmp==0) {
            return Long.compare(getId(),other.getId());
        }
        return cmp;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public Callable getCallable() {
        return callable;
    }

    public long getId() {
        return id;
    }
}
