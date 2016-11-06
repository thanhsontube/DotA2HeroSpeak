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

    private List<HeroListFragment> mFragments;

    public HomePagerAdapter(FragmentManager fm, List<HeroListFragment> list) {
        super(fm);
        this.mFragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Str";
            case 1:
                return "Agi";
            case 2:
                return "Intel";
        }
        return "";
    }
}
