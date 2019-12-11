package server.model.util;

public class IdGenerator {
    private long maxId;

    public IdGenerator(long maxId) {
        this.maxId = maxId;
    }

    public long next() {
        return ++maxId;
    }

}
