package son.nt.dota2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import son.nt.dota2.fragment.HeroListFragment;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    List<HeroListFragment> mFragments;

//    @Override
//    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        super.setPrimaryItem(container, position, object);
//    }

    public HomePagerAdapter(FragmentManager fm, List<HeroListFragment> list) {
        super(fm);
        this.mFragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

//    @Override
//    public boolean isFragmentReusable(Fragment f, int position) {
//        return true;
//    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "abc" + position;
//        return mFragments.get(position).getGroupDisplayName();
    }
}
