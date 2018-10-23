package seedu.address.commons.events.ui;

import java.io.File;
import java.util.function.Consumer;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to prompt the user to select a location to save a file
 */
public class SelectFileSaveEvent extends BaseEvent {

    public final Consumer<File> fileReceiver;

    public SelectFileSaveEvent(Consumer<File> fileReceiver) {
        this.fileReceiver = fileReceiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
