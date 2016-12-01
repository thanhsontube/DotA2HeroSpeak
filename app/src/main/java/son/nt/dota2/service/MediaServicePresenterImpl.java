package son.nt.dota2.service;

import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;

import java.util.List;

import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 11/20/16.
 */
public class MediaServicePresenterImpl implements MediaServiceContract.Presenter {

    private MediaServiceContract.Controller mController;

    SparseArrayCompat<List<? extends ISound>> soundsSources = new SparseArrayCompat<>();

    private int mCurentIndex;

    public MediaServicePresenterImpl(MediaServiceContract.Controller controller) {
        mController = controller;
    }

    /**
     * what next after finish a sound
     */
    @Override
    public void completeSound() {

    }
    /**
     * what next if the selected sound can not played
     */
    @Override
    public void errorPlaySound() {

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
     * play a sound from selected item clicked from Adapter.
     * @param dto
     */
    @Override
    public void playSelectedSound(ISound dto, boolean arcana) {
        mController.playOnline(arcana ? (TextUtils.isEmpty(dto.getArcanaLink()) ? dto.getLink() : dto.getArcanaLink()) : dto.getLink());
        mController.showOnNotification (dto);

    }
}
