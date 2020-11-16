package com.med.com.drawing.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Aklouh El Hassan on 31/03/2018.
 */

public class PaintPotView extends View implements View.OnTouchListener {

    private Rect rect;
    private final int DEFAULT_DOT_SIZE  = 10, DEFAULT_COLOR = Color.GREEN, MIN_DOT_SIZE = 10, MAX_DOT_SIZE = 100;
    private int mPenColor;
    private int RECTANGLE_STROKE_WIDTH = 40;
    private int mDotSize;
    private static int nbrTentative=0;
    private float circleX, circleY, circleRadius;
    private Path mPath, trianglePath;
    private Paint mPaint, shapePaint, pointShape;
    private float nX , nY ,mOldX , mOldY;
    private ArrayList<Path> mPaths;
    private ArrayList<Paint> mPaints;
    private ArrayList<Point> points, touchedPoints, processedPoint, formCoord, outSidePoints, topLeft, topRight, bottomLeft, bottomRight;
    private int nbrColone=10, RADIUS=50, form;
    private static int coloredPoints = 0, nbrPoints = 0, nbrPointsOutside = 0;

    public static int getColoredPoints(){return coloredPoints;}
    public static int getNbrPoints(){return nbrPoints;}
    public static int getNbrPointsOutside(){return nbrPointsOutside;}

    public void changeDotSize(int increment) {
        this.mDotSize += increment;
        this.mDotSize = Math.max(mDotSize,MIN_DOT_SIZE);
        this.mDotSize = Math.min(mDotSize,MAX_DOT_SIZE);
    }

    public int getDotSize() {
        return mDotSize;
    }

    public PaintPotView(Context context) {
        super(context);
        this.init();
    }

    public void setPenColor(int PenColor) {
        this.mPenColor = PenColor;
    }

    public int getPenColor() {
        return mPenColor;
    }

    private void init() {
        this.mDotSize = DEFAULT_DOT_SIZE;
        this.mPaths = new ArrayList<>();
        this.mPaints = new ArrayList<>();
        this.touchedPoints = new ArrayList<>();
        this.mPenColor = DEFAULT_COLOR;
        this.mPath = new Path();
        this.addPath(false);
        this.nX = this.nY = this.mOldY = this.mOldX = (float)0.0;
        this.setOnTouchListener(this);
        this.shapePaint = new Paint();
        this.shapePaint.setAntiAlias(true);
        this.shapePaint.setColor(Color.BLACK);
        this.shapePaint.setStyle(Paint.Style.STROKE);
        this.shapePaint.setStrokeJoin(Paint.Join.ROUND);
        this.shapePaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
        this.pointShape = new Paint();
        this.pointShape.setColor(Color.BLACK);
        this.pointShape.setStyle(Paint.Style.STROKE);
        this.pointShape.setStrokeWidth(5);
    }

