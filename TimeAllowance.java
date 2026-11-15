package model;

/* GPU minutes of a student
   available = allocated - used - reserved */
public class TimeAllowance {

    public static final int DEFAULT_MINUTES = 100;

    private int allocated;
    private int used;
    private int reserved;

    public TimeAllowance(int allocated) {
        this.allocated = allocated;
        used = 0;
        reserved=0;
    }

    // for loading from the file
    public TimeAllowance(int allocated, int used, int reserved) {
        this.allocated = allocated;
        this.used=used;
        this.reserved = reserved;
    }

    public int getAllocated() { return allocated; }
    public int getUsed() {
        return used;
    }
    public int getReserved() { return reserved; }

    public int getAvailable() {
        int a = allocated - used - reserved;
        return a;
    }

    // false if there is not enough time
    public boolean reserve(int minutes) {
        if (minutes > getAvailable())
            return false;
        reserved = reserved + minutes;
        return true;
    }

    public void release(int minutes) {
        reserved = reserved - minutes;
        if (reserved < 0)
            reserved = 0;      // should not happen
    }

    public void charge(int minutes) {
        if (minutes < 0)
            return;
        used = used + minutes;
        if (used > allocated - reserved)
            used = allocated - reserved;     // balance never goes below zero
    }

    public void addAllocated(int minutes) {
        allocated=allocated+minutes;
    }
}