package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTaskCollection;

/**
 * Indicates the TaskCollection in the model has changed
 */
public class AddressBookChangedEvent extends BaseEvent {

    public final ReadOnlyTaskCollection data;

    public AddressBookChangedEvent(ReadOnlyTaskCollection data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getTaskList().size();
    }
}
