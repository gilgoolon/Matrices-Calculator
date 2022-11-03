package logic;

import logic.exceptions.IncompatibleDimensionsException;
import logic.exceptions.NonDiagonalizableMatrixException;
import logic.exceptions.NonInvertibleMatrixException;
import logic.exceptions.NonSquareMatrixException;

public class MatrixCalculations {

    /**
     * calculate the determinant of a given matrix
     * @param m represents the matrix operand
     * @return the result of |m|
     */
    public static double det(Matrix m) throws NonSquareMatrixException {
        if (m.getWidth() != m.getHeight())
            throw new NonSquareMatrixException();

        m = MatrixCalculations.canonicalize(m);
        double result = 1;
        for (int i = 0; i < m.getHeight(); i++)
            result *= m.get(i,i);

        return result;
    }

    //TODO: Write canonicalize() and everything else is smooth sailing
    /**
     * calculate the canonical form of a given matrix
     * @param m represents the matrix operand
     * @return the canonical form of m
     */
    public static Matrix canonicalize(Matrix m){
        Matrix ans = new Matrix(m.getWidth(), m.getHeight());
        ans.set(0,0,1);

        return ans;
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

        return multiply(MatrixCalculations.transpose(m), 1/det);
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

    //TODO: Write diagonalize(), but dependent on some other function which are needed to be written
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
