package model;

public class User {
    private Internet internet;
    private Phone phone;
    private Television television;
    private long id;

    public User(long id, Internet internet, Phone phone, Television television) {
        this.id = id;
        this.internet = internet;
        this.phone = phone;
        this.television = television;
    }

    public Internet getInternet() {
        return internet;
    }

    public void setInternet(Internet internet) {
        this.internet = internet;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Television getTelevision() {
        return television;
    }

    public void setTelevision(Television television) {
        this.television = television;
    }



}
