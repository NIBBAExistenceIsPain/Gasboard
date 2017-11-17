package com.example.felix.gasboard;

/**
 * Created by Stacra on 13/11/2017.
 */

public class ListEntry {

    private int width;
    private int[] numbers;
    private String name;

    public ListEntry(int width, int[] numbers, String name){
        this.name = name;
        this.numbers = numbers;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public String getName() {
        return name;
    }
}
