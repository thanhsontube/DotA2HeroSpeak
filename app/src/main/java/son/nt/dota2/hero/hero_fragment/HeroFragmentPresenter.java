package son.nt.dota2.hero.hero_fragment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.utils.Logger;
import timber.log.Timber;

import static son.nt.dota2.hero.hero_fragment.AdapterFragmentSound.TAG;

/**
 * Created by sonnt on 11/7/16.
 * Input is heroID
 * Get HeroBasicDto from heroID -> update kenburns and get heroGroup,
 * Then get List <HeroBasicDto> based on heroGroup
 */

public class HeroFragmentPresenter extends BasePresenter implements HeroResponseContract.Presenter {
    private HeroResponseContract.View mView;
    private IHeroRepository mRepository;
    private String mHeroID;
    private HeroBasicDto mSelectedHero;

    private boolean mBindFetchServiceDone = false;
    private boolean mHeroSoundsLoaded = false;
    private List<HeroResponsesDto> mHeroResponsesDtos;

    public HeroFragmentPresenter(HeroResponseContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void fetchBasicHeroFromHeroId(String heroId) {
        this.mHeroID = heroId;
        final Subscription subscribe = mRepository.getHeroFromId(heroId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(heroBasicDto -> {
                            this.mSelectedHero = heroBasicDto;
                            mView.updateArcana(mSelectedHero.isArcana());
                            getHeroSounds();

                        }
                );
        mCompositeSubscription.add(subscribe);
    }

    @Override
    public void setFetchServiceBind(boolean bind) {
        this.mBindFetchServiceDone = bind;
        startPrefetch();
    }

    @Override
    public List<HeroResponsesDto> getSoundsList() {
        return mHeroResponsesDtos;
    }

    @Override
    public void getAbi() {
        final Subscription subscribe = mRepository.getAbis(mHeroID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(data -> {
                            mView.updateAbi(data);

                        }
                );
        mCompositeSubscription.add(subscribe);
    }

    @Override
    public void downloadFetch() {
//        download(mHeroResponsesDtos);
    }

    @Override
    public void searchSound(String keyword) {

        mRepository.getSearchSounds(mHeroID, keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HeroResponsesDto>>() {
                    @Override
                    public void call(List<HeroResponsesDto> heroResponsesDtos) {
                        mView.showHeroSoundsList(heroResponsesDtos);
                    }
                });
    }

    private void startPrefetch() {
        Timber.d(">>>" + ">>>" + "startPrefetch mBindFetchServiceDone:" + mBindFetchServiceDone + ";mHeroSoundsLoaded:" + mHeroSoundsLoaded);
        if (mBindFetchServiceDone && mHeroSoundsLoaded) {
            mView.addDataToDownload(mHeroResponsesDtos, mHeroID);
            download(mHeroResponsesDtos);
            mBindFetchServiceDone = false;
            mHeroSoundsLoaded = false;
        }
    }

    @Override
    public void getHeroSounds() {
        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {
                // Do nothing

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(">>>" + "error getAllHeroBasicOnGroup:" + e);

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mHeroSoundsLoaded = true;
                mView.showHeroSoundsList(list);
                mHeroResponsesDtos = list;
//                download(list);

//                startPrefetch();

            }
        };

        Subscription subscription = mRepository.getSounds(mHeroID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);
    }

//    private void download2(final List<HeroResponsesDto> list) {
//        for (ISound d : list) {
//            download(d);
//        }
//    }

    private void download(final List<HeroResponsesDto> list) {
        Timber.d(">>>" + "download:" + list);
        if (list == null || list.isEmpty()) {
            return;
        }
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {

            for (ISound iSound : list) {
                File fileTo = new File(ResourceManager.getInstance().getPathSound(iSound.getLink(), iSound.getSavedRootFolder(), iSound.getSavedBranchFolder()));
                downloadSound(iSound.getLink(), fileTo);

                if (!TextUtils.isEmpty(iSound.getArcanaLink())) {
                    File fileToArcana = new File(ResourceManager.getInstance().getPathSound(iSound.getArcanaLink(), iSound.getSavedRootFolder(), iSound.getSavedBranchFolder()));
                    downloadSound(iSound.getArcanaLink(), fileToArcana);
                }
            }



            subscriber.onNext("");
            subscriber.onCompleted();

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Timber.d(">>>" + "Download Done");
//                    Timber.d(">>>" + "download done:" + iSound.getSavedRootFolder() + "/" + iSound.getSavedBranchFolder());

                });
    }

    public void downloadSound(String linkSpeak, File fileTo) {
        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }

        if (fileTo.exists()) {
            return;
        }

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(linkSpeak));
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();

                OutputStream out = new FileOutputStream(fileTo, false);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                in.close();

                Logger.debug(TAG, ">>>" + "downloadLink SUCCESS");

            } else {
                Logger.error(TAG, ">>>" + "Download FAIL:" + status.getStatusCode());
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "downloadSound err:" + e);
        }
    }


}
