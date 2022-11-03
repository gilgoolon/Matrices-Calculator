package gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import logic.Matrix;
import logic.MatrixCalculations;
import logic.exceptions.IncompatibleDimensionsException;
import logic.exceptions.NonDiagonalizableMatrixException;
import logic.exceptions.NonInvertibleMatrixException;
import logic.exceptions.NonSquareMatrixException;

import java.util.Objects;

public class Controller {

    @FXML
    public TextField _resultField;

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

    private Matrix _resultMatrix;
    private double _scalarResult;

    public void init(){
        String[] items = {
                "Add",
                "Subtract",
                "Multiply",
                "Determinant",
                "Canonicalize",
                "Invert",
                "Transpose",
                "Diagonalize"
        };
        _scalarResult = 0;
        _operationChoiceBox.getItems().addAll(items);
        _operationChoiceBox.setValue("Add");

        resetMatrices();
        numericListener(_widthField, _heightField);
    }

    /**
     * force the field to be numeric only
     */
    private static void numericListener(TextField ... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
    }

    private void resetMatrices(){
        formatGridPanes(_leftGrid,_resultGrid,_rightGrid);
        _resultMatrix = new Matrix(getWidth(),getHeight());
    }

    private void formatGridPanes(GridPane...grids){
        for (GridPane g : grids){
            g.getChildren().clear();
            g.getColumnConstraints().clear();
            for (int x = 0; x < Integer.parseInt(_widthField.getText()); x++){
                for (int y = 0; y < Integer.parseInt(_heightField.getText()); y++){
                    TextField t = new TextField();
                    g.add(t,x,y);
                    formatTextFields(t);
                }
            }
            g.setGridLinesVisible(true);
        }
    }

    private void formatTextFields(TextField...fields){
        for(TextField f : fields){
            f.setAlignment(Pos.CENTER);
            f.setText("0");
            if (f.getParent() == _resultGrid)
                f.setEditable(false);
            else numericListener(f);
        }
    }

    private void updateResult(){
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++)
                set(x,y,Double.toString(_resultMatrix.get(x,y)));

        _resultField.setText(Double.toString(_scalarResult));
    }

    private void set(final int x, final int y, String s){
        try {
            ((TextField) Objects.requireNonNull(Utils.getNodeByCoordinates(x, y, _resultGrid))).setText(s);
        } catch(ClassCastException | NullPointerException e){
            e.printStackTrace();
        }
    }

    @FXML
    void executeOperation(){
        switch (_operationChoiceBox.getValue()) {
            case "Add" -> add();
            case "Subtract" -> subtract();
            case "Multiply" -> multiply();
            case "Canonicalize" -> canonicalize();
            case "Determinant" -> determinant();
            case "Invert" -> invert();
            case "Transpose" -> transpose();
            case "Diagonalize" -> diagonalize();
        }
        
        updateResult();
    }

    private void diagonalize(){
        try {
            _resultMatrix = MatrixCalculations.diagonalize(Matrix.gptom(_leftGrid));
        } catch(NonDiagonalizableMatrixException | NonSquareMatrixException e){
            e.printStackTrace();
        }
    }

    private void transpose(){
        _resultMatrix = MatrixCalculations.transpose(Matrix.gptom(_leftGrid));
    }

    private void invert() {
        try {
            _resultMatrix = MatrixCalculations.invert(Matrix.gptom(_leftGrid));
        } catch (NonInvertibleMatrixException | NonSquareMatrixException e){
            e.printStackTrace();
        }
    }

    @FXML
    void dimensionsChanged(){
        resetMatrices();
    }

    private void multiply() {
        try {
            _resultMatrix = MatrixCalculations.multiply(Matrix.gptom(_leftGrid),Matrix.gptom(_rightGrid));
        } catch(IncompatibleDimensionsException e){
            e.printStackTrace();
        }
    }

    private void subtract() {
        _resultMatrix = MatrixCalculations.sub(Matrix.gptom(_leftGrid), Matrix.gptom(_rightGrid));
    }

    private void canonicalize() {
        _resultMatrix = MatrixCalculations.canonicalize(Matrix.gptom(_leftGrid));
    }

    private void determinant() {
        try {
            _scalarResult = MatrixCalculations.det(Matrix.gptom(_leftGrid));
        } catch(NonSquareMatrixException e){
            e.printStackTrace();
        }
    }

    private void add() {
        _resultMatrix = MatrixCalculations.add(Matrix.gptom(_leftGrid), Matrix.gptom(_rightGrid));
    }

    private int getWidth(){
        return Integer.parseInt(_widthField.getText());
    }

    private int getHeight(){
        return Integer.parseInt(_heightField.getText());
    }
}