    private void addPath(boolean fill){
        mPath = new Path();
        mPaths.add(mPath);
        mPaint = new Paint();
        mPaints.add(mPaint);
        mPaint.setColor(mPenColor);
        if(!fill)
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDotSize);
    }

    public PaintPotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PaintPotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void reset(){
        this.touchedPoints=new ArrayList<>();
        this.processedPoint=new ArrayList<>();
        coloredPoints=0;
        nbrPoints=0;
        this.nbrTentative=0;
        this.init();
        this.invalidate();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(int i = 0 ; i<mPaths.size() ; ++i){
            canvas.drawPath(mPaths.get(i) , mPaints.get(i));
        }

        switch (form){
            case 1 :
                canvas.drawRect(rect, shapePaint);
//                for (int h = 0; h < points.size(); h++) {
//                    canvas.drawPoint(points.get(h).getxCoord(), points.get(h).getyCoord(), pointShape);
//                }
                break;
            case 2 :
                canvas.drawPath(trianglePath, shapePaint);
//                for (int h = 0; h < points.size(); h++) {
//                    canvas.drawPoint(points.get(h).getxCoord(), points.get(h).getyCoord(), pointShape);
//                }
//                for (int p = 0; p < outSidePoints.size(); p++) {
//                    canvas.drawPoint(outSidePoints.get(p).getxCoord(), outSidePoints.get(p).getyCoord(), pointShape);
//                }
                break;
            case 3 :
                canvas.drawCircle(circleX, circleY, circleRadius, shapePaint);
//                canvas.drawPoint(circleX, circleY, pointShape);
//                for(int i=0; i<points.size(); i++){
//                    canvas.drawPoint(points.get(i).getxCoord(), points.get(i).getyCoord(), pointShape);
//                }
//                for(int i=0; i<topRight.size(); i++){
//                    canvas.drawPoint(topRight.get(i).getxCoord(), topRight.get(i).getyCoord(), pointShape);
//                    canvas.drawPoint(topLeft.get(i).getxCoord(), topLeft.get(i).getyCoord(), pointShape);
//                    canvas.drawPoint(bottomRight.get(i).getxCoord(), bottomRight.get(i).getyCoord(), pointShape);
//                    canvas.drawPoint(bottomLeft.get(i).getxCoord(), bottomLeft.get(i).getyCoord(), pointShape);
//                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        nX = motionEvent.getX();
        nY = motionEvent.getY();

        Point pt = new Point((int)nX, (int)nY);

        switch (motionEvent.getAction()){

            case MotionEvent.ACTION_DOWN:
                addPoint(pt);
                this.addPath(true);
                this.mPath.addCircle(nX , nY , mDotSize/2 , Path.Direction.CW);
                this.addPath(false);
                this.mPath.moveTo(nX,nY);
                break;

            case MotionEvent.ACTION_MOVE:
                addPoint(pt);
                this.mPath.lineTo(nX,nY);
                break;

            case MotionEvent.ACTION_UP:
                addPoint(pt);
                this.addPath(true);
                if(mOldX == nX && mOldY == nY)
                    this.mPath.addCircle(nX , nY , mDotSize/2 , Path.Direction.CW);
                break;
        }

        switch (form){

            case 1://Check if he touched outside the rectangle.
                int padding = 80;
                if( (nX <= padding || nX >= this.getWidth()-padding) ){
                    nbrTentative++;
                }
                if(nY <= padding || nY >= this.getHeight()-padding){
                    nbrTentative++;
                }
                break;

            case 2://check if he touched outside the triangle.
                if (nY < formCoord.get(0).getyCoord() || nY > formCoord.get(2).getyCoord()) { // if y < top point or y > bottom point
                    nbrTentative++;
                }

                for(int i=0; i<outSidePoints.size(); i++){
                    if(pt.isInside(outSidePoints.get(i), 30)){
                        nbrTentative++;
                    }
                }

                break;

            case 3://Check if he touch outside the circle.
                int tolerance=5;
                if(nY < circleY-circleRadius-tolerance || nY > circleY+circleRadius+tolerance){
                    nbrTentative++;
                }
                if(nX < circleX-circleRadius-tolerance || nX > circleX+circleRadius+tolerance){
                    nbrTentative++;
                }
                if(nX < circleX && nY < circleY){//topLeft
                    for(int i=0; i<topLeft.size(); i++){
                        if(pt.isInside(topLeft.get(i), 30)){
                            nbrTentative++;
                        }
                    }
                }else if(nX > circleX && nY < circleY){//topRight
                    for(int i=0; i<topRight.size(); i++){
                        if(pt.isInside(topRight.get(i), 30)){
                            nbrTentative++;
                        }
                    }
                }else if(nX < circleX && nY > circleY) {//bottomLeft
                    for(int i=0; i<bottomLeft.size(); i++){
                        if(pt.isInside(bottomLeft.get(i), 30)){
                            nbrTentative++;
                        }
                    }
                }else if(nX > circleX && nY < circleY){//bottomRight
                    for(int i=0; i<bottomRight.size(); i++){
                        if(pt.isInside(bottomRight.get(i), 30)){
                            nbrTentative++;
                        }
                    }
                }
                break;
        }

        this.invalidate();
        mOldX = nX;
        mOldY = nY;
        return  true;
    }

    private void addPoint(Point pt){
        if(touchedPoints.contains(pt)){
            System.out.println("Point Already Exist");
        }else{
            System.out.println("Point Added");
            touchedPoints.add(pt);
        }
    }

    public void drawRectangle(int left, int top, int right, int bottom){
        //Set form to 1
        form=1;

        //Setting up the rect variable
        rect = new Rect(left, top, right, bottom);

        //Setting up the inside points
        points = new ArrayList<>();

        //every point is inside a rectangle wich largeur = l & longueur = L
        float l = (right-left)/nbrColone;
        float L = (bottom-top)/nbrColone;

        //Setting up the radius variable
        RADIUS = (int) l;

        //Setting up the nbr of colonnes
        nbrColone=10;

        //adding the points in the arraylist points
        for (int x = 0; x < nbrColone; x++) {
            for (int y = 0; y < nbrColone; y++) {
                Point pt = new Point((int) (left + l/2 + x * l),(int) (top + L/2 + y * L));
                points.add(pt);
            }
        }

        reset();
        invalidate();
    }

    public void drawTriangle(int x, int y, int width){
        form=2;
        int halfwidth = width/2;
        trianglePath = new Path();
        trianglePath.moveTo(x, y-halfwidth); // Top
        trianglePath.lineTo(x - halfwidth, y + halfwidth); // Bottom left
        trianglePath.lineTo(x + halfwidth, y + halfwidth); // Bottom right
        trianglePath.lineTo(x, y - halfwidth); // Back to Top
        trianglePath.close();

        //saving the coordonates of the triangle.
        formCoord = new ArrayList<>();
        formCoord.add(new Point(x, y-halfwidth)); // Top -- indice 0
        formCoord.add(new Point(x - halfwidth, y + halfwidth)); // Bottom left -- indice 1
        formCoord.add(new Point(x + halfwidth, y + halfwidth)); // Bottom right -- indice 2

        // Setting up the points.
        points = new ArrayList<>();
        outSidePoints = new ArrayList<>();
        int dx = width/8, dy = dx, i, j, k, l;
        //      (x + j * dx, y+halfwidth - i * dy) pour j allant de 0-->4, on i allant de 0-->9-2j
        for(j=0; j<5; j++){
            for(i=0; i<7-2*j; i++){
                points.add(new Point(x + j * dx, y+halfwidth-dy - i * dy));
                points.add(new Point(x - j * dx, y+halfwidth-dy - i * dy));
            }
        }
        points = clearDoublePoints(points);
        System.out.println("Triangle : nbr de points inside :"+points.size());
        //Setting up oustside points
        for(l=0; l<4; l++){
            for(k=0; k<8-2*l; k++){
                outSidePoints.add(new Point(x+halfwidth-l*dx, y-halfwidth+k*dy));
                outSidePoints.add(new Point(x-halfwidth+l*dx, y-halfwidth+k*dy));
            }
        }
        System.out.println("Triangle : nbr de points outside :"+outSidePoints.size());

        reset();
        invalidate();
    }

    public void drawCircle(float cx, float cy, float radius){
        form=3;
        this.circleX = cx;
        this.circleY = cy;
        this.circleRadius = radius;

        points = new ArrayList<>();
        outSidePoints = new ArrayList<>();
        topLeft = new ArrayList<>();
        topRight = new ArrayList<>();
        bottomLeft = new ArrayList<>();
        bottomRight = new ArrayList<>();

        int dx = (int) radius/10;
        int dy = dx;

        RADIUS = (int) dy/2;

        //Inside points
        for (int j = 1; j < 20; j++) {
            points.add(new Point((int) cx, (int) (cy - radius + j * dy)));
        }
        for(int k=0; k<3; k++) {
            for (int j = 1; j < 20; j++) {
                points.add(new Point((int) (cx + ( k + 1 ) * dx), (int) (cy - radius + j * dy)));
                points.add(new Point((int) (cx - ( k + 1 ) * dx), (int) (cy - radius + j * dy)));
            }
        }
        for(int k=0; k<2; k++){
            for (int j = 1; j < 18; j++) {
                points.add(new Point((int) (cx + ( k + 4 ) * dx), (int) (cy - radius + (j+1) * dy)));
                points.add(new Point((int) (cx - ( k + 4 ) * dx), (int) (cy - radius + (j+1) * dy)));
            }
        }
        for(int k = 0; k < 4; k++){
            for (int j = 1; j < 16-2*k; j++) {
                points.add(new Point((int) (cx + (6+k) * dx), (int) (cy - radius + (j+2+k) * dy)));
                points.add(new Point((int) (cx - (6+k) * dx), (int) (cy - radius + (j+2+k) * dy)));
            }
        }

        //Outside points
        int l;
        for(int k = 0; k<2; k++){
            for( l=0; l<8-2*k; l++){
                topRight.add(new Point((int)(cx + radius - k * dx), (int) (cy - radius + l * dy)));
                bottomRight.add(new Point((int)(cx + radius - k * dx), (int) (cy + radius - l * dy)));
                topLeft.add(new Point((int)(cx - radius + k * dx), (int) (cy - radius + l * dy)));
                bottomLeft.add(new Point((int)(cx - radius + k * dx), (int) (cy + radius - l * dy)));
            }
            for( l=0; l<(4-k); l++){
                topRight.add(new Point((int)(cx + radius - (2+k) * dx), (int) (cy - radius + l * dy)));
                bottomRight.add(new Point((int)(cx + radius - (2+k) * dx), (int) (cy + radius - l * dy)));
                topLeft.add(new Point((int)(cx - radius + (2+k) * dx), (int) (cy - radius + l * dy)));
                bottomLeft.add(new Point((int)(cx - radius + (2+k) * dx), (int) (cy + radius - l * dy)));
            }
            for( l=0; l<2; l++){
                topRight.add(new Point((int)(cx + radius - (4+k) * dx), (int) (cy - radius + l * dy)));
                bottomRight.add(new Point((int)(cx + radius - (4+k) * dx), (int) (cy + radius - l * dy)));
                topLeft.add(new Point((int)(cx - radius + (4+k) * dx), (int) (cy - radius + l * dy)));
                bottomLeft.add(new Point((int)(cx - radius + (4+k) * dx), (int) (cy + radius - l * dy)));
            }
        }
        topRight.add(new Point((int)(cx + radius - 6 * dx), (int) (cy - radius)));
        bottomRight.add(new Point((int)(cx + radius - 6 * dx), (int) (cy + radius)));
        topLeft.add(new Point((int)(cx - radius + 6 * dx), (int) (cy - radius)));
        bottomLeft.add(new Point((int)(cx - radius + 6 * dx), (int) (cy + radius)));

        points = clearDoublePoints(points);
        System.out.println("Circle :  nbr points inside = "+points.size()+", points outside = "+(topLeft.size()+bottomRight.size()+topLeft.size()+bottomLeft.size()));
        reset();
        invalidate();
    }

    public ArrayList<Point> clearDoublePoints(ArrayList<Point> point){
        for(int i=0; i<point.size(); i++) {
            for (int j = i+1; j < point.size(); j++) {
                if (point.get(j).getxCoord() == point.get(i).getxCoord() && point.get(j).getyCoord() == point.get(i).getyCoord()) {
                    point.remove(point.get(i));
                }
            }
        }
        Collections.sort(point, Point.xComparator);
        return point;
    }

    public boolean contains(ArrayList<Point> points, Point point){
        for (int i=0; i<points.size(); i++){
            if (points.get(i).getxCoord() == point.getxCoord() && points.get(i).getyCoord() == point.getyCoord()){
                return true;
            }
        }
        return false;
    }

    public void getDrawnPoints(){
        //Clear double points & sort by x coord...
        touchedPoints = clearDoublePoints(touchedPoints);

        processedPoint = new ArrayList<>();

        coloredPoints = 0;
        nbrPoints = 0;
        nbrPointsOutside = 0;

        for(int j=0; j<touchedPoints.size(); j++){

            Point point = new Point(touchedPoints.get(j).getxCoord(), touchedPoints.get(j).getyCoord());

            for(int i=0; i<points.size(); i++) {

                Point pointI = new Point(points.get(i).getxCoord(), points.get(i).getyCoord());

                if ( point.isInside(pointI, RADIUS) ) {
                    if( contains(processedPoint, pointI)){
                        System.out.println("processedPoint contains pointI.");
                        break;
                    }else {
                        processedPoint.add(pointI);
                        break;
                    }
                }
            }
        }

        coloredPoints = processedPoint.size();
        nbrPoints = points.size();

        switch (form){
            case 2:
                nbrPointsOutside = outSidePoints.size();
                break;
            case 3:
                nbrPointsOutside = topRight.size() + topLeft.size() + bottomLeft.size() + bottomRight.size();
                break;
        }

    }

    public static int getNbrTentative(){
        return nbrTentative;
    }
}
