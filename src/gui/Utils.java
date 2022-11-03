package gui;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class Utils {
    public static Node getNodeByCoordinates(final int x, final int y, final GridPane gp){
        ObservableList<Node> children = gp.getChildren();
        for (Node n : children)
            if (GridPane.getColumnIndex(n).equals(x) && GridPane.getRowIndex(n).equals(y))
                return n;

        return null;
    }

    public static double getInt(final int x, final int y, final GridPane gp){
        return Double.parseDouble(((TextField) Objects.requireNonNull(Utils.getNodeByCoordinates(x, y, gp))).getText());
    }
}
