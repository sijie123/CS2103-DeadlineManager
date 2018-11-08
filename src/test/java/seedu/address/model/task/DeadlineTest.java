package seedu.address.model.task;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/-1/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/1/0"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/1/-1"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("10/10/10000000000000000000000000000"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/10/2018a"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/10/"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1//2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("/10/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("12/100/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("12/33/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("12/100000000000000000000000000/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("12/100000/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("100000000000000000000000000/1/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("//"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/10a"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("30/2"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1a/10"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("a1/10"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("1/a10"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("29/2/2018"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline("31/4/2018"));
    }

    @Test
    public void constructor_validDeadline_hasCorrectDate() {
        assertEquals(new GregorianCalendar(2018, 9, 1).getTime(), new Deadline("1/10/2018").value);
        assertEquals(new GregorianCalendar(2018, 9, 31).getTime(), new Deadline("31/10/2018").value);
        assertEquals(new GregorianCalendar(2019, 0, 5).getTime(), new Deadline("5/1/2019").value);
        assertEquals(new GregorianCalendar(2019, 0, 5).getTime(), new Deadline("05/1/2019").value);
        assertEquals(new GregorianCalendar(2019, 0, 5).getTime(), new Deadline("05/01/2019").value);
        assertEquals(new GregorianCalendar(2015, 11, 15).getTime(), new Deadline("15/12/2015").value);
        assertEquals(new GregorianCalendar(1, 0, 1).getTime(), new Deadline("1/1/1").value);
        assertEquals(new GregorianCalendar(2, 11, 15).getTime(), new Deadline("15/12/2").value);
        assertEquals(new GregorianCalendar(22, 11, 15).getTime(), new Deadline("15/12/22").value);
        assertEquals(new GregorianCalendar(123, 11, 15).getTime(), new Deadline("15/12/123").value);
        assertEquals(new GregorianCalendar(new GregorianCalendar().get(Calendar.YEAR), 11, 15).getTime(),
            new Deadline("15/12").value);
    }

    @Test
    public void isValidComparison() {
        Deadline a = new Deadline("1/10/2018");
        Deadline b = new Deadline("1/9/2018");
        Deadline c = new Deadline("01/10/2018");
        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));
        assertEquals(0, a.compareTo(c));
    }
}
