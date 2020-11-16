package com.med.com.drawing.Utils;

import java.util.Comparator;

/**
 * Created by Abad Mohamed Zayd on 4/26/2018.
 */

public class Point {

    private int xCoord;
    private int yCoord;


    //Constructor
    public Point(int xCoord, int yCord) {
        this.xCoord = xCoord;
        this.yCoord = yCord;
    }

    //Getters
    public int getxCoord() {
        return this.xCoord;
    }
    public int getyCoord() {
        return this.yCoord;
    }


    //Others
    public boolean isInside(Point mPoint, int radius){
        boolean result=true;

        if(this.getxCoord() < (mPoint.getxCoord()-radius) || this.getxCoord() > mPoint.getxCoord()+radius){
            result = false;
        }else{
            if(this.getyCoord() < (mPoint.getyCoord()-radius) || this.getyCoord() > (mPoint.getyCoord()+radius) ){
                result = false;
            }else{
                result = true;
            }
        }

        return result;
    }


    //Comparator
    public static Comparator<Point> xComparator = new Comparator<Point>() {
        @Override
        public int compare(Point point1, Point point2) {
            int x1 = point1.getxCoord(), x2 = point2.getxCoord();
            return x1-x2;
        }
    };
    public static Comparator<Point> yComparator = new Comparator<Point>() {
        @Override
        public int compare(Point point1, Point point2) {
            int y1 = point1.getyCoord(), y2 = point2.getyCoord();
            return y1-y2;
        }
    };

}
