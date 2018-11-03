package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "ResultDisplay.fxml";
    private static final String DEFAULT_TEXT_STYLE_CLASS = "result-display-text";

    private final ObservableList<Node> displayed;

    @FXML
    private TextFlow resultDisplay;

    public ResultDisplay() {
        super(FXML);
        displayed = resultDisplay.getChildren();
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> {
            displayed.clear();
            Text text = new Text(event.message);
            text.getStyleClass().add(DEFAULT_TEXT_STYLE_CLASS);
            displayed.add(text);
        });
    }

}
