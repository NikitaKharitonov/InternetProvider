package model;

public class IdGenerator<T> {
    private long maxId;

    public IdGenerator(long maxId) {
        this.maxId = maxId;
    }

    public long next() {
        return ++maxId;
    }

}
