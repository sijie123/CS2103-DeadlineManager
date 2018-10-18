package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTaskCollection;

/**
 * Indicates a request to export the current taskcollection view.
 */
public class ExportRequestEvent extends BaseEvent {

    public final ReadOnlyTaskCollection data;
    public final String filename;

    public ExportRequestEvent(ReadOnlyTaskCollection data, String filename) {
        this.data = data;
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size();
    }
}
