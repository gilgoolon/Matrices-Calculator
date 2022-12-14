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

    public static double getDouble(final int x, final int y, final GridPane gp){
        return Double.parseDouble(((TextField) Objects.requireNonNull(Utils.getNodeByCoordinates(x, y, gp))).getText());
    }

    public static String formatDouble(double x, boolean isData){
        if (!isData)
            return Double.toString(Math.round(x*1000)/1000.0);
        else return Double.toString(x);
    }
}
