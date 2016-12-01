package son.nt.dota2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.fragment.AbiFragment;
import son.nt.dota2.fragment.SwipeHeroFragment;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AdapterPagerHero extends FragmentPagerAdapter {

    List<HeroBasicDto> listFragments;

    public AdapterPagerHero(FragmentManager fm, List<HeroBasicDto> list) {
        super(fm);
        this.listFragments = list;
    }

    public void updateData(List<HeroBasicDto> list) {
        listFragments = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
//        return IntroFragment.newInstance(listFragments.get(position).heroId);
//        return new TestFragment();
//        return VoiceFragment.newInstance(listFragments.get(position).heroId);
        return SwipeHeroFragment.newInstance(listFragments.get(position).heroId);
    }


    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listFragments.get(position).name;
    }


}
