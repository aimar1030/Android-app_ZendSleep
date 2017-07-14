package com.peter.zensleepfree.Model;

/**
 * Created by peter on 6/24/16.
 */
public class Average {

    int number;
    int position;
    int count;
    double average;
    double[] values;

    public Average() {

    }

    public Average(int number) {
        this.number = number;
        values = new double[number];
        reset();
    }

    public void reset() {
        count = 0;
        position = 0;
        average = 0;
        for (int i = 0; i < number; i++) {
            values[i] = 0;
        }
    }

    public void putValue(double value) {
        double divider = (count + 1 > number)? number:(count + 1);
        double multier = (count > number)?number:count;
        average = (average * multier - values[position] + value)/divider;
        values[position] = value;
        position = (++position)%number;
        count++;
    }

    public double getAverage() {
        return this.average;
    }
}
