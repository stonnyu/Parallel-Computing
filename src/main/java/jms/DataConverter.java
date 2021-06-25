package jms;

public class DataConverter {
    public static StringBuilder stringComposer(Double[] array) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                stringBuilder.append(array[i]);
            } else {
                stringBuilder.append(",").append(array[i]);
            }
        }

        return stringBuilder;
    }

    public static Double[] stringDecomposer(String string) {
        // Split and save message into array
        String[] stringArray = string.split(",");

        Double[] doubleArray = new Double[stringArray.length];

        // Save message array into double array
        for (int i = 0; i < stringArray.length; i++) {
            doubleArray[i] = Double.parseDouble(stringArray[i]);
        }

        return doubleArray;
    }
}