package son.nt.dota2.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public abstract class BasePagerAdapter extends PagerAdapter {
    private static final String TAG = "MyFragmentPagerAdapter";
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment mCurrentPrimaryItem = null;

    public BasePagerAdapter(FragmentManager fragmentManager) {
        this.fm = fragmentManager;
    }

    public abstract Fragment getItem(int position);

    public abstract boolean isFragmentReusable(Fragment f, int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (ft == null) {
            ft = fm.beginTransaction();
        }

        // check the exist of fragment
        String name = makeFragmentName(container.getId(), position);
        Fragment fragment = fm.findFragmentByTag(name);

        boolean reusable = (fragment != null && isFragmentReusable(fragment, position));
        if (fragment != null && reusable) {
            Log.i(TAG, "reuseable fragment at:" + position);
            ft.show(fragment);
        } else {
            fragment = getItem(position);
            if (reusable) {
                Log.i(TAG, "instantiateItem replace fragment at:" + position);
                ft.replace(container.getId(), fragment, makeFragmentName(container.getId(), position));
            } else {
                Log.i(TAG, "instantiateItem add fragment at:" + position);
                ft.add(container.getId(), fragment, makeFragmentName(container.getId(), position));
            }
        }

        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
        }
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
            }

            if (fragment != null) {
                fragment.setMenuVisibility(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (ft == null) {
            ft = fm.beginTransaction();
        }
        Fragment fragment = (Fragment) object;
        if (isFragmentReusable(fragment, position)) {
            Log.i(TAG, "destroyItem hide at:" + position);
            ft.hide(fragment);
        } else {
            Log.i(TAG, "destroyItem detach at:" + position);
            ft.detach(fragment);
        }

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return ((Fragment) arg1).getView() == arg0;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (ft != null) {
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();
        }
    }

    private static String makeFragmentName(int viewId, int index) {
        StringBuilder builder = new StringBuilder();
        builder.append("makeFragmentName:switcher:");
        builder.append(viewId);
        builder.append(":");
        builder.append(index);
        return builder.toString();
    }

    public Fragment getFragment(View container, int position) {
        return fm.findFragmentByTag(makeFragmentName(container.getId(), position));
    }
}
