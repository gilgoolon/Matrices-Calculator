package logic;

import logic.exceptions.*;

import java.util.Arrays;
import java.util.Iterator;

public class MatrixCalculations {

    /**
     * calculate the determinant of a given matrix
     * @param m represents the matrix operand
     * @return the result of |m|
     */
    public static double det(Matrix m) throws NonSquareMatrixException {
        if (m.getWidth() != m.getHeight())
            throw new NonSquareMatrixException();

        m = MatrixCalculations.rowEchelonForm(m);
        double result = m.getExtraValue();
        for (int i = 0; i < m.getHeight(); i++)
            result *= m.get(i,i);

        return result;
    }

    /**
     * calculate the row echelon form of a given matrix
     * @param m represents the matrix operand
     * @return the row echelon form of m
     */
    public static Matrix rowEchelonForm(Matrix m){
        // Using algorithm from file "Algorithm REF" at C:\Users\alper\Downloads\Algorithm REF.pdf

        // Step 1. Begin with an m × n matrix A. If A = 0, go to Step 7
        Matrix result = new Matrix(m);

        if (result.isZero())
            return result;

        for (int pivotRow = 0; pivotRow < result.getHeight(); pivotRow++) {
            // Step 2. Determine the leftmost non-zero column
            int leftmostNonZero = 0;
            int newRow = -1;
            for (;leftmostNonZero < result.getWidth(); leftmostNonZero++)
                if ((newRow = isNonZeroCol(result, leftmostNonZero, pivotRow)) != -1)
                    break;

            // if there are no more nonzero rows to the bottom, we are done
            if (newRow == -1)
                break;

            if (newRow != pivotRow){
                result = MatrixCalculations.elementaryRowOperation3(result,newRow,pivotRow);
                result.setExtraValue(result.getExtraValue()*-1);
            }

            if (leftmostNonZero == result.getWidth())
                break;

            // Step 3. Use elementary row operations to put a 1 in the topmost position
            // (we call this position pivot position) of this column

            // store the multiple aside for possible determinant usage
            result.setExtraValue(result.getExtraValue() * result.get(pivotRow, leftmostNonZero));

            result = MatrixCalculations.elementaryRowOperation1(result, pivotRow, 1 / result.get(pivotRow, leftmostNonZero));

            // Step 4. Use elementary row operations to put zeros (strictly) below the pivot position
            for (int row = pivotRow + 1; row < result.getHeight(); row++)
                result = MatrixCalculations.elementaryRowOperation2(result, row, pivotRow, -result.get(leftmostNonZero, row));

            // Step 5. Apply Step 2-4 to the sub-matrix consisting of the rows that lie
            // (strictly below) the pivot position.
        }
        return result;
    }

    // helper function to check if a column is nonzero from the given indices and onwards
    private static int isNonZeroCol(Matrix m, int x, int y){
        for (int i = y; i < m.getHeight(); i++)
            if (m.get(x,i) != 0)
                return i;
        return -1;
    }

    /**
     * operation 1 := multiple * rowToChange
     * @param m represents the matrix to perform the operation on
     * @param rowToChange represents the row to change (perform the operation on)
     * @param multiple represents the multiple of the row
     * @return the resulting matrix after performed operation 1
     */
    public static Matrix elementaryRowOperation1(Matrix m, int rowToChange, double multiple){
        Matrix result = new Matrix(m);

        for (int x = 0; x < m.getWidth(); x++)
            result.set(x, rowToChange, result.get(x,rowToChange) * multiple);

        return result;
    }

    /**
     * operation 2 := rowToChange + multiple * rowToUse
     * @param m represents the matrix to perform the operation on
     * @param rowToChange represents the row to change (perform the operation on)
     * @param rowToUse represents the row to add (times the multiple) to rowToChange
     * @param multiple represents the multiple of rowToUse
     * @return the resulting matrix after performed operation 2
     */
    public static Matrix elementaryRowOperation2(Matrix m, int rowToChange, int rowToUse, double multiple){
        Matrix result = new Matrix(m);

        for (int x = 0; x < result.getWidth(); x++)
            result.set(x, rowToChange, result.get(x, rowToChange) + multiple * result.get(x, rowToUse));

        return result;
    }

