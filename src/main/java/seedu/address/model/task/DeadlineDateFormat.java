package seedu.address.model.task;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Date formatter that accepts dates such as 1/10/2018 and 1/10 (year inferred to this year).
 * Two-digit year will be interpreted literally (not in the current century).
 * It is used in the Deadline class for parsing dates.
 */
public class DeadlineDateFormat extends DateFormat {

    private static final char SEPARATOR = '/';

    public DeadlineDateFormat(Locale locale) {
        calendar = new GregorianCalendar(locale);
        calendar.setLenient(false);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        calendar.setTime(date);
        toAppendTo.append(calendar.get(Calendar.DAY_OF_MONTH));
        toAppendTo.append(SEPARATOR);
        toAppendTo.append(calendar.get(Calendar.MONTH) + 1);
        toAppendTo.append(SEPARATOR);
        toAppendTo.append(String.format("%04d", calendar.get(Calendar.YEAR)));
        return toAppendTo;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        try {
            int firstSepIndex = advanceToEndOfNumber(source, pos.getIndex());

            if (firstSepIndex == source.length() || source.charAt(firstSepIndex) != SEPARATOR) {
                // cannot find first separator
                // return an error
                pos.setErrorIndex(firstSepIndex);
                return null;
            }

            int dayInt = Integer.parseUnsignedInt(source, pos.getIndex(), firstSepIndex, 10);
            int secondSepIndex = advanceToEndOfNumber(source, firstSepIndex + 1);
            int monthInt = Integer.parseUnsignedInt(source, firstSepIndex + 1, secondSepIndex, 10) - 1;

            if (secondSepIndex == source.length() || source.charAt(firstSepIndex) != SEPARATOR) {
                // cannot find second separator
                // take the current year
                calendar.setTime(new Date());
                setCalendarDate(dayInt, monthInt);
                pos.setIndex(secondSepIndex);
                return calendar.getTime();
            }

            int thirdSepIndex = advanceToEndOfNumber(source, secondSepIndex + 1);
            int yearInt = Integer.parseUnsignedInt(source, secondSepIndex + 1, thirdSepIndex, 10);

            // use the full year
            calendar.setTime(new Date());
            setCalendarDate(dayInt, monthInt, yearInt);
            pos.setIndex(thirdSepIndex);
            return calendar.getTime();

        } catch (NumberFormatException e) {
            pos.setErrorIndex(pos.getIndex());
            return null;
        }
    }

    /**
     * Traverse the given string until the first non-digit character (i.e. not [0-9]).
     *
     * @param source    The original string.
     * @param currIndex The current position in the string.
     * @return The index of the first non-digit character.
     */
    private int advanceToEndOfNumber(String source, int currIndex) {
        while (currIndex < source.length() && isDecimalDigit(source.charAt(currIndex))) {
            ++currIndex;
        }
        return currIndex;
    }

    /**
     * Set the current Calendar to the given date.
     *
     * @param dayInt   The day of month.
     * @param monthInt The month of year.
     * @param yearInt  The year.
     * @throws NumberFormatException if the day, month, or year is not within the valid range.
     */
    private void setCalendarDate(int dayInt, int monthInt, int yearInt) throws NumberFormatException {
        if (isWithinRange(Calendar.YEAR, yearInt)) {
            calendar.set(Calendar.YEAR, yearInt);
        } else {
            throw new NumberFormatException();
        }

        setCalendarDate(dayInt, monthInt);
    }

    /**
     * Set the current Calendar to the given date, using the current year.
     *
     * @param dayInt   The day of month.
     * @param monthInt The month of year.
     * @throws NumberFormatException if the day or month is not within the valid range.
     */
    private void setCalendarDate(int dayInt, int monthInt) throws NumberFormatException {
        if (isWithinRange(Calendar.MONTH, monthInt)) {
            calendar.set(Calendar.MONTH, monthInt);
        } else {
            throw new NumberFormatException();
        }

        if (isWithinRange(Calendar.DAY_OF_MONTH, dayInt)) {
            calendar.set(Calendar.DAY_OF_MONTH, dayInt);
        } else {
            throw new NumberFormatException();
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Determine if the given value is within the valid range for the given calendar field.
     *
     * @param field The calendar field.
     * @param value The value that we want to determine the validity.
     * @return True if the given value is valid, false otherwise.
     */
    private boolean isWithinRange(int field, int value) {
        int tmp1 = calendar.getActualMinimum(field);
        int tmp2 = calendar.getActualMaximum(field);
        return calendar.getActualMinimum(field) <= value && calendar.getActualMaximum(field) >= value;
    }

    /**
     * Determine whether the given character is one of the digits [0-9].
     *
     * @param ch The given character.
     * @return True if the given character is a digit, false otherwise.
     */
    private boolean isDecimalDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

}
