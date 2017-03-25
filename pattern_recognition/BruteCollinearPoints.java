/*----------------------------------------------------------------
 *  Author:        Daniel Doyle
 *  Written:       11/03/2017
 *  Last updated:  11/03/2017
 *  Score:         93/100
 *
 *  Compilation:   javac-algs4 BruteCollinearPoints.java
 *  Execution:     java BruteCollinearPoints
 *
 *  Examines 4 points at a time and checks whether they all lie on the same
 *  line segment, returning all such line segments
 *----------------------------------------------------------------*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;


public class BruteCollinearPoints {

    private ArrayList<LineSegment> lineSegments;    // list of output segments

    /**
     *   Constructor. Initialises variables etc.
     *
     *   Takes a list of x,y points from an input file and
     *   determines the line segments present by brute force
     *
     *   throws NullPointerException if the point array or any of
     *      the points are null
     *   throws IllegalArgumentException if a repeated point is detected
     */
    public BruteCollinearPoints(Point[] points) {

        if (points == null) throw new java.lang.NullPointerException();

        // initialise the output list
        lineSegments = new ArrayList<LineSegment>();

        // iterate through each point in the list
        for (int i = 0; i < points.length; i++)
        {
            if (points[i] == null) throw new java.lang.NullPointerException();

            // keep a list of points on the same slope relative to i
            HashMap<Double, List<Point>> matches = new HashMap<>();

            // iterate through each other point excluding i & those preceeding i
            for (int j = i + 1; j < points.length; j++)
            {
                if (points[j] == null)
                    throw new java.lang.NullPointerException();

                // no repeated points
                if (points[j] == points[i])
                    throw new java.lang.IllegalArgumentException();

                // get slope of j relative to i
                double slope = points[i].slopeTo(points[j]);

                // get the existing point list for this slope if there is one
                List<Point> results = matches.getOrDefault(
                    slope, new ArrayList<Point>(Arrays.asList(points[i]))
                );

                // add j to the appropriate list based on slope to i
                results.add(points[j]);

                // create the list, if there wasn't one before
                if (!matches.containsKey(slope)) {
                    matches.put(slope, results);
                }
            }

            // Get a set of the points
            Set<Map.Entry<Double, List<Point>>> set = matches.entrySet();

            // Get an iterator
            Iterator<Map.Entry<Double, List<Point>>> k = set.iterator();

            // Iterate through the points per slope and build segments
            while (k.hasNext())
            {
                Map.Entry<Double, List<Point>> potentialPoints = k.next();

                // there must be at least 4 collinear points
                if (potentialPoints.getValue().size() >= 4)
                {
                    // get start and end points and build line segment
                    LineSegment lns = new LineSegment(
                        Collections.min(potentialPoints.getValue()),
                        Collections.max(potentialPoints.getValue())
                    );
                    lineSegments.add(lns);
                }
            }
        }
    }

    /**
     *  Returns the number of segments
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     *  Returns an array containing the output line segments
     */
    public LineSegment[] segments() {
        LineSegment[] output = new LineSegment[lineSegments.size()];
        output = lineSegments.toArray(output);

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}