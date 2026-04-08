package com.sweetcupcake.model;

public class Cupcake {
    private int id;
    private String name;
    private String flavor;
    private String category;
    private double price;
    private String description;
    private boolean available;
    private String createdAt;

    public Cupcake() {}

    public Cupcake(String name, String flavor, String category, double price, String description) {
        this.name = name;
        this.flavor = flavor;
        this.category = category;
        this.price = price;
        this.description = description;
        this.available = true;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFlavor() { return flavor; }
    public void setFlavor(String flavor) { this.flavor = flavor; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    @Override
    public String toString() {
        return String.format("Cupcake{id=%d, name='%s', flavor='%s', category='%s', price=%.2f}", id, name, flavor, category, price);
    }
}
