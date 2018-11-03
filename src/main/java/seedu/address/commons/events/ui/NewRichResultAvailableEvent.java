package seedu.address.commons.events.ui;

import java.util.List;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.ResultDisplay;

/**
 * Indicates that a new rich result is available.
 */
public class NewRichResultAvailableEvent extends BaseEvent {

    public final List<ResultDisplay.StyledText> message;

    public NewRichResultAvailableEvent(List<ResultDisplay.StyledText> message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
