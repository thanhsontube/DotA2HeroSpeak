package son.nt.dota2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.BasePagerAdapter;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AdapterPagerHero extends BasePagerAdapter {

    List<Fragment> listFragments;
    ArrayList<String> titles;
    FragmentManager fm;

    public AdapterPagerHero(FragmentManager fm, List<Fragment> list, ArrayList<String> arr) {
        super(fm);
        this.listFragments = list;
        this.fm = fm;
        this.titles = arr;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public boolean isFragmentReusable(Fragment f, int position) {
        return true;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
