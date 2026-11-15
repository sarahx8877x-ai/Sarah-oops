package model;

public class TaskSize {

    public static final String SMALL = "Small";
    public static final String MEDIUM = "Medium";
    public static final String LARGE="Large";
    public static final String VERY_LARGE = "Very large";

    // 1 unit = 1 GPU minute. gives 0 if the size is wrong
    public static int unitsFor(String size) {
        int units = 0;
        if (size == null)
            return 0;
        if (size.equals(SMALL))
            units = 5;
        else if (size.equals(MEDIUM))
            units=15;
        else if(size.equals(LARGE))
            units = 30;
        else if (size.equals(VERY_LARGE))
            units = 60;
        return units;
    }
}