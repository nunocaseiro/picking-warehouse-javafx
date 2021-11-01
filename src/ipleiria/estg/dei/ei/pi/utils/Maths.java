package ipleiria.estg.dei.ei.pi.utils;

import java.util.List;

public class Maths {
    public static double average(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    public static double average(List<Double> array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.size();
    }

    public static double standardDeviation(double[] array, double mean) {
        double sum = 0;
        for (double value : array) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(1 / (double) array.length * sum);
    }

    public static double standardDeviation(List<Double> array, double mean) {
        double sum = 0;
        for (Double value : array) {
            sum += Math.pow(value.doubleValue() - mean, 2);
        }
        return Math.sqrt(1 / (double) array.size() * sum);
    }

    public static double computeMedian(List<Double> values) {
        if (values.size() == 0) {
            return 0;
        }

        double value;
        if (values.size() % 2 == 0) {
            value = (values.get(values.size() / 2) + values.get((values.size() / 2) - 1)) / 2;
        } else {
            value = values.get(values.size() / 2);
        }

        return  value;
    }
}