    /**
     * operation 3 := r1 <-> r2
     * @param m represents the matrix to perform the operation on
     * @param r1 represents the first row to switch
     * @param r2 represents the first row to switch
     * @return the resulting matrix after performed operation 3
     */
    public static Matrix elementaryRowOperation3(Matrix m, int r1, int r2){
        Matrix result = new Matrix(m);

        for (int x = 0; x < result.getWidth(); x++) {
            double temp = result.get(x,r1);
            result.set(x,r1,result.get(x,r2));
            result.set(x,r2,temp);
        }

        return result;
    }

    /**
     * calculate the reduced row echelon form of a given matrix
     * @param m represents the matrix operand
     * @return the reduced row echelon form of m
     */
    public static Matrix reducedRowEchelonForm(Matrix m){
        // Using algorithm from file "Algorithm REF" at C:\Users\alper\Downloads\Algorithm REF.pdf

        // Step 1. reduce the given matrix to a row echelon form
        Matrix result = MatrixCalculations.rowEchelonForm(m);

        // Step 2. Determine the right most column containing a leading one (we call
        // this column pivot column)
        int pivotCol = result.getWidth() - 1;
        int pivotRow;
        for (; pivotCol >= 0; pivotCol--) {
            if ((pivotRow = isLeadingOne(result, pivotCol)) != -1) {
                // Step 3. Use type II elementary row operations to erase all the non-zero
                // entries above the leading one in the pivot column.
                for (int i = pivotRow - 1; i >= 0; i--)
                    result = MatrixCalculations.elementaryRowOperation2(result, i, pivotRow, -result.get(pivotCol, i));
            }
            // Step 4. Apply Step 2-3 to the sub-matrix consisting of the columns that lie
            // to the left of the pivot column
        }

        return result;
    }

    /**
     * check if the given column has a leading one
     * @param m represents the given matrix
     * @param col represents the column being checked for having a leading one
     * @return -1 if the given column does not contain a leading one,
     * and the row index of the one if it does contain a leading one
     */
    private static int isLeadingOne(Matrix m, int col){
        for (int i = m.getHeight() - 1; i >= 0; i--){
            if (m.get(col, i) != 0){
                for (int x = col-1; x >= 0; x--)
                    if (m.get(x, i) != 0)
                        return -1;
                return i;
            }
        }
        return -1;
    }

    /**
     * calculate the normalized form of a given vector
     * @param m represents the vector operand
     * @return the normalized vector |m|
     * @throws NotAVectorException in case m isn't a vector (height and width are neither 1)
     */
    public static Matrix normalize(Matrix m) throws NotAVectorException {
        Iterator<Double> it = m.iterator();
        double sum = 0;
        while (it.hasNext())
            sum += Math.pow(it.next(), 2);

        double norm = Math.sqrt(sum);
        Matrix result = new Matrix(m.getWidth(), m.getHeight());

        boolean isWide = m.getWidth() != 1;

        it = m.iterator();
        for (int i = 0; i < (isWide ? m.getWidth() : m.getHeight()); i++)
            result.set(isWide ? i : 0, isWide ? 0 : i, it.next()/norm);

        return result;
    }

    /**
     * calculate the result of addition between two matrices
     * @param m1 represents the left (first) operand
     * @param m2 represents the right (second) operand
     * @return the result of m1+m2
     */
    public static Matrix add(Matrix m1, Matrix m2){
        Matrix m = new Matrix(m1.getWidth(), m1.getHeight());

        for (int x = 0; x < m1.getWidth(); x++) {
            for (int y = 0; y < m1.getHeight(); y++) {
                m.set(x,y,m1.get(x,y) + m2.get(x,y));
            }
        }

        return m;
    }

    /**
     * calculate the result of subtraction between two matrices
     * @param m1 represents the left (first) operand
     * @param m2 represents the right (second) operand
     * @return the result of m1-m2
     */
    public static Matrix sub(Matrix m1, Matrix m2){
        Matrix m = new Matrix(m1.getWidth(), m1.getHeight());

        for (int x = 0; x < m1.getWidth(); x++) {
            for (int y = 0; y < m1.getHeight(); y++) {
                m.set(x,y,m1.get(x,y) - m2.get(x,y));
            }
        }

        return m;
    }

