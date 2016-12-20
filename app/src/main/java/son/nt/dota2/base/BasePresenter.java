package son.nt.dota2.base;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sonnt on 11/7/16.
 */

public class BasePresenter {
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();


    public void onDestroy() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
