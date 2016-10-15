package son.nt.dota2.activity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import son.nt.dota2.FacebookManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.comments.ChatDialog;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.fragment.HeroFragment;
import son.nt.dota2.gridmenu.CommentDialog;
import son.nt.dota2.gridmenu.GridMenuDialog;
import son.nt.dota2.gridmenu.SpeakLongClick;
import son.nt.dota2.ottobus_entry.GoLoginDto;
import son.nt.dota2.ottobus_entry.GoShare;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;

public class HeroActivity extends AActivity implements HeroFragment.OnFragmentInteractionListener {
    HeroEntry heroEntry;
    FloatingActionButton fabChat;

    private String mHeroId;


    public static void startActivity(Context context, String heroID) {
        Intent intent = new Intent(context, HeroActivity.class);
        intent.putExtra("data", heroID);
        context.startActivity(intent);
    }

    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return HeroFragment.newInstance(heroEntry);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.hero_main_ll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        heroEntry = (HeroEntry) getIntent().getExtras().getSerializable(MsConst.EXTRA_HERO);
        mHeroId = getIntent().getExtras().getString("data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);
        fabChat = (FloatingActionButton) findViewById(R.id.btn_chat_hero);
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TsGaTools.trackPages(MsConst.TRACK_CHAT);
                FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
                Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
                if (f != null) {
                    ft.remove(f);
                }
                ChatDialog dialog = ChatDialog.newInstance();
                ft.add(dialog, "chat");
                ft.commit();
            }
        });
        OttoBus.register(this);
        adMob();
        isAddMob();

    }

    public void isAddMob() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Admob");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    return;
                }
                String enable = parseObject.getString("isEnable");
                if (enable.equals("off")) {
                    AdMobUtils.hide();
                } else {
                    AdMobUtils.show();
                }
            }
        });

    }

    private void adMob() {
        AdMobUtils.init(findViewById(R.id.ll_ads), R.id.adView);
        AdMobUtils.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hero, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            if (mFragmentTagStack.size() == 0) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Subscribe
    public void voiceLongItemClick(SpeakLongClick dto) {
        FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
        Fragment f = getSafeFragmentManager().findFragmentByTag("long-click");
        if (f != null) {
            ft.remove(f);
        }
        GridMenuDialog dialog = GridMenuDialog.newInstance(dto.speakDto);
        ft.add(dialog, "long-click");
        ft.commit();
    }

    @Subscribe
    public void goLogin(GoLoginDto dto) {
        if (!dto.isLogin) {

            startActivity(LoginActivity.getIntent(HeroActivity.this));
        } else {
            FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
            Fragment f = getSafeFragmentManager().findFragmentByTag("cmt");
            if (f != null) {
                ft.remove(f);
            }
            CommentDialog dialog = CommentDialog.createInstance(dto.speakDto);
            ft.add(dialog, "cmt");
            ft.commit();
        }
    }

    @Subscribe
    public void goShare(GoShare dto) {
        if (dto.type.equals("facebook")) {
            FacebookManager.getInstance().shareViaDialogFb(HeroActivity.this, dto.speakDto);
        }
    }

//    @Subscribe
//    public void goChatDialog (CommentDto dto) {
//        FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
//        Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
//        if (f != null) {
//            ft.remove(f);
//        }
//        ChatDialog dialog = ChatDialog.newInstance();
//        ft.add(dialog, "chat");
//        ft.commit();
//    }


}
