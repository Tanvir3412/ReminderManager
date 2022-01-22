package edu.qc.seclass.glm;

public class QuickSort {
    static void swap(Reminder[] arr, int i, int j) {
        Reminder temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    static int partition(Reminder[] arr, int low, int high) {
        Reminder pivot = arr[high];
        int i = (low - 1);
        for(int j = low; j <= high - 1; j++) {
            int compare = arr[j].getType().compareToIgnoreCase(pivot.getType());
            if (compare < 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    static void quickSort(Reminder[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
}
