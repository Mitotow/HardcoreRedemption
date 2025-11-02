package fr.mitoto.hardcoreredemption.utils;

public class InventoryPaginator<T> {
    private final InventoryPaginatorEl<T> first;
    private InventoryPaginatorEl<T> actual;
    private final int limit;

    /**
     * InventoryPaginator is used to create paginated inventory
     *
     * @param limit
     */
    public InventoryPaginator(final int limit) {
        this.first = new InventoryPaginatorEl<>(null);
        this.actual = this.first;
        this.limit = limit;
    }

    /**
     * Return the actual page of paginator
     *
     * @return actual page
     */
    public InventoryPaginatorEl<T> getActual() {
        return actual;
    }

    /**
     * Check the presence of a next page in actual page
     *
     * @return true if there is a next page, false if not
     */
    public boolean hasNext() {
        return (actual.next() != null);
    }

    /**
     * Check the presence of a previous page in actual page
     *
     * @return true if there is a previous page, false if not
     */
    public boolean hasPrevious() {
        return (actual.previous() != null);
    }

    /**
     * Return the next page of paginator
     *
     * @return next page
     * @throws IllegalStateException no next page found
     */
    public InventoryPaginatorEl<T> next() throws IllegalStateException {
        if (!this.hasNext()) {
            throw new IllegalStateException();
        }

        this.actual = this.actual.next();
        return this.actual;
    }

    /**
     * Return the previous page of paginator
     *
     * @return previous page
     * @throws IllegalStateException no previous page found
     */
    public InventoryPaginatorEl<T> previous() throws IllegalStateException {
        if (!this.hasPrevious()) {
            throw new IllegalStateException();
        }

        this.actual = this.actual.previous();
        return this.actual;
    }

    public InventoryPaginatorEl<T> findPageByItem(final T item) {
        InventoryPaginatorEl<T> temp = this.first;

        do {
            if ((temp = temp.next()).contains(item)) {
                return temp;
            }
        } while (temp.hasNext());

        return null;
    }

    /**
     * Add an item to the first not full page of the paginator
     *
     * @param item item to add
     */
    public void add(final T item) {
        if (this.first.size() >= limit) {
            InventoryPaginatorEl<T> page = this.getFirstNotFullOrCreate();
            page.add(item);
        } else this.first.add(item);
    }

    /**
     * Add an empty page at last position of paginator
     *
     * @return New page
     */
    private  InventoryPaginatorEl<T> addPage() {
        final InventoryPaginatorEl<T> last = this.getLast();
        final InventoryPaginatorEl<T> page = new InventoryPaginatorEl<>(last);

        last.setNext(page);
        page.setPrevious(last);
        return page;
    }

    public void remove(final T item) {
        final InventoryPaginatorEl<T> page = this.findPageByItem(item);
        page.remove(item);

        InventoryPaginatorEl<T> prev;
        InventoryPaginatorEl<T> tmp = page;
        while(tmp.hasNext()) {
            prev = tmp;
            tmp = tmp.next();
            prev.add(tmp.get(0));
            tmp.remove(0);
        }
    }

    /**
     * Return the first not full page of the inventory paginator
     * If all pages are full, it will create a new one
     *
     * @return First not full page
     */
    public InventoryPaginatorEl<T> getFirstNotFullOrCreate() {
        InventoryPaginatorEl<T> page = this.getFirstNotFull(this.actual);
        if (page == null) {
            page = this.addPage();
        }

        return page;
    }

    /**
     * Method to find the first not full page of the inventory paginator
     *
     * @return First not full page or null if all pages are full
     */
    public InventoryPaginatorEl<T> getFirstNotFull() {
        return this.getFirstNotFull(this.first);
    }

    private InventoryPaginatorEl<T> getFirstNotFull(final InventoryPaginatorEl<T> first) {
        if (first.size() < limit) {
            return first;
        }

        InventoryPaginatorEl<T> temp = first;
        while (temp.hasNext()) {
            if ((temp = temp.next()).size() < limit) {
                return temp;
            }
        }

        return null;
    }

    /**
     * Get last element of paginator
     *
     * @return the last element
     */
    public InventoryPaginatorEl<T> getLast() {
        return this.getLastRecursive(this.actual);
    }

    /**
     * Get last element of paginator recursively
     *
     * @param element Starting element
     * @return Last element of the chained list
     */
    private InventoryPaginatorEl<T> getLastRecursive(InventoryPaginatorEl<T> element) {
        if (element.hasNext()) {
            return getLastRecursive(element.next());
        }

        return element;
    }

    private int getElementIndex(final InventoryPaginatorEl<T> element) {
        int index = 0;

        if (this.first.equals(element)) {
            return index;
        }

        InventoryPaginatorEl<T> temp = this.first;
        while (temp.hasNext()) {
            temp = temp.next();
            index ++;

            if (temp.equals(element)) {
                return index;
            }
        }

        return index;
    }

    /**
     * Retrieve the actual page index
     *
     * @return Actual page index
     */
    public int getActualIndex() {
        return this.getElementIndex(this.actual);
    }

    /**
     * Retrieve the last index
     *
     * @return Last index
     */
    public int getLastIndex() {
        return this.getElementIndex(null);
    }

    /**
     * Retrieve the actual page number
     *
     * @return Actual page number
     */
    public int getActualPageNumber() {
        return this.getActualIndex() + 1;
    }

    /**
     * Retrieve number of pages
     *
     * @return Number of pages
     */
    public int size() {
        return this.getLastIndex() + 1;
    }
}