    /**
     * calculate the inverted matrix of m, meaning the matrix p such that mp = pm = I
     * @param m represents the matrix operand
     * @return the result of m^-1
     * @throws NonSquareMatrixException in case the matrix isn't squared and therefore non-invertible
     * @throws NonInvertibleMatrixException in case the matrix is squared but is non-invertible (|m| = 0)
     */
    public static Matrix invert(Matrix m) throws NonSquareMatrixException, NonInvertibleMatrixException {
        double det = MatrixCalculations.det(m);
        if (det == 0)
            throw new NonInvertibleMatrixException();

        return multiply(MatrixCalculations.adjoint(m), 1/det);
    }

    public static Matrix adjoint(Matrix m) throws NonSquareMatrixException {
        Matrix result = new Matrix(m);
        for (int x = 0; x < result.getWidth(); x++)
            for (int y = 0; y < result.getHeight(); y++)
                result.set(x, y, Math.pow(-1, x + y) * MatrixCalculations.minor(m, x, y));

        return MatrixCalculations.transpose(result);
    }

    /**
     * calculate the determinant of a minor in coordinate (x,y)
     * @param m represents the matrix operand
     * @param x represents the x coordinate
     * @param y represents the y coordinate
     * @return the determinant of the minor (x,y)
     * @throws NonSquareMatrixException in case the matrix is not squared and therefore cannot be minored
     */
    private static double minor(Matrix m, int x, int y) throws NonSquareMatrixException{
        Matrix result = new Matrix(m.getWidth() - 1, m.getHeight() - 1);

        int offsetX = 0;
        for (int i = 0; i < m.getWidth(); i++) {
            int offsetY = 0;
            if (i != x) {
                for (int j = 0; j < m.getHeight(); j++) {
                    if (j != y){
                        result.set(i - offsetX, j - offsetY, m.get(i, j));
                    } else offsetY = 1;
                }
            } else offsetX = 1;
        }

        return MatrixCalculations.det(result);
    }

    /**
     * calculate the transposed matrix of a given matrix
     * @param m represents the matrix operand
     * @return the result of m^t
     */
    public static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.getHeight(), m.getWidth());

        for (int i = 0; i < m.getWidth(); i++)
            for (int j = 0; j < m.getHeight(); j++)
                result.set(j, i, m.get(i, j));

        return result;
    }

    /**
     * Calculate the multiplication of a given matrix with a scalar
     * @param m represents the matrix operand
     * @param scalar represents the scalar operand
     * @return the result of c*m
     */
    public static Matrix multiply(Matrix m, double scalar){
        Matrix result = new Matrix(m.getWidth(), m.getHeight());

        for (int x = 0; x < m.getWidth(); x++)
            for (int y = 0; y < m.getHeight(); y++)
                result.set(x, y, m.get(x, y) * scalar);

        return result;
    }

    /**
     * calculate the result of multiplication between two matrices
     * @param m1 represents the left (first) operand
     * @param m2 represents the right (second) operand
     * @return the result of m1*m2
     */
    public static Matrix multiply(Matrix m1, Matrix m2) throws IncompatibleDimensionsException {
        if (m1.getWidth() != m2.getHeight())
            throw new IncompatibleDimensionsException();

        Matrix result = new Matrix(m2.getWidth(), m1.getHeight());

        for (int x = 0; x < m2.getWidth(); x++) {
            for (int y = 0; y < m1.getHeight(); y++) {
                double sum = 0;
                for (int i = 0; i < m1.getWidth(); i++)
                    sum += m1.get(i,y)*m2.get(x,i);

                result.set(x, y, sum);
            }
        }

        return result;
    }

    //TODO: Write diagonalize()
    /**
     * calculates the similar diagonal matrix of m
     * @param m represents the matrix operand
     * @return the diagonal matrix x similar to m such that exists an invertible matrix p that satisfies x = p*m*p^-1
     * @throws NonSquareMatrixException in case the matrix isn't squared and therefore indiagonalizable
     * @throws NonDiagonalizableMatrixException in case the matrix is squared but indiagonalizable (רא != רג)
     */
    public static Matrix diagonalize(Matrix m) throws NonSquareMatrixException, NonDiagonalizableMatrixException {
        if (m.getWidth() != m.getHeight())
            throw new NonSquareMatrixException();

        Matrix result = new Matrix(m.getWidth(),m.getHeight());
        // random stuff to make the code compile without errors
        result.set(0,0,0);
        if (result.get(0,0) == 0)
            throw new NonDiagonalizableMatrixException();
        return result;
    }
}
