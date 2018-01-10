package com.example.mbcrocci.amov1718;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.mbcrocci.amov1718.board.Location;
import com.example.mbcrocci.amov1718.move.Move;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardView extends View {
    Location loc;

    int selectedSquare;
    float cursorX, cursorY;
    boolean cursorVisible;
    protected int x0, y0, sqSize;
    boolean flipped;
    boolean oneTouchMoves;

    List<Move> moveHints;

    protected Paint darkPaint;
    protected Paint brightPaint;
    private Paint selectedSquarePaint;
    private Paint cursorSquarePaint;

    private ArrayList<Paint> moveMarkPaint;

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        loc = new Location(0, 0);
        selectedSquare = -1;
        cursorX = 0; cursorY = 0;
        cursorVisible = false;
        x0 = y0 = sqSize = 0;
        flipped = false;
        oneTouchMoves = false;

        darkPaint = new Paint();
        brightPaint = new Paint();

        selectedSquarePaint = new Paint();
        selectedSquarePaint.setStyle(Paint.Style.STROKE);
        selectedSquarePaint.setAntiAlias(true);

        cursorSquarePaint = new Paint();
        cursorSquarePaint.setStyle(Paint.Style.STROKE);
        cursorSquarePaint.setAntiAlias(true);

        moveMarkPaint = new ArrayList<Paint>();
        for (int i = 0; i < 6; i++) {
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setAntiAlias(true);
            moveMarkPaint.add(p);
        }

        setColors();
    }

    final void setColors() {
        brightPaint.setColor(Color.GRAY);
    }
}
