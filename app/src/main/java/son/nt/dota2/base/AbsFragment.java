package son.nt.dota2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Sonnt on 7/9/15.
 */
public abstract class AbsFragment extends AFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initLayout(view);
        initListener();
    }

    public abstract void initData();
    public abstract void initLayout(View view);
    public abstract void initListener();
}
