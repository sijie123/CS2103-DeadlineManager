package seedu.address.model.task;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DeadlineTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Deadline((String) null));
    }

    @Test
    public void constructor_invalidDeadline_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline(""));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("abc"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("32/10/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("-1/10/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("-1/-2/-3"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("10/10/10000000000000000000000000000"));
    }

    @Test
    public void constructor_validDeadline_doesNotThrow() {
        new Deadline("1/10/2018");
        new Deadline("31/10/2018");
        new Deadline("5/1/2019");
        new Deadline("05/1/2019");
        new Deadline("05/01/2019");
        new Deadline("15/12/2015");
    }

    @Test
    public void isValidComparison() {
        Deadline a = new Deadline("1/10/2018");
        Deadline b = new Deadline("1/9/2018");
        Deadline c = new Deadline("01/10/2018");
        assertEquals(a.compareTo(b), 1);
        assertEquals(b.compareTo(a), -1);
        assertEquals(a.compareTo(c), 0);
    }
}
