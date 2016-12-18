package son.nt.dota2.manager;

import rx.Observable;

/**
 * Created by sonnt on 12/18/16.
 */

public interface IAssetMng {
    Observable<String> copyDataFromAsset (String assetFolder, String output);

}
