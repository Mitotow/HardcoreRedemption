import fr.mitoto.hardcoreredemption.utils.InventoryPaginator;
import fr.mitoto.hardcoreredemption.utils.InventoryPaginatorEl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.SAME_THREAD)
public class InventoryPaginatorTest {
    private InventoryPaginator<Integer> paginator;

    private void addElements(int number) {
        for (int i = 0; i < number; i++) {
            paginator.add(i);
        }
    }

    @BeforeEach()
    public void setUp() {
        paginator = new InventoryPaginator<>(10);
    }

    @Test
    public void testAddOneElement() {
        paginator.add(1);

        assertEquals(1, paginator.getActual().get(0));
        assertEquals(1, paginator.getActual().size());
    }

    @Test
    public void testAddMultipleElementsToLimit() {
        this.addElements(10);

        for (int i = 0; i < 10; i++) {
            assertEquals(i, paginator.getActual().get(i));
        }
    }

    @Test
    public void testAddMultipleElementsOverLimit() {
        this.addElements(20);

        int offset = 0;
        for (int i = 0; i < 20; i++) {
            assertEquals(i, paginator.getActual().get(i - offset));

            if (i == 9) {
                paginator.next();
                offset = 10;
            }
        }
    }

    @Test
    public void testPageNumberAndNext() {
        this.addElements(20);

        int page = 1;
        assertEquals(page,  paginator.getActualPageNumber());
        while (paginator.hasNext()) {
            paginator.next();
            page++;
            assertEquals(page,  paginator.getActualPageNumber());
        }
    }

    @Test
    public void testPageNumberAndPrevious() {
        this.addElements(20);

        int page = 2;
        paginator.next();
        assertEquals(page, paginator.getActualPageNumber());
        while (paginator.hasPrevious()) {
            paginator.previous();
            page--;
            assertEquals(page, paginator.getActualPageNumber());
        }
    }

    @Test
    public void testNextShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> paginator.next());
        this.addElements(10);
        assertThrows(IllegalStateException.class, () -> paginator.next());
    }

    @Test
    public void testPreviousShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> paginator.previous());
        this.addElements(10);
        assertThrows(IllegalStateException.class, () -> paginator.previous());
    }

    @Test
    public void testHasNext() {
        this.addElements(10);
        assertFalse(paginator.hasNext());
        this.addElements(10);
        assertTrue(paginator.hasNext());
    }

    @Test
    public void testHasPrevious() {
        this.addElements(20);
        assertFalse(paginator.hasPrevious());
        paginator.next();
        assertTrue(paginator.hasPrevious());
    }

    @Test
    public void testFindPageByItem() {
        this.addElements(50);
        InventoryPaginatorEl<Integer> page = paginator.findPageByItem(39);
        assertTrue(page.contains(39));
        page = paginator.findPageByItem(100);
        assertNull(page);
    }

    @Test
    public void testRemove() {
        this.addElements(40);
        paginator.remove(30);
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
        expected.add(new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)));
        expected.add(new ArrayList<>(Arrays.asList(20, 21, 22, 23, 24, 25, 26, 27, 28, 29)));
        expected.add(new ArrayList<>(Arrays.asList(31, 32, 33, 34, 35, 36, 37, 38, 39)));

        for (final ArrayList<Integer> list : expected) {
            for (int i = 0; i < list.size(); i++) {
                assertEquals(list.get(i), paginator.getActual().get(i));
            }

            if (paginator.getActualPageNumber() != 4) {
                paginator.next();
            }
        }

        assertEquals(4, paginator.getActualPageNumber());
        assertEquals(9, paginator.getActual().size());
    }

    @Test
    public void testRemove2() {
        this.addElements(40);
        paginator.remove(12);
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
        expected.add(new ArrayList<>(Arrays.asList(10, 11, 13, 14, 15, 16, 17, 18, 19, 20)));
        expected.add(new ArrayList<>(Arrays.asList(21, 22, 23, 24, 25, 26, 27, 28, 29, 30)));
        expected.add(new ArrayList<>(Arrays.asList(31, 32, 33, 34, 35, 36, 37, 38, 39)));

        for (final ArrayList<Integer> list : expected) {
            for (int i = 0; i < list.size(); i++) {
                assertEquals(list.get(i), paginator.getActual().get(i));
            }

            if (paginator.getActualPageNumber() != 4) {
                paginator.next();
            }
        }

        assertEquals(4, paginator.getActualPageNumber());
        assertEquals(9, paginator.getActual().size());
    }

    @Test
    public void testGetFirstNotFull() {
        this.addElements(9);
        assertEquals(paginator.getActual(), paginator.getFirstNotFull());
    }

    @Test
    public void testGetFirstNotFull2() {
        this.addElements(20);
        paginator.next();
        assertFalse(paginator.hasNext());
        assertNull(paginator.getFirstNotFull());
        paginator.add(50);
        InventoryPaginatorEl<Integer> page = paginator.getFirstNotFull();
        assertNotNull(page);
        assertEquals(50, page.get(0));
        assertEquals(1, page.size());
    }

    @Test
    public void testGetFirstNotFullOrCreate() {
        this.addElements(20);
        paginator.next();
        assertFalse(paginator.hasNext());
        paginator.getFirstNotFullOrCreate();
        assertTrue(paginator.hasNext());
    }

    @Test
    public void testGetActual() {
        this.addElements(20);
        final InventoryPaginatorEl<Integer> page = paginator.next();
        assertEquals(page, paginator.getActual());
    }

    @Test
    public void testGetLast() {
        this.addElements(20);
        assertEquals(paginator.getActual().next(), paginator.getLast());
    }

    @Test
    public void testGetActualIndex() {
        this.addElements(20);
        assertEquals(0, paginator.getActualIndex());
        paginator.next();
        assertEquals(1, paginator.getActualIndex());
    }

    @Test
    public void testGetLastIndex() {
        this.addElements(20);
        assertEquals(1, paginator.getLastIndex());
    }

    @Test
    public void testSize() {
        this.addElements(20);
        assertEquals(2, paginator.size());
    }
}
