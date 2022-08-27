package com.example.team7_project_1.models;

public abstract class Product implements IProduct, Comparable<Product> {

    // Fields
    private long id;
    private String name;
    private double price;
    private String description;
    private double rating;
    private long frequency;

    /**
     * Constructor
     */
    protected Product(long id, String name, double price, String description, double rating, long frequency) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Product{" +
                "soldPhoneId=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }

    /** Getters and setters for fields */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public long getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    @Override
    public int compareTo(Product product) {
        return Long.compare(product.frequency, this.frequency);
    }

    public Phone getPhone() {
        return (Phone) this;
    }

}
