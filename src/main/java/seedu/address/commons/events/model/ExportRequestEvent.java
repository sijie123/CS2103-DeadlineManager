package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTaskCollection;

/**
 * Indicates a request to export the current taskcollection view.
 */
public class ExportRequestEvent extends BaseEvent {

    public final ReadOnlyTaskCollection data;
    public final String filename;
    public final boolean overwrite;
    public final boolean isCsvFormat;

    public ExportRequestEvent(ReadOnlyTaskCollection data, String filename, boolean overwrite, boolean isCsvFormat) {
        this.data = data;
        this.filename = filename;
        this.overwrite = overwrite;
        this.isCsvFormat = isCsvFormat;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size();
    }
}
