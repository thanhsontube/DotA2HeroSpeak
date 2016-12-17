package son.nt.dota2.story.cmts;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.comments.CmtsDto;
import son.nt.dota2.comments.FullCmtsDto;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.firebase.FireBaseUtils;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryCmtsPresenter extends BasePresenter implements StoryCmtsContract.Presenter {

    StoryCmtsContract.View mView;
    IHeroRepository mRepository;


    String mStoryId;

    public StoryCmtsPresenter(StoryCmtsContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    @Override
    public void setStoryId(String data) {
        this.mStoryId = data;
    }

    @Override
    public void getComments() {
        mRepository.getStoryComments(mStoryId)
                .map(new Func1<List<CmtsDto>, List<FullCmtsDto>>() {
                    @Override
                    public List<FullCmtsDto> call(List<CmtsDto> cmtsDtos) {
                        List<FullCmtsDto> fullCmtsDtos = new ArrayList<FullCmtsDto>();
                        FullCmtsDto dto;
                        for (CmtsDto d : cmtsDtos) {
                            dto = new FullCmtsDto();
                            dto.setCmtsDto(d);
                            fullCmtsDtos.add(dto);
                        }
                        return fullCmtsDtos;
                    }
                })


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FullCmtsDto>>() {
                    @Override
                    public void call(List<FullCmtsDto> fullCmtsDtos) {
                        mView.showList (fullCmtsDtos);
                    }
                });


    }

    @Override
    public void putCommend(String mess) {
        FireBaseUtils.sendStoryComments(mess, mStoryId);
    }
}
