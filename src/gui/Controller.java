package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private TextField _heightField;

    @FXML
    private GridPane _resultGrid;

    @FXML
    private GridPane _leftGrid;

    @FXML
    private ChoiceBox<String> _operationChoiceBox;

    @FXML
    private GridPane _rightGrid;

    @FXML
    private TextField _widthField;

    @FXML
    void initialize(){

    }

    public void init(){
        // force the field to be numeric only
        numericListener(_widthField, _heightField);

        _operationChoiceBox.getItems().addAll("Add", "Subtract", "Multiply", "Determinant", "Canonicalize");
        _operationChoiceBox.setValue("Add");
    }

    private void numericListener(TextField ... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

    private void resetMatrices(){
        _leftGrid.getChildren().clear();
        _rightGrid.getChildren().clear();
        for (int x = 0; x < Integer.parseInt(_widthField.getText()); x++){
            for (int y = 0; y < Integer.parseInt(_heightField.getText()); y++){
                TextField left = new TextField();
                TextField right = new TextField();

                // force the field to be numeric only
                numericListener(left,right);
                _leftGrid.add(left,x,y);
                _rightGrid.add(right,x,y);
            }
        }
    }
    private void updateComponents(){

    }

    @FXML
    void executeOperation(){
        switch (_operationChoiceBox.getValue()) {
            case "Add" -> add();
            case "Subtract" -> sub();
            case "Multiply" -> mult();
            case "Canonicalize" -> canon();
            case "Determinant" -> det();
        }
        
        updateComponents();
    }

    @FXML
    private void onKeyTyped(KeyEvent event){

    }
    private void mult() {
        
    }

    private void sub() {
        
    }

    private void canon() {
        
    }

    private void det() {
        
    }

    private void add() {
        
    }
}
