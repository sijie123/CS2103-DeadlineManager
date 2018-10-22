package seedu.address.commons.events.ui;

import java.io.File;
import java.util.function.Consumer;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that the command specified by commandText should be executed
 */
public class ExecuteCommandEvent extends BaseEvent {

    public final String commandText;

    public ExecuteCommandEvent(String commandText) {
        this.commandText = commandText;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + commandText;
    }

}
