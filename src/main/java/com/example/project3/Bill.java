package com.example.project3;
import java.util.ArrayList;
public interface Bill {
    double calculateTotalBill(ArrayList<String> selectedMenuItems,
                              ArrayList<String> selectedDrinks);
}