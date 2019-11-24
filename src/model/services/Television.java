package model.services;

import util.Annotations.MethodParameter;

import java.util.Date;

public class Television extends Service {

    private int numberOfChannels;

    public Television(
            @MethodParameter(name = "name", type = String.class)
            String name,
            @MethodParameter(name = "numberOfChannels", type = Integer.class)
            Integer numberOfChannels ) {

        super(name);
        this.numberOfChannels = numberOfChannels;
    }

    public Television(Television television) {
        super(television);
        this.numberOfChannels = television.numberOfChannels;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(Integer numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    @Override
    public String getType() {
        return "Television";
    }

    @Override
    public String toString() {
        return super.toString() + ": Television{" +
                "numberOfChannels=" + numberOfChannels +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if (!(obj instanceof Television))
            return false;
        return this.numberOfChannels == ((Television) obj).numberOfChannels;
    }

    @Override
    public Object clone(){
        //deep copying
        return new Television(this);
    }
}
