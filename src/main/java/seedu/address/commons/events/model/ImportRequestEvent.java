package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to import to the current model.
 */
public class ImportRequestEvent extends BaseEvent {

    public final String filename;

    public ImportRequestEvent(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "importing from " + filename;
    }
}
