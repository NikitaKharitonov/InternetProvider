package model.services;

import model.ValueReader;
import util.Annotations.MethodParameter;

public class Television extends Service {

    private int numberOfChannels;

    public Television(
            @MethodParameter(name = "id", type = Long.class)
                    Long id,
            @MethodParameter(name = "name", type = String.class)
                    String name,
            @MethodParameter(name = "numberOfChannels", type = Integer.class)
                    Integer numberOfChannels ) {
        super(id, name);
        this.numberOfChannels = numberOfChannels;
    }

    public Television(String str) {
        super(str);
        numberOfChannels = Integer.parseInt(ValueReader.nextValue());
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
        return getClass().getSimpleName() +
                " {id=" + id +
                ", name=" + name +
                ", numberOfChannels=" + numberOfChannels +
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
}
