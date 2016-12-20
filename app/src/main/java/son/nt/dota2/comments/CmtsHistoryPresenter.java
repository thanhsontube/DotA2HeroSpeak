package son.nt.dota2.comments;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class CmtsHistoryPresenter extends BasePresenter implements CmtsHisotyContract.Presenter {
    CmtsHisotyContract.View mView;
    IHeroRepository mRepository;

    private String mSide;
    private String mFilter;
    private HeroBasicDto mHeroBasicDto;
    private HeroResponsesDto mHeroResponsesDto;

    public CmtsHistoryPresenter(CmtsHisotyContract.View view, IHeroRepository repository) {
        mView = view;
        this.mRepository = repository;
    }

    @Override
    public void getCmts() {
        mRepository.getAllComments ()
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
}
