package son.nt.dota2.service;

import java.util.List;

import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 11/20/16.
 */

public class MediaServiceContract {

    public interface Controller  {
        void createMediaPlayerIfNeeded();
        void releaseMediaPlayer ();
        void switchPlay (int status);
        void play();
        void pause();

        void playOffline (String dataSource);
        void playOnline (String link);
        void downloadSoundService (ISound iSound);

        void setCurrentIndex (int currentIndex);
        void setSoundsSource (int type, List<? extends ISound> list);

        void showOnNotification(ISound dto);
    }

    public interface Presenter  {

        void completeSound();

        void errorPlaySound();

        void setCurrentIndex (int currentIndex);

        void setSoundsSource(int type, List<? extends ISound> list);

        void playSelectedSound(ISound dto, boolean arcana);

        void playSelectedStory(List<? extends ISound> mlist, String title, String user);
    }
}
