package fr.mitoto.hardcoreredemption.utils;

import java.util.ArrayList;

public class InventoryPaginatorEl<T> extends ArrayList<T> {
    private InventoryPaginatorEl<T> previousItems;
    private InventoryPaginatorEl<T> nextItems = null;

    public InventoryPaginatorEl(InventoryPaginatorEl<T> previousItems) {
        this.previousItems = previousItems;
    }

    public InventoryPaginatorEl<T> previous() {
        return previousItems;
    }

    public void setPrevious(InventoryPaginatorEl<T> previousItems) {
        this.previousItems = previousItems;
    }

    public InventoryPaginatorEl<T> next() {
        return nextItems;
    }

    public void setNext(InventoryPaginatorEl<T> nextItems) {
        this.nextItems = nextItems;
    }

    public boolean hasNext() {
        return (this.nextItems != null);
    }
}
