package son.nt.dota2.base;

/**
 * Created by sonnt on 5/23/16.
 */

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by sonnt on 3/27/16.
 */
public abstract class ASafeActivity extends AppCompatActivity {


    private ProgressDialog mProgressDialog;

    public abstract int getContentViewID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        OttoBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    public boolean isSafe() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this.isDestroyed()) {
                return false;
            }
        }
        return !this.isFinishing();
    }

    public void showProgressDialog(final String message, boolean isCancelable) {
        if (!this.isSafe()) {
            return;

        }

        if (this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        this.mProgressDialog.setMessage(message);
        this.mProgressDialog.setCancelable(isCancelable);

        if (this.mProgressDialog.isShowing()) {
            return;
        }

        this.mProgressDialog.show();

    }

    public void hideProgressDialog() {
        if (this.mProgressDialog == null) {
            return;
        }

        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
    }

    public void setupToolbar(Toolbar mToolbar, int icon, String title) {
        if (mToolbar == null) {
            return;
        }
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (icon != -1) {
            ab.setHomeAsUpIndicator(icon);
        }
        ab.setTitle(title);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

