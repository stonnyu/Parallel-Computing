package sort;

public class MergeSort {
    public static void mergeSort(Double[] array) {
        // Only divide array if it's larger than 1 element
        if (array.length > 1) {
            int mid = array.length / 2;

            // Split array in left and right half
            Double[] left = new Double[mid];
            System.arraycopy(array, 0, left, 0, mid);

            Double[] right = new Double[array.length - mid];
            System.arraycopy(array, mid, right, 0, array.length - mid);

            // Divide and sort with recursion
            mergeSort(left);
            mergeSort(right);

            // Merge left with right
            merge(left, right, array);
        }
    }

    public static void merge(Double[] left, Double[] right, Double[] array) {
        int currIndexLeft = 0;
        int currIndexRight = 0;
        int currIndexArray = 0;

        // Sorting of the left and right array
        while (currIndexLeft < left.length && currIndexRight < right.length) {
            if (left[currIndexLeft] < right[currIndexRight]) {
                array[currIndexArray] = left[currIndexLeft];
                currIndexLeft++;
            } else {
                array[currIndexArray] = right[currIndexRight];
                currIndexRight++;
            }
            currIndexArray++;
        }

        // Executes when the right array has no elements left
        while (currIndexLeft < left.length) {
            array[currIndexArray] = left[currIndexLeft];
            currIndexLeft++;
            currIndexArray++;
        }

        // Executes when the left array has no elements left
        while (currIndexRight < right.length) {
            array[currIndexArray] = right[currIndexRight];
            currIndexRight++;
            currIndexArray++;
        }
    }

    public static void main(String[] args) {
        MergeSort mergeSort = new MergeSort();

        String fileName = "src/main/resources/8mil.csv";
        ReadCSV read = new ReadCSV(fileName);

        Double[] unsortedArray = read.readCSV(fileName);

        long startTime = System.currentTimeMillis();
        mergeSort.mergeSort(unsortedArray);
        long endTime = System.currentTimeMillis();

        System.out.println("Sequential = total sorting time in ms:" + (endTime - startTime));
    }
}