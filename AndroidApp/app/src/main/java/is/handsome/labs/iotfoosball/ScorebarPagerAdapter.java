package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ScorebarPagerAdapter extends PagerAdapter {

    private ArrayList<View> mPages;
    int mCount;

    public ScorebarPagerAdapter(Context context, int count) {
        this.mCount = count;
        //TODO reuse invisible views and use ViewHolder
        mPages = new ArrayList<View>();
        for (int i = 0; i < 10; i++) {
            Log.d("score", "crating view # " + i);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.barnumber, null);
            ((TextView) v.findViewById(R.id.number)).setText(String.valueOf(i));
            Log.d("score", "view # " + i + " created " + v.toString());
            mPages.add(v);
            Log.d("score", "view # " + i + " added");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("score", "adding # " + String.valueOf(position));
        container.addView(mPages.get(position % 10));
        return mPages.get(position % 10);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPages.get(position % 10));
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
