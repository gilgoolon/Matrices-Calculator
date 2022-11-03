package logic;

import gui.Utils;
import javafx.scene.layout.GridPane;

import java.util.Arrays;

public class Matrix {
    private final double [][] _matrix;
    private final int _width;
    private final int _height;
    private final boolean _isHomogeneous;

    public Matrix(int width, int height){
        this(width, height, true);
    }

    public Matrix(int width, int height, boolean isHomogeneous){
        _width = width;
        _height = height;
        _isHomogeneous = isHomogeneous;
        _matrix = new double[height][width];

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

    public boolean isHomogeneous(){
        return _isHomogeneous;
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
                m.set(x,y,Utils.getInt(x,y,gp));

        return m;
    }

    public static Matrix identity(int length){
        Matrix m = new Matrix(length, length);

        for (int i = 0; i < length; i++)
            m.set(i, i, 1);

        return m;
    }
}
