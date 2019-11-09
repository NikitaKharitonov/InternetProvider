package model;

public class Internet {
    private int speed;
    private boolean antiVirus;
    private String name;
    private double price;

    public Internet(int speed, boolean antiVirus, String name, double price) {
        this.speed = speed;
        this.antiVirus = antiVirus;
        this.name = name;
        this.price = price;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isAntiVirus() {
        return antiVirus;
    }

    public void setAntiVirus(boolean antiVirus) {
        this.antiVirus = antiVirus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
