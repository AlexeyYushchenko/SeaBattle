package model;

import java.util.Random;

/**
 minimumValue – the least value that can be returned
 maximumValue – the upper bound (exclusive) for the returned value
 */
public class CoordinateGenerator {
    private static final Random random = new Random();
    private final int minimumValue;
    private final int maximumValue;

    public CoordinateGenerator(int maximumValue) {
        this.minimumValue = 0;
        this.maximumValue = maximumValue;
    }

    public int generateInt(){
        return random.nextInt(minimumValue, maximumValue);
    }
}
