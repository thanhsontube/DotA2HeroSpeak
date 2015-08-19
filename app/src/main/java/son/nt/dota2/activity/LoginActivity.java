package son.nt.dota2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.fragment.LoginFragment;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.Logger;

public class LoginActivity extends AActivity {

    public static final String TAG = "LoginActivity";
    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return LoginFragment.newInstance("", "");
    }
    List<HeroEntry> listTemp = new ArrayList<>();
    boolean isAbi = false;

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
//                Toast.makeText(getApplicationContext(), "Finish>>>>", Toast.LENGTH_SHORT).show();
                if (isAbi) {
                    Logger.debug(TAG, ">>>" + "Abi done:" + listTemp.get(listTemp.size() - 1).heroId + ";pos:" + listTemp.size());
                    listTemp.remove(listTemp.size() - 1);
                    if (listTemp.size() > 0) {
                        HTTPParseUtils.getInstance().withAbility(listTemp.get(listTemp.size() - 1).heroId);
                    } else {
                        Toast.makeText(getApplicationContext(), "We have done with Ability List", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.pre_heroList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPParseUtils.getInstance().withHeroListFromParse();

            }
        });

        findViewById(R.id.pre_hero_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HTTPParseUtils.getInstance().withBasicBg();

            }
        });

        findViewById(R.id.pre_hero_ability).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAbi = true;
                listTemp.clear();
                listTemp.addAll(HeroManager.getInstance().listHeroes);
                HTTPParseUtils.getInstance().withAbility(listTemp.get(listTemp.size() -1).heroId);

//                for (HeroEntry p : HeroManager.getInstance().listHeroes) {
//                    HTTPParseUtils.getInstance().withVoices(p.heroId);
//                }

            }
        });

        findViewById(R.id.pre_hero_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HeroEntry p : HeroManager.getInstance().listHeroes) {
//                    HTTPParseUtils.getInstance().withVoices(p.heroId);
                }

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

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
