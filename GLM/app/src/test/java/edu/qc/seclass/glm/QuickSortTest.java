package edu.qc.seclass.glm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;

public class QuickSortTest {


    public static String reverseString(String str) {
        char[] ch = str.toCharArray();
        String rev = "";
        for (int i = ch.length - 1; i >= 0; i--) {
            rev += ch[i];
        }
        return rev;
    }

    @Test
    public void testOrderedList_shouldBeTheSame() {
        Reminder[] arr = new Reminder[]{
                new Reminder("Hola", "Type 1"),
                new Reminder("vTP", "Type 2"),
                new Reminder("Ismzy5I", "Type 3"),
                new Reminder("AtPd", "Type 4"),
                new Reminder("W6m", "Type 5"),
                new Reminder("hvtsZ6MN", "Type 6"),
                new Reminder("fLM", "Type 7"),
                new Reminder("6d7ZBZQ2", "Type 8"),
        };
        QuickSort.quickSort(arr, 0, arr.length - 1);
        assertEquals("Type 1", arr[0].getType());
        assertEquals("Type 4", arr[3].getType());
        assertEquals("Type 6", arr[5].getType());
    }

    @Test
    public void testNonOrderedList_shouldSortItBeTheSame() {
        Reminder[] arr = new Reminder[]{
                new Reminder("Hola", "Type 1"),
                new Reminder("vTP", "Type 2"),
                new Reminder("Ismzy5I", "Type 3"),
                new Reminder("AtPd", "Type 4"),
                new Reminder("W6m", "Type 5"),
                new Reminder("hvtsZ6MN", "Type 22"),
                new Reminder("fLM", "Type 7"),
                new Reminder("6d7ZBZQ2", "Type 10"),
        };
        QuickSort.quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
        assertEquals("Type 1", arr[0].getType());
        assertEquals("Type 10", arr[1].getType());
        assertEquals("Type 22", arr[3].getType());
        assertEquals("Type 7", arr[7].getType());
    }

    @Test
    public void testCompleteReversedList_shouldSortIt() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String reversedAlphabet = reverseString(alphabet);
        Reminder[] arr = new Reminder[alphabet.length()];
        for (int i = 0; i < reversedAlphabet.length(); i++) {
            String type = Character.toString(reversedAlphabet.charAt(i));
            arr[i] = new Reminder(type, type);
        }
        QuickSort.quickSort(arr, 0, arr.length - 1);
        for (int i = 0; i < alphabet.length(); i++) {
            String type = Character.toString(alphabet.charAt(i));
            assertEquals(arr[i].getType(), type);
        }
    }
}