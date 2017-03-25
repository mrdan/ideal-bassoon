/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints
 *  Dependencies: none
 *  Score:         93/100
 *
 *  Determines whether p participates in a set of 4 or more collinear points.
 *
 ******************************************************************************/
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {

    private ArrayList<LineSegment> segments;    // list of output segments

    /**
     *   Constructor. Initialises variables etc.
     *
     *   Takes a list of x,y points from an input file and
     *   determines the line segments present
     *
     *  throws NullPointerException if the point array or any of
     *      the points are null
     *  throws IllegalArgumentException if a repeated point is detected
     */
    public FastCollinearPoints(Point[] points) {

        if (points == null) throw new java.lang.NullPointerException();

        for (int j = 0; j < points.length; j++)
        {
            if (points[j] == null) throw new java.lang.NullPointerException();

            for (int k = j + 1; k < points.length; k++)
            {
                if (points[k].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)
                    throw new java.lang.IllegalArgumentException();
            }
        }

        segments = new ArrayList<LineSegment>();

        ArrayList<ArrayList<Point>> pointList = new ArrayList<ArrayList<Point>>();

        Point[] remaining = points.clone();

        // for point i
        for (int i = 0; i < points.length; i++) {

            ArrayList<Point> potential = new ArrayList<Point>();

            // sort
            Arrays.sort(remaining, points[i].slopeOrder());

            // add the current origin
            potential.add(remaining[0]);

            // check adjacent points for equal slopes
            int count = 1; // origin point already in there
            for (int k = 1; k < remaining.length; k++)
            {
                // if it's the first one, just add it
                if (k == 1) {
                    potential.add(remaining[k]);
                    count++;
                    continue;
                }

                double prevSlope = points[i].slopeTo(remaining[k - 1]);
                double nuSlope = points[i].slopeTo(remaining[k]);

                // if the slope is the same as the last, add it
                if (prevSlope == nuSlope)
                {
                    potential.add(remaining[k]);
                    count++;
                }
                else
                {
                    // if there has been a streak of 4 (counting the origin)
                    if (count > 3)
                    {
                        // add this list
                        pointList.add(potential);
                    }

                    // create a new potential list
                    // pre-add the current & the origin
                    potential = new ArrayList<Point>();
                    potential.add(remaining[0]);
                    potential.add(remaining[k]);
                    count = 2;
                }
            }

            // add remaining potential at the end of the loop if it's big enough
            if (count > 3)
            {
                pointList.add(potential);
            }
        }

        // create line segments
        ArrayList<ArrayList<Point>> added = new ArrayList<>();
        for (ArrayList<Point> x : pointList)
        {
            Collections.sort(x);
            if (!added.contains(x)) {
                // build a line segment using the min and max of the point list
                segments.add(new LineSegment(x.get(0), x.get(x.size() - 1)));
                added.add(x);
            }
        }
    }

    /**
     *  Returns the number of segments
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     *  Returns an array containing the output line segments
     */
    public LineSegment[] segments() {
        LineSegment[] output = new LineSegment[segments.size()];
        output = segments.toArray(output);
        return output;
    }

    /**
     *  Tests
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
