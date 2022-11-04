package gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import logic.Matrix;
import logic.MatrixCalculations;
import logic.exceptions.*;

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
                "Row Echelon Form",
                "Reduced Row Echelon Form",
                "Invert",
                "Transpose",
                "Diagonalize",
                "Normalize",
                "Adjoint Matrix"
        };
        _scalarResult = 0;
        _operationChoiceBox.getItems().addAll(items);
        _operationChoiceBox.setValue("Reduced Row Echelon Form");

        resetMatrices();
        numericListener(_widthField, _heightField);
    }

    /**
     * force the field to be numeric only
     */
    private static void numericListener(TextField ... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("^(\\d+(\\.\\d+)?)$")) {
                    field.setText(newValue.replaceAll("[^(\\d+(E\\-\\+\\.\\d+)?)$]", ""));
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
            g.getRowConstraints().clear();
            for (int x = 0; x < Integer.parseInt(_widthField.getText()); x++){
                for (int y = 0; y < Integer.parseInt(_heightField.getText()); y++){
                    TextField t = new TextField();
                    g.add(t,x,y);
                    if (g == _resultGrid)
                        t.setEditable(false);
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
            numericListener(f);
        }
    }

    private void updateResult(){
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++)
                set(x,y,Utils.formatDouble(_resultMatrix.get(x,y), false));

        _resultField.setText(Utils.formatDouble(_scalarResult, false));
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
            case "Row Echelon Form" -> rowEchelonForm();
            case "Determinant" -> determinant();
            case "Reduced Row Echelon Form" -> reducedRowEchelonForm();
            case "Invert" -> invert();
            case "Transpose" -> transpose();
            case "Diagonalize" -> diagonalize();
            case "Normalize" -> normalize();
            case "Adjoint Matrix" -> adjoint();
        }
        
        updateResult();
    }

    private void adjoint(){
        try {
            _resultMatrix = MatrixCalculations.adjoint(Matrix.gptom(_leftGrid));
        } catch(NonSquareMatrixException e){
            graphicError(e);
        }
    }

    private void normalize(){
        try {
            _resultMatrix = MatrixCalculations.normalize(Matrix.gptom(_leftGrid));
        } catch(NotAVectorException e){
            graphicError(e);
        }
    }

    private void diagonalize(){
        try {
            _resultMatrix = MatrixCalculations.diagonalize(Matrix.gptom(_leftGrid));
        } catch(NonDiagonalizableMatrixException | NonSquareMatrixException e){
            graphicError(e);
        }
    }

    private void transpose(){
        _resultMatrix = MatrixCalculations.transpose(Matrix.gptom(_leftGrid));
    }

    private void invert() {
        try {
            _resultMatrix = MatrixCalculations.invert(Matrix.gptom(_leftGrid));
        } catch (NonInvertibleMatrixException | NonSquareMatrixException e){
            graphicError(e);
        }
    }

    @FXML
    void dimensionsChanged(){
        resetMatrices();
    }

    @FXML
    void storeResultRight(){
        storeResult(false);
    }

    @FXML
    void storeResultLeft(){
        storeResult(true);
    }

    private void storeResult(boolean isLeft){
        for (int x = 0; x < (isLeft ? _leftGrid : _rightGrid).getColumnCount(); x++)
            for (int y = 0; y < (isLeft ? _leftGrid : _rightGrid).getRowCount(); y++)
                ((TextField) Objects.requireNonNull(Utils.getNodeByCoordinates(x, y, isLeft ? _leftGrid : _rightGrid))).setText(Utils.formatDouble(_resultMatrix.get(x,y), true));
    }

    private void multiply() {
        try {
            _resultMatrix = MatrixCalculations.multiply(Matrix.gptom(_leftGrid),Matrix.gptom(_rightGrid));
        } catch(IncompatibleDimensionsException e){
            graphicError(e);
        }
    }

    private void subtract() {
        _resultMatrix = MatrixCalculations.sub(Matrix.gptom(_leftGrid), Matrix.gptom(_rightGrid));
    }

    private void rowEchelonForm() {
        _resultMatrix = MatrixCalculations.rowEchelonForm(Matrix.gptom(_leftGrid));
    }

    private void reducedRowEchelonForm() {
        _resultMatrix = MatrixCalculations.reducedRowEchelonForm(Matrix.gptom(_leftGrid));
    }

    private void determinant() {
        try {
            _scalarResult = MatrixCalculations.det(Matrix.gptom(_leftGrid));
        } catch(NonSquareMatrixException e){
            graphicError(e);
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

    private void graphicError(MatrixOperationException e){
        Alert a = new Alert(Alert.AlertType.ERROR, "Error");
        a.setHeaderText("Error: bad input for the current operation");
        a.setContentText(e.getMessage());
        a.showAndWait();
    }
}
