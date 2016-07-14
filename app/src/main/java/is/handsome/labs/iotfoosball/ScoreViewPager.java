package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import timber.log.Timber;

/**
 /**
 * Uses a combination of a PageTransformer and swapping X & Y coordinates
 * of touch events to create the illusion of a vertically scrolling ViewPager.
 *
 * Requires API 11+
 *
 */
public class ScoreViewPager extends ViewPager {

    private Boolean isListing = true;
    private ViewPager mFirstDigitViewPager;

    public ScoreViewPager(Context context) {
        super(context);
        init();
    }


    public ScoreViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setListing(boolean listing) {
        isListing = listing;
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = -position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();
        Timber.d(" before "+ ev.toString());
        //TODO invert Y direction

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        Timber.d(" after "+ ev.toString());
        return ev;
    }

    public void setFirstDigitViewPager(ViewPager viewPager) {
        mFirstDigitViewPager = viewPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //TODO catch MotionEvent in common frame and redirect it to 1st digit
        Timber.d("Event = " + ev.toString());
        if (isListing) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev); // return touch coordinates to original reference frame for any child views
            return intercepted;
        } else if (mFirstDigitViewPager != null) {
            Timber.d("redirected");
            swapXY(ev);
            boolean intercepted = mFirstDigitViewPager.onInterceptTouchEvent(ev);
            return intercepted;
        } else {
            Timber.d("rejected");
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Timber.d("OnTouchEvent");
        if (isListing) {
            return isListing && super.onTouchEvent(swapXY(ev));
        }
        else if (mFirstDigitViewPager != null) {
            return mFirstDigitViewPager.onTouchEvent(ev);
        } else {
            return true;
        }
    }

}
