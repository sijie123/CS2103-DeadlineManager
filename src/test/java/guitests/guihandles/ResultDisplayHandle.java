package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A handler for the {@code ResultDisplay} of the UI
 */
public class ResultDisplayHandle extends NodeHandle<TextFlow> {

    public static final String RESULT_DISPLAY_ID = "#resultDisplay";

    public ResultDisplayHandle(TextFlow resultDisplayNode) {
        super(resultDisplayNode);
    }

    /**
     * Returns the text in the result display.
     */
    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (Node node : getRootNode().getChildren()) {
            if (node instanceof Text) {
                sb.append(((Text) node).getText());
            }
        }
        return sb.toString();
    }
}
