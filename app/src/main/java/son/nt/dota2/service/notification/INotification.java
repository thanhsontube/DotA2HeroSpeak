package son.nt.dota2.service.notification;

import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 7/28/16.
 */
public interface INotification {

    void setData(ISound currentItem);

    void doPause();

    void doPlay();

    void doDetach();
}
