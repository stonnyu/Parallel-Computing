package sort;

import java.util.concurrent.atomic.AtomicInteger;

public class Parallel extends Thread {

    private static AtomicInteger num = new AtomicInteger(0);
    private final static int MAX_ADDITIONAL_THREADS = 0;

    public void parallelMergeSort(Double[] array) {
        // Only divide array if it's larger than 1 element
        if (array.length > 1) {
            Thread rightThread = null;
            int mid = array.length / 2;

            // Split array in left and right half
            Double[] left = new Double[mid];
            System.arraycopy(array, 0, left, 0, mid);

            Double[] right = new Double[array.length - mid];
            System.arraycopy(array, mid, right, 0, array.length - mid);

            // Divide and sort with recursion

            if (num.get() < MAX_ADDITIONAL_THREADS) {
                num.incrementAndGet();
                rightThread = new Thread(() -> parallelMergeSort(right));
                rightThread.start();
//                rightThread.setName("Thread number: " + num.get());
//                System.out.println(rightThread.getName());
            } else {
                parallelMergeSort(right);
            }

            parallelMergeSort(left);


            if (rightThread != null) {
                try {
                    rightThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Merge left with right
            MergeSort.merge(left, right, array);
        }
    }

    public static void main(String[] args) {
        Parallel parallel = new Parallel();
        String fileName = "src/main/resources/95k.csv";
        Double[] unsortedArray = ReadCSV.readCSV(fileName);

        // Copy of unsorted array for comparison
//        Double[] copyArray = new Double[unsortedArray.length];
//        System.arraycopy(unsortedArray, 0, copyArray, 0, unsortedArray.length);

        long startTime = System.currentTimeMillis();
        parallel.parallelMergeSort(unsortedArray);
        long endTime = System.currentTimeMillis();

        System.out.println("sort.Parallel = total sorting time in ms:" + (endTime - startTime));

        // Sort a copy the Java (correct) way
//        Arrays.sort(copyArray);

        // Check if our results are equal to the Java (correct) way
//        System.out.println(Arrays.equals(unsortedArray, copyArray));
    }
}