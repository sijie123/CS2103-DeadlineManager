package seedu.address.model.task;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class FrequencyTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Frequency((String) null));
    }

    @Test
    public void isValidFrequency_invalidFrequencyInString_returnFalse() {
        assertFalse(Frequency.isValidFrequency(""));
        assertFalse(Frequency.isValidFrequency("-1"));
        assertFalse(Frequency.isValidFrequency("1.0000000"));
        assertFalse(Frequency.isValidFrequency("-1000000000000000"));
        assertFalse(Frequency.isValidFrequency(Frequency.FREQUENCY_LIMIT.toString()));
        assertFalse(Frequency.isValidFrequency(
            Frequency.FREQUENCY_LIMIT.toString()+"0000000000000000000000000000000000000"));
        assertFalse(Frequency.isValidFrequency("abcdefg"));
        assertFalse(Frequency.isValidFrequency("01/01/01"));;
    }

    @Test
    public void isValidFrequency_validFrequencyInString_returnTrue() {
        assertTrue(Frequency.isValidFrequency("0"));
        assertTrue(Frequency.isValidFrequency("1"));
        assertTrue(Frequency.isValidFrequency(((Integer) (Frequency.FREQUENCY_LIMIT - 1)).toString()));
        assertTrue(Frequency.isValidFrequency("0000000000000000000000000000000000000000000000"));
        assertTrue(Frequency.isValidFrequency("0000000000000000000000000000000000000000000123"));
    }

    @Test
    public void isValidFrequency_invalidFrequencyInInt_returnFalse() {
        assertFalse(Frequency.isValidFrequency(-1));
        assertFalse(Frequency.isValidFrequency(Frequency.FREQUENCY_LIMIT));
    }

    @Test
    public void isValidFrequency_validFrequencyInInt_returnTrue() {
        assertTrue(Frequency.isValidFrequency(0));
        assertTrue(Frequency.isValidFrequency(1));
        assertTrue(Frequency.isValidFrequency(Frequency.FREQUENCY_LIMIT-1));
    }
}
