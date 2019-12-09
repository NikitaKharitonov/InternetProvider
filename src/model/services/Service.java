package model.services;

import model.ValueReader;

import java.io.Serializable;

public abstract class Service implements Serializable, Cloneable {

    final long id;
    String name;
    //State state;

    Service(long id, String name) {
        this.id = id;
        this.name = name;
        //this.state = State.ACTIVE;
    }

    Service(String str) {
        ValueReader.setString(str);
        this.id = Long.parseLong(ValueReader.nextValue());
        this.name = ValueReader.nextValue();
    }

//    Service(long id, String name, State state) {
//        this.id = id;
//        this.name = name;
//        this.state = state;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public abstract String getType();

//    public State getState() {
//        return state;
//    }
//
//    public void setState(State state) {
//        this.state = state;
//    }
}
