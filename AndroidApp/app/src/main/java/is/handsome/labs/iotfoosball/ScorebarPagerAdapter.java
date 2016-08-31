package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ScorebarPagerAdapter extends PagerAdapter {

    private ArrayList<View> pages;
    private int count;

    public ScorebarPagerAdapter(Context context, int count) {
        this.count = count;
        //TODO reuse invisible views and use ViewHolder
        pages = new ArrayList<View>();
        for (int i = 0; i < 10; i++) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.barnumber, null);
            ((TextView) v.findViewById(R.id.number)).setText(String.valueOf(i));
            pages.add(v);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Log.d("score", "adding # " + String.valueOf(position));
        container.addView(pages.get(position % 10));
        return pages.get(position % 10);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pages.get(position % 10));
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
