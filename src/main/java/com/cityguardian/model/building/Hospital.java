package com.cityguardian.model.building;

public class Hospital extends Building {
    private int medicalSupplies;

    public Hospital(String name, int capacity, int medicalSupplies) {
        super(name, capacity);
        this.medicalSupplies = medicalSupplies;
    }

    public int getMedicalSupplies() { return medicalSupplies; }
    public void useSupply() {
        if (medicalSupplies > 0) {
            medicalSupplies--;
        }
    }
}
