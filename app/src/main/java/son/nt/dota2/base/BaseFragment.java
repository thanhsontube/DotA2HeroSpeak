package son.nt.dota2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by sonnt on 11/6/16.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(provideLayoutResID(), container, false);
        ButterKnife.bind(this, view);
        Timber.d(">>> onCreate:" + getClass().getSimpleName());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    /**
     * provide the the xml layout
     */
    protected abstract int provideLayoutResID();
}
