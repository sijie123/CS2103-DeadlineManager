package seedu.address.commons.events.ui;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.ResultDisplay;

/**
 * Indicates that a new rich result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final List<ResultDisplay.StyledText> message;

    public NewResultAvailableEvent(String message) {
        this.message = new ArrayList<>();
        this.message.add(new ResultDisplay.StyledText(message, ResultDisplay.TEXT_STYLE_CLASS_DEFAULT));
    }

    public NewResultAvailableEvent(List<ResultDisplay.StyledText> message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
