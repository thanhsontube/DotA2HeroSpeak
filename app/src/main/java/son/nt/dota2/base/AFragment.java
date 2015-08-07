package son.nt.dota2.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.loader.MyPath;
import son.nt.dota2.loader.base.ContentManager;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class AFragment extends Fragment {
    protected Context context;
    protected ResourceManager resource;
    protected ContentManager contentManager;
    protected MyPath mypath;
    protected AQuery aq;
    protected ActionBar actionBar;
    protected FragmentManager fragmentManager;
    protected TsSqlite sqlite;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        resource = ResourceManager.getInstance();
        contentManager = resource.getContentManager();
        mypath = resource.getMyPath();
        aq = new AQuery(context);
        sqlite = TsSqlite.getInstance();
        actionBar = getSafeActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    protected FragmentManager getSafeFragmentManager() {
        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        return fragmentManager;
    }

    protected ActionBar getSafeActionBar() {
        if (actionBar == null) {
            actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        }
        return actionBar;
    }
    protected AppCompatActivity getSaveActivity () {
        return (AppCompatActivity)getActivity();
    }

    protected boolean isSafe()
    {
        if (this.isRemoving() || this.getActivity() == null || this.isDetached() || !this.isAdded()
                || this.getView() == null)
        {
            return false;
        }

        return true;
    }

    protected void setTitle(String title) {
        getSafeActionBar().setTitle(title);
    }


}
