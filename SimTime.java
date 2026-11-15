package model;

// simulated time. we store minutes counted from 01/01/2000 00:00
public class SimTime {

    private int total;
    private int day, month, year;
    private int hour;
    private int minute;

    public SimTime(int d, int m, int y, int h, int min) {
        day = d;
        month=m;
        year = y;
        hour=h;
        minute = min;

        int days = 0;
        for (int i = 2000; i < y; i++) {
            if (isLeapYear(i))
                days = days + 366;
            else
                days = days + 365;
        }
        for (int i=1; i<m; i++)
            days = days + daysInMonth(i, y);
        days = days + d - 1;

        total = days*24*60 + h*60 + min;
    }

    public SimTime(int total) {
        this.total = total;

        // turn the minutes back into a date
        int days = total / 1440;
        int rest = total % 1440;
        hour = rest / 60;
        minute = rest % 60;

        year = 2000;
        boolean done = false;
        while (!done) {
            int len = 365;
            if (isLeapYear(year))
                len = 366;
            if (days >= len) {
                days = days - len;
                year++;
            }
            else
                done = true;
        }

        month=1;
        while (days >= daysInMonth(month, year))
        {
            days = days - daysInMonth(month, year);
            month++;
        }
        day = days+1;
    }

    public static boolean isLeapYear(int y) {
        if (y % 400 == 0) return true;
        if (y % 100 == 0) return false;
        if (y % 4 == 0)
            return true;
        return false;
    }

    public static int daysInMonth(int m, int y) {
        int n;
        switch (m) {
            case 4: case 6: case 9: case 11:
                n = 30;
                break;
            case 2:
                if (isLeapYear(y)) n = 29;
                else n = 28;
                break;
            default:
                n = 31;
        }
        return n;
    }

    // reads text like 16/11/2026 10:00. the word null means no time
    public static SimTime fromText(String text) {
        if (text.equals("null"))
            return null;
        String[] parts = text.split(" ");
        String[] d = parts[0].split("/");
        String[] t = parts[1].split(":");
        int dd = Integer.parseInt(d[0]);
        int mm = Integer.parseInt(d[1]);
        int yy=Integer.parseInt(d[2]);
        int hh = Integer.parseInt(t[0]);
        int mi = Integer.parseInt(t[1]);
        return new SimTime(dd, mm, yy, hh, mi);
    }

    public SimTime plusMinutes(int n) {
        SimTime t = new SimTime(total + n);
        return t;
    }

    /* minutes from this time to the other one */
    public int minutesUntil(SimTime other) {
        return other.total - total;
    }

    public int getTotal() { return total; }

    public String toString() {
        String s = String.format("%02d/%02d/%04d %02d:%02d", day, month, year, hour, minute);
        return s;
    }
}