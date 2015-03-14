package son.nt.dota2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import son.nt.dota2.base.BasePagerAdapter;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.fragment.HeroListFragment;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class AdapterTop extends BasePagerAdapter {
    private HeroData herodata;
    private FragmentManager fm;

    public AdapterTop(FragmentManager fm, HeroData herodata) {
        super(fm);
        this.fm = fm;
        this.herodata = herodata;

    }

    @Override
    public Fragment getItem(int position) {
        return HeroListFragment.newInstance(herodata);
    }

    @Override
    public boolean isFragmentReusable(Fragment f, int position) {
        return true;
    }

    @Override
    public int getCount() {
        return  herodata.groups.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return herodata.groups[position];
    }
}
