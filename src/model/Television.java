package model;

import java.nio.channels.Channel;

public class Television {
    private Channel[] channels;

    public Television(Channel[] channels) {
        this.channels = channels;
    }

    public Channel[] getChannels() {
        return channels;
    }

    public void setChannels(Channel[] channels) {
        this.channels = channels;
    }
}
