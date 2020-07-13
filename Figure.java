package RogSimpsonTetrisData;

import java.util.Random;

public class Figure {

    protected enum SquareForms { CleanFigure, ZFigure, ZReverseFigure, QuarterLineFigure,
        TFigure, SquareFigure, LFigure, LReverseFigure, 
        PFigure, ZBigFigure, ZReverseBigFigure, DoubleLineFigure, CFigure, CornerFigure, PlusFigure, CornerBigFigure, SingleFigure, TriangleFigure, TripleLineFigure, RectangleFigure, ManFigure }

    private SquareForms figure;
    private int coords[][];
    private int[][][] coordsTable;
    
    public Figure() {
    	
        initFigure();
    }

    private void initFigure() {
    	
        coords = new int[6][2];

        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } }, //CleanFigure
                { { 0, -1},   { 0, 0 },   {-1, 0 },   {-1, 1 },   { 0, 0 },   { 0, 0 } }, //ZFigure
                { { 0, -1},   { 0, 0 },   { 1, 0 },   { 1, 1 },   { 0, 0 },   { 0, 0 } }, //ZReverseFigure
                { { -1, 0},   { 0, 0 },   { 1, 0 },   { 2, 0 },   { 0, 0 },   { 0, 0 } }, //QuarterLineFigure
                { { -1, 0},   { 0, 0 },   { 1, 0 },   { 0, 1 },   { 0, 0 },   { 0, 0 } }, //TFigure
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 },   { 0, 0 },   { 0, 0 } }, //SquareFigure
                { {-1, -1},   { 0, -1},   { 0, 0 },   { 0, 1 },   { 0, 0 },   { 0, 0 } }, //LFigure
                { { 1, -1},   { 0, -1},   { 0, 0 },   { 0, 1 },   { 0, 0 },   { 0, 0 } }, //LReverseFigure
                { { 0, -1},   { 1, 0 },   { 0, 1 },   { 1, 1 },   { 0, 0 },   { 0, 0 } }, //SquareFigure
                { { -1, 1},   { 0, 1 },   { 0, 0 },   { 0, -1},   { 1, -1},   { 0, 0 } }, //ZBigFigure
                { { 1, 1 },   { 0, 1 },   { 0, 0 },   { 0, -1},   {-1, -1},   { 0, 0 } }, //ZReverseBigFigure
                { { 0, 0 },   { 1, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } }, //DoubleLineFigure
                { { -1, 1},   { 0, 1 },   { 0, 0 },   { 0, -1},   {-1, -1},   { 0, 0 } }, //CFigure
                { { 0, 0 },   { 1, 0 },   { 1, 1 },   { 0, 0 },   { 0, 0 },   { 0, 0 } }, //CornerFigure
                { { -1, 0},   { 0, -1},   { 0, 0 },   { 0, 1 },   { 1, 0 },   { 0, 0 } }, //PlusFigure
                { { -1, 1},   { -1, 0},   {-1, -1},   { 0, -1},   { 1, -1},   { 0, -1} }, //CornerBigFigure
                { { 1, 1 },   { 1, 1 },   { 1, 1 },   { 1, 1 },   { 1, 1 },   { 1, 1 } }, //SingleFigure
                { { -1, 1},   { -1, 0},   {-1, -1},   { 0, -1},   { 1, -1},   { 0, 0 } }, //TriangleFigure
                { { 0, 0 },   { 1, 0 },   { 2, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } }, //TripleLineFigure
                { {-1, -1},   { -1, 0},   { 0, -1},   { 0, 0 },   { 1, -1},   { 1, 0 } }, //RectangleFigure
                { { 0, 1 },   { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } }  //ManFigure
          
        };

        setFigure(SquareForms.CleanFigure);

   }

    protected void setFigure(SquareForms squareForm) {

        for (int i = 0; i < 6 ; i++) {

            for (int j = 0; j < 2; ++j) {

                coords[i][j] = coordsTable[squareForm.ordinal()][i][j];
            }
        }

        figure = squareForm;
    }
    
    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public SquareForms getFigure()  { return figure; }

   public SquareForms getRandomFigure(int number) {

        var r = new Random();
        int x = Math.abs(r.nextInt()) % number + 1; 

        SquareForms[] values = SquareForms.values();
        SquareForms squareForm = values[x];
        return squareForm;
        
    }
    
       
    public int minX() {

        int m = coords[0][0];

        for (int i=0; i < 6; i++) {

            m = Math.min(m, coords[i][0]);
        }

        return m;
    }


    public int minY() {

        int m = coords[0][1];

        for (int i=0; i < 6; i++) {

            m = Math.min(m, coords[i][1]);
        }

        return m;
    }

    public Figure rotateLeft() {

        if (figure == SquareForms.SquareFigure
        		|| figure == SquareForms.PlusFigure || figure == SquareForms.SingleFigure) {

            return this;
        }

        var result = new Figure();
        result.figure = figure;

        for (int i = 0; i < 6; ++i) {

            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    public Figure rotateRight() {

        if (figure == SquareForms.SquareFigure
        		|| figure == SquareForms.PlusFigure || figure == SquareForms.SingleFigure) {

            return this;
        }

        var result = new Figure();
        result.figure = figure;

        for (int i = 0; i < 6; ++i) {

            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }
    
    public Figure reflect() {

    	 if (figure == SquareForms.SquareFigure || figure == SquareForms.PlusFigure
    			 || figure == SquareForms.SingleFigure || figure == SquareForms.QuarterLineFigure
    			 || figure == SquareForms.DoubleLineFigure || figure == SquareForms.RectangleFigure) {

             return this;
         }

        var result = new Figure();
        result.figure = figure;

        for (int i = 0; i < 6; ++i) {

            result.setX(i, -x(i));
            result.setY(i, y(i));
        }

        return result;
    }
    
}

