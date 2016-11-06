package son.nt.dota2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by sonnt on 11/6/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutResID());
        ButterKnife.bind(this);
        Timber.d(">>> onCreate:" + getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * provide the the xml layout
     */
    protected abstract int provideLayoutResID();
}
