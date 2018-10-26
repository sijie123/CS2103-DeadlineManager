package seedu.address.logic.parser;

/**
 * A prefix that marks the beginning of an argument in an arguments string. E.g. 't/' in 'add James
 * t/ friend'.
 */
public class Prefix {

    private final String prefix;
    private final boolean hasMultiple;

    public Prefix(String prefix, boolean hasMultiple) {
        this.prefix = prefix;
        this.hasMultiple = hasMultiple;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean canOccurMultipleTimes() {
        return this.hasMultiple;
    }

    public String toString() {
        return getPrefix();
    }

    @Override
    public int hashCode() {
        return prefix == null ? 0 : prefix.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Prefix)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Prefix otherPrefix = (Prefix) obj;
        return otherPrefix.getPrefix().equals(getPrefix()) &&
            otherPrefix.canOccurMultipleTimes() == canOccurMultipleTimes();
    }
}
