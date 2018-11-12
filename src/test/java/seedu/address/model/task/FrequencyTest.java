package seedu.address.model.task;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class FrequencyTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Frequency((String) null));
    }

    @Test
    public void constructor_success_returnFrequency() {
        assertEquals(new Frequency("0").value, new Frequency(0).value);
        assertEquals(new Frequency(((Integer) (Frequency.FREQUENCY_LIMIT - 1)).toString()).value,
                new Frequency(Frequency.FREQUENCY_LIMIT - 1).value);
    }

    @Test
    public void isValidFrequency_invalidFrequencyInString_returnFalse() {
        assertFalse(Frequency.isValidFrequency(""));
        assertFalse(Frequency.isValidFrequency("-1"));
        assertFalse(Frequency.isValidFrequency("1.0000000"));
        assertFalse(Frequency.isValidFrequency("-1000000000000000"));
        assertFalse(Frequency.isValidFrequency(Frequency.FREQUENCY_LIMIT.toString()));
        assertFalse(Frequency.isValidFrequency(
                Frequency.FREQUENCY_LIMIT.toString() + "0000000000000000000000000000000000000"));
        assertFalse(Frequency.isValidFrequency("abcdefg"));
        assertFalse(Frequency.isValidFrequency("01/01/01"));
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
        assertTrue(Frequency.isValidFrequency(Frequency.FREQUENCY_LIMIT - 1));
    }

    @Test
    public void isZero_frequencyZero_returnTrue() {
        assertTrue(new Frequency(0).isZero());
    }

    @Test
    public void isZero_frequencyOne_returnFalse() {
        assertFalse(new Frequency(1).isZero());
    }

    @Test
    public void hashCode_sameFrequency() {
        assertEquals(new Frequency("0").hashCode(), new Frequency(0).hashCode());
        assertEquals(new Frequency(((Integer) (Frequency.FREQUENCY_LIMIT - 1)).toString()).hashCode(),
                new Frequency(Frequency.FREQUENCY_LIMIT - 1).hashCode());
    }

    @Test
    public void compareTo_differentValue() {
        Frequency nonRepeat = new Frequency(0); //Frequency 0 is as good as 'infinite' frequency
        Frequency repeat = new Frequency(1);
        assertTrue(nonRepeat.compareTo(repeat) > 0);
        assertTrue(repeat.compareTo(nonRepeat) < 0);
        if (Frequency.FREQUENCY_LIMIT > 2) {
            Frequency repeatMax = new Frequency(Frequency.FREQUENCY_LIMIT - 1);
            assertTrue(nonRepeat.compareTo(repeatMax) > 0);
            assertTrue(repeatMax.compareTo(nonRepeat) < 0);
            assertTrue(repeat.compareTo(repeatMax) < 0);
            assertTrue(repeatMax.compareTo(repeat) > 0);
        }
    }
}
