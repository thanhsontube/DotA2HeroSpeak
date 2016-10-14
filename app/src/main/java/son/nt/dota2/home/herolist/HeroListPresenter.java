package son.nt.dota2.home.herolist;

import son.nt.dota2.firebase.IFireBaseRepository;

/**
 * Created by sonnt on 10/14/16.
 */

public class HeroListPresenter implements HeroListContract.Presenter {
    private static final String TAG = HeroListPresenter.class.getSimpleName();
    HeroListContract.View mView;
    IFireBaseRepository mIFireBaseRepository;

    private String mGroup;

    public HeroListPresenter(HeroListContract.View view, IFireBaseRepository IFireBaseRepository) {
        mView = view;
        mIFireBaseRepository = IFireBaseRepository;

        mIFireBaseRepository.setCallBack(list -> mView.showHeroList (list));
    }

    @Override
    public void setGroup(String group) {
        this.mGroup = group;

    }

    @Override
    public void getHeroList() {
        mIFireBaseRepository.getHeroBasic(mGroup);
    }

//    @Subscribe
//    public void onGetHeroList (BasicHeroListResponse response) {
//        Logger.debug(TAG, ">>>" + "onGetHeroList:" + response);
//        if (response != null) {
//            mView.showHeroList (response.getHeroBasicDtoList());
//        }
//
//    }


}
