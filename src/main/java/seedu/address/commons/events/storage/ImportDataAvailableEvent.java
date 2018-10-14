package seedu.address.commons.events.storage;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTaskCollection;

/**
 * Indicates that a new result is available.
 */
public class ImportDataAvailableEvent extends BaseEvent {

    public final ReadOnlyTaskCollection data;

    public ImportDataAvailableEvent(ReadOnlyTaskCollection data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
