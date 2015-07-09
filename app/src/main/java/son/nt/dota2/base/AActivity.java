package son.nt.dota2.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.Collection;
import java.util.Stack;

abstract public class AActivity extends AppCompatActivity implements OnBackStackChangedListener {

    protected static final String FRAGMENT_KEY = "main";
    protected static final String SAVE_KEY_STACK = "tag_stack";

    protected final Stack<String> mFragmentTagStack = new Stack<String>();

    abstract protected Fragment onCreateMainFragment(Bundle savedInstanceState);

    abstract protected int getFragmentContainerId();

    public interface OnBackPressListener {

        boolean onBackPress();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(getFragmentContainerId(), onCreateMainFragment(savedInstanceState), FRAGMENT_KEY)
                    .setTransition(FragmentTransaction.TRANSIT_NONE).commit();
        } else {
            mFragmentTagStack.addAll((Collection<String>) savedInstanceState.getSerializable(SAVE_KEY_STACK));
            restoreFragmentsState();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        restoreFragmentsState();
    }

    @Override
    public void onBackPressed() {
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment f;
        if (mFragmentTagStack.size() > 0) {
            f = fm.findFragmentByTag(mFragmentTagStack.peek());
        } else {
            f = fm.findFragmentByTag(FRAGMENT_KEY);
        }

        if (f instanceof OnBackPressListener) {
            if (((OnBackPressListener) f).onBackPress()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVE_KEY_STACK, mFragmentTagStack);
    }

    public void showFragment(Fragment f, boolean isTransit) {
        StringBuilder tagB = new StringBuilder();
        tagB.append(getClass().getName());
        tagB.append(":");
        tagB.append(mFragmentTagStack.size());

        String tag = tagB.toString();

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        if (mFragmentTagStack.size() > 0) {
            final Fragment ff = fm.findFragmentByTag(mFragmentTagStack.peek());
            ft.hide(ff);
        } else {
            final Fragment ff = fm.findFragmentByTag(FRAGMENT_KEY);
            ft.hide(ff);
        }
        if (fm.findFragmentByTag(tag) == null) {
            ft.add(getFragmentContainerId(), f, tag);
            ft.show(f);
        } else {
            ft.replace(getFragmentContainerId(), f, tag);
            ft.show(f);
        }
        if (isTransit) {
            ft.addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            ft.addToBackStack(null);
        }
        ft.commit();
        mFragmentTagStack.add(tag);
    }

    @Override
    public void onBackStackChanged() {
        final FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() == mFragmentTagStack.size()) {
            return;
        }

        if (mFragmentTagStack.size() > 0) {
            final FragmentTransaction ft = fm.beginTransaction();
            final String tag = mFragmentTagStack.pop();
            if (fm.findFragmentByTag(tag) != null) {
                ft.remove(fm.findFragmentByTag(tag));
            }
            ft.commit();
        }
    }

    protected void restoreFragmentsState() {
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        if (mFragmentTagStack.size() == 0) {
            ft.show(fm.findFragmentByTag(FRAGMENT_KEY));
        } else {
            ft.hide(fm.findFragmentByTag(FRAGMENT_KEY));
            for (int i = 0; i < mFragmentTagStack.size(); i++) {
                String tag = mFragmentTagStack.get(i);
                Fragment f = fm.findFragmentByTag(tag);
                if (i + 1 < mFragmentTagStack.size()) {
                    ft.hide(f);
                } else {
                    ft.show(f);
                }
            }
        }
        ft.commit();
    }

    protected FragmentManager fragmentManager;
    protected ActionBar actionBar;

    protected FragmentManager getSafeFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        return fragmentManager;
    }

    protected ActionBar getSafeActionBar() {
        if (actionBar == null) {
            actionBar = this.getSupportActionBar();
        }
        return actionBar;
    }

    protected boolean isSafe() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this.isDestroyed()) {
                return false;
            }
        }
        if (this.isFinishing()) {
            return false;
        }
        return true;
    }


}
