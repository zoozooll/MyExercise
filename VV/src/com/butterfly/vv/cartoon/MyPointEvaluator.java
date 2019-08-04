package com.butterfly.vv.cartoon;

import com.nineoldandroids.animation.TypeEvaluator;

import android.graphics.Point;
import android.util.Log;

/**
 * This evaluator can be used to perform type interpolation between <code>PointF</code> values.
 */
public class MyPointEvaluator implements TypeEvaluator<Point> {

    /**
     * When null, a new PointF is returned on every evaluate call. When non-null,
     * mPoint will be modified and returned on every evaluate.
     */
    private Point mPoint;

    /**
     * Construct a PointFEvaluator that returns a new PointF on every evaluate call.
     * To avoid creating an object for each evaluate call,
     * {@link MyPointEvaluator#MyPointEvaluator(android.graphics.PointF)} should be used
     * whenever possible.
     */
    public MyPointEvaluator() {
    }

    /**
     * Constructs a PointFEvaluator that modifies and returns <code>reuse</code>
     * in {@link #evaluate(float, android.graphics.PointF, android.graphics.PointF)} calls.
     * The value returned from
     * {@link #evaluate(float, android.graphics.PointF, android.graphics.PointF)} should
     * not be cached because it will change over time as the object is reused on each
     * call.
     *
     * @param reuse A PointF to be modified and returned by evaluate.
     */
    public MyPointEvaluator(Point reuse) {
        mPoint = reuse;
    }

    /**
     * This function returns the result of linearly interpolating the start and
     * end PointF values, with <code>fraction</code> representing the proportion
     * between the start and end values. The calculation is a simple parametric
     * calculation on each of the separate components in the PointF objects
     * (x, y).
     *
     * <p>If {@link #MyPointEvaluator(android.graphics.PointF)} was used to construct
     * this PointFEvaluator, the object returned will be the <code>reuse</code>
     * passed into the constructor.</p>
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start PointF
     * @param endValue   The end PointF
     * @return A linear interpolation between the start and end values, given the
     *         <code>fraction</code> parameter.
     */
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        int x = (int) (startValue.x + (fraction * (endValue.x - startValue.x)));
        int y = (int) (startValue.y + (fraction * (endValue.y - startValue.y)));
//        Log.d("aaron", "fraction " + fraction + " startValue " + startValue + " endValue " + endValue);
        if (mPoint != null) {
            mPoint.set(x, y);
            return mPoint;
        } else {
            return new Point(x, y);
        }
    }
}
