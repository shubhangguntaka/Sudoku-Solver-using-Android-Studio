package com.ss.sudoku2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private final int boardcolor;
    private final int cellFillColor;
    private final int cellsHighlightColor;
    private final int letterColor;
    private final int letterColorsolve;

    private final Paint boardcolorPaint=new Paint();
    private final Paint cellFillColorPaint=new Paint();
    private final Paint cellsHighlightColorPaint=new Paint();

    private final Paint letterPaint=new Paint();
    private final Rect letterPaintBounds=new Rect();


    private int cellSize;

    private final Solver solver=new Solver();
    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a=context.getTheme().obtainStyledAttributes(attrs,R.styleable.SudokuBoard,
                0,0);
        try{
            boardcolor=a.getInteger(R.styleable.SudokuBoard_boardcolor,0);
            cellFillColor=a.getInteger(R.styleable.SudokuBoard_cellFillColor,0);
            cellsHighlightColor=a.getInteger(R.styleable.SudokuBoard_cellsHighlightColor,0);
            letterColor=a.getInteger(R.styleable.SudokuBoard_letterColor,0);
            letterColorsolve=a.getInteger(R.styleable.SudokuBoard_letterColorsolve,0);
        }finally {
            a.recycle();

        }

    }
    @Override
    protected void onMeasure(int widht,int height) {
        super.onMeasure(widht, height);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        cellSize=dimension/9;
        setMeasuredDimension(dimension, dimension);
    }
    @Override
    protected void onDraw(Canvas canvas){
        boardcolorPaint.setStyle(Paint.Style.STROKE);
        boardcolorPaint.setStrokeWidth(16);
        boardcolorPaint.setColor(boardcolor);
        boardcolorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);

        cellsHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellsHighlightColorPaint.setAntiAlias(true);
        cellsHighlightColorPaint.setColor(cellsHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        colorCell(canvas,solver.getSelected_row(),solver.getSelected_column());

        canvas.drawRect(0,0,getWidth(),getHeight(),boardcolorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean isvalid;

        float x=event.getX();
        float y=event.getY();

        int action=event.getAction();

        if(action==MotionEvent.ACTION_DOWN){
            solver.setSelected_row((int) Math.ceil(y/cellSize));
            solver.setSelected_column((int) Math.ceil(x/cellSize));
            isvalid=true;
        }
        else{
            isvalid=false;
        }
        return isvalid;
    }

    private void drawNumbers(Canvas canvas){
        letterPaint.setTextSize(cellSize);
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                if(solver.getBoard()[r][c]!=0){
                    String text=Integer.toString(solver.getBoard()[r][c]);
                    float widht,height;

                    letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
                    widht=letterPaint.measureText(text);
                    height=letterPaintBounds.height();

                    canvas.drawText(text,(c*cellSize)+((cellSize-widht)/2),(r*cellSize+cellSize)
                    -((cellSize-height)/2),letterPaint);
                }
            }
        }
        letterPaint.setColor(letterColorsolve);
        for(ArrayList<Object>letter:solver.getEmptyBoxIndex()){
            int r=(int) letter.get(0);
            int c=(int) letter.get(1);
            String text=Integer.toString(solver.getBoard()[r][c]);
            float widht,height;

            letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
            widht=letterPaint.measureText(text);
            height=letterPaintBounds.height();

            canvas.drawText(text,(c*cellSize)+((cellSize-widht)/2),(r*cellSize+cellSize)
                    -((cellSize-height)/2),letterPaint);
        }
    }

    private void colorCell(Canvas canvas,int r,int c){
    if(solver.getSelected_column()!=-1 && solver.getSelected_row() !=-1){
        canvas.drawRect((c-1)*cellSize,0,c*cellSize,cellSize*9,
                cellsHighlightColorPaint);

        canvas.drawRect(0,(r-1)*cellSize,cellSize*9,r*cellSize,
                cellsHighlightColorPaint);

        canvas.drawRect((c-1)*cellSize,(r-1)*cellSize,c*cellSize,r*cellSize,
                cellsHighlightColorPaint);
    }
         invalidate();
    }


    private void drawThickLine(){
        boardcolorPaint.setStyle(Paint.Style.STROKE);
        boardcolorPaint.setStrokeWidth(10);
        boardcolorPaint.setColor(boardcolor);

    }
    private void drawThinLine(){
        boardcolorPaint.setStyle(Paint.Style.STROKE);
        boardcolorPaint.setStrokeWidth(4);
        boardcolorPaint.setColor(boardcolor);

    }

    private void drawBoard(Canvas canvas){

        for(int c=0;c<10;c++){
            if(c%3==0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }
            canvas.drawLine(cellSize*c,
                    0,cellSize*c,getWidth(),boardcolorPaint);

        }
        for(int r=0;r<10;r++){
            if(r%3==0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }
            canvas.drawLine(0,
                    cellSize*r,getWidth(), cellSize*r,boardcolorPaint);
        }

    }
    public Solver getSolver(){
         return this.solver;
    }

}
