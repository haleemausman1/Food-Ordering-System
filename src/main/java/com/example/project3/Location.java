package com.example.project3;

import java.util.ArrayList;
import java.util.HashMap;

public class Location {
    private String city;
    private ArrayList<String> towns;
    private HashMap<String, ArrayList<Restaurant>> townRestaurants; // Restaurants mapped to each town

    public Location() {
        towns = new ArrayList<>();
        townRestaurants = new HashMap<>();
    }

    // City getters and setters
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    // Towns getters and setters
    public void setTowns(ArrayList<String> towns) {
        this.towns = towns;
    }

    public ArrayList<String> getTowns() {
        return towns;
    }

    // Restaurants for each town
    public void setRestaurants(String town, ArrayList<Restaurant> restaurants) {
        townRestaurants.put(town, restaurants);
    }

    public ArrayList<Restaurant> getSelectedTownRestaurants(String selectedTown) {
        return townRestaurants.getOrDefault(selectedTown, new ArrayList<>());
    }

    @Override
    public String toString() {
        return this.city;  // assuming you have a field 'city'
    }


    // Optional: static helper to return cities list
    public static ArrayList<String> setCities(ArrayList<String> cities) {
        return cities; // Can also remove if unused
    }
}
