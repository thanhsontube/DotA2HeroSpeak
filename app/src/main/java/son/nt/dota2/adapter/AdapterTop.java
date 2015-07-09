package son.nt.dota2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import son.nt.dota2.MsConst;
import son.nt.dota2.base.BasePagerAdapter;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.fragment.HeroListFragment;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class AdapterTop extends BasePagerAdapter {
    private HeroData herodata;
    private FragmentManager fm;
    private Fragment mPrimaryFragment;
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mPrimaryFragment = (Fragment) object;
    }

    public HeroListFragment getCurrentFragment() {
        return (HeroListFragment)mPrimaryFragment;
    }

    public AdapterTop(FragmentManager fm, HeroData herodata) {
        super(fm);
        this.fm = fm;
        this.herodata = herodata;

    }
    public void update(HeroData data) {
        this.herodata = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        String group = MsConst.GROUP_STR;
        switch (position) {
            case 0:
                group = MsConst.GROUP_STR;
                break;
            case 1:
                group = MsConst.GROUP_AGI;
                break;
            case 2:
                group = MsConst.GROUP_INTEL;
                break;
        }
        return HeroListFragment.newInstance(herodata, group);
    }

    @Override
    public boolean isFragmentReusable(Fragment f, int position) {
        return true;
    }

    @Override
    public int getCount() {
        return herodata.groups.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return herodata.groups[position];
    }
}
