package logic;

import gui.Utils;
import javafx.scene.layout.GridPane;
import logic.exceptions.NotAVectorException;

import java.util.Arrays;
import java.util.Iterator;

public class Matrix {
    private final double [][] _matrix;
    private final int _width;
    private final int _height;
    private final boolean _isHomogeneous;
    private double _extraValue;

    public Matrix(Matrix m){
        this(m.getWidth(), m.getHeight(), m.isHomogeneous());
        for (int x = 0; x < _width; x++)
            for (int y = 0; y < _height; y++)
                _matrix[x][y] = m.get(x,y);
        _extraValue = m.getExtraValue();
    }

    public Matrix(int width, int height){
        this(width, height, true);
    }

    public Matrix(int width, int height, boolean isHomogeneous){
        _width = width;
        _height = height;
        _isHomogeneous = isHomogeneous;
        _matrix = new double[width][height];
        _extraValue = 1;

        for (double[] a : _matrix)
            Arrays.fill(a,0);
    }

    public double get(int x, int y){
        return _matrix[x][y];
    }

    public void set(int x, int y, double val){
        _matrix[x][y] = val;
    }

    public int getWidth(){
        return _width;
    }

    public int getHeight(){
        return _height;
    }

    public void setExtraValue(double x){
        _extraValue = x;
    }

    public double getExtraValue(){
        return _extraValue;
    }

    public boolean isHomogeneous(){
        return _isHomogeneous;
    }

    public boolean isZero(){
        for (int x = 0; x < _width; x++)
            for (int y = 0; y < _height; y++)
                if (_matrix[x][y] != 0)
                    return false;
        return true;
    }

    /**
     * get an iterator for the values of a vector
     * @return an iterator that goes through the values in order
     * @throws NotAVectorException in case the given matrix is not a vector
     */
    public Iterator<Double> iterator() throws NotAVectorException {
        if (_width != 1 && _height != 1)
            throw new NotAVectorException();

        boolean isWide = _width != 1;

        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < (isWide ? _width : _height);
            }

            @Override
            public Double next() {
                return _matrix[isWide ? i++ : 0][isWide ? 0 : i++];
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix m){
            if (_width != m.getWidth() || _height != m.getHeight())
                return false;

            for (int x = 0; x < _height; x++)
                for (int y = 0; y < _width; y++)
                    if (_matrix[x][y] != m.get(x,y))
                        return false;

            return true;
        }
        return false;
    }

    public static Matrix gptom(GridPane gp){
        int width = gp.getColumnCount();
        int height = gp.getRowCount();

        Matrix m = new Matrix(width,height);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                m.set(x,y,Utils.getDouble(x,y,gp));

        return m;
    }
}
