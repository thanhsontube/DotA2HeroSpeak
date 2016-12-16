package son.nt.dota2.service;

import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 11/20/16.
 */
public class MediaServicePresenterImpl implements MediaServiceContract.Presenter {

    private MediaServiceContract.Controller mController;

    SparseArrayCompat<List<? extends ISound>> soundsSources = new SparseArrayCompat<>();
    List<? extends ISound> mStoryList = new ArrayList<>();

    private int mCurentIndex;

    public MediaServicePresenterImpl(MediaServiceContract.Controller controller) {
        mController = controller;
    }

    /**
     * what next after finish a sound
     */
    @Override
    public void completeSound() {
        mCurentIndex++;
        playStory();

    }

    /**
     * what next if the selected sound can not played
     */
    @Override
    public void errorPlaySound() {
        mCurentIndex++;
        playStory();
    }

    @Override
    public void setSoundsSource(int type, List<? extends ISound> list) {
        soundsSources.put(type, list);

    }

    @Override
    public void setCurrentIndex(int currentIndex) {
        this.mCurentIndex = currentIndex;
    }

    /**
     * playStory a sound from selected item clicked from Adapter.
     */
    @Override
    public void playSelectedSound(ISound dto, boolean arcana) {
        mStoryList.clear();


        final String link = arcana ? (TextUtils.isEmpty(dto.getArcanaLink()) ? dto.getLink() : dto.getArcanaLink()) : dto.getLink();
        try {
            File file = new File(ResourceManager.getInstance().getPathSound(link, dto.getSavedRootFolder(), dto.getSavedBranchFolder()));
            String dataSourcePath = link;
            if (file.exists()) {
                dataSourcePath = file.getPath();
            } else {
                mController.downloadSoundService(dto);
            }

            mController.playOnline(dataSourcePath);
            mController.showOnNotification(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void playSelectedStory(List<? extends ISound> mlist, String title, String user) {
        mCurentIndex = 0;
        this.mStoryList = mlist;
        playStory();
    }

    private void playStory() {
        if (mStoryList != null && !mStoryList.isEmpty() && mCurentIndex < mStoryList.size()) {
            final ISound iSound1 = mStoryList.get(mCurentIndex);

            if (TextUtils.isEmpty(iSound1.getLink())) {
                mCurentIndex++;
                playStory();
                return;
            }
            mController.playOnline(iSound1.getLink());

        }

    }
}
