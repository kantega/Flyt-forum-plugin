package no.kantega.forum.dao;

public enum ThreadSortOrder {
    SORT_BY_DEFAULT(0),
    SORT_BY_DATE_CREATED(1);

    public int getId() {
        return id;
    }

    private final int id;

    ThreadSortOrder(int id){
        this.id=id;
    }

}
