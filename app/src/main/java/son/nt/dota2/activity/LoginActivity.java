package son.nt.dota2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.save.SaveBasicHeroData;
import son.nt.dota2.fragment.LoginFragment;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;

public class LoginActivity extends AActivity {

    public static final String TAG = "LoginActivity";
    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return LoginFragment.newInstance("", "");
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_ll_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HeroManager.getInstance().initDataSelf();
        HTTPParseUtils.getInstance().setCallback(new HTTPParseUtils.IParseCallBack() {
            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Finish>>>>", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.pre_heroList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPParseUtils.getInstance().withHeroListFromParse();

            }
        });

        findViewById(R.id.pre_hero_ability).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AObject a = FileUtil.getObject(getApplicationContext(), SaveBasicHeroData.class.getSimpleName());
                    SaveBasicHeroData saveBasicHeroData = (SaveBasicHeroData) a;
                    int i = 0;
                    for (HeroEntry p : saveBasicHeroData.list) {
                        Logger.debug(TAG, ">>>" + i ++ + " name:" + p.heroId + ";group" +p.group);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.pre_hero_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        int i = 1;
        Logger.debug(TAG, ">>>" + "============LIST NAME ===== size" + HeroManager.getInstance().listHeroes.size());
        for (HeroEntry p :HeroManager.getInstance().listHeroes) {
            Logger.debug(TAG, ">>>" + i ++  + ";name:" + p.heroId + ";Full:" + p.fullName);
        }

//        if (BuildConfig.DEBUG) {
//            startActivity(new Intent(this, HomeActivity.class));
//            finish();
//        }
    }

    private void testAbilities() {
        HTTPParseUtils.getInstance().withHeroListBasic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_push_step1:
                break;
            case R.id.action_push_step2:
                break;
            case R.id.action_push_step3:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent (Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
