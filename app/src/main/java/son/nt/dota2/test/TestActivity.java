package son.nt.dota2.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import son.nt.dota2.R;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.jsoup.JsoupLoader;
import son.nt.dota2.musicPack.MusicPackListActivity;
import son.nt.dota2.musicPack.fav.MusicPackFavActivity;

public class TestActivity extends Activity implements View.OnClickListener {
    JsoupLoader mJsoupLoader = new JsoupLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.test_get_hero).setOnClickListener(this);
        findViewById(R.id.test_arc_voice).setOnClickListener(this);
        findViewById(R.id.test_arc_ability).setOnClickListener(this);
        findViewById(R.id.test_music_packs_list).setOnClickListener(this);
        findViewById(R.id.test_music_packs_list_download).setOnClickListener(this);
        findViewById(R.id.test_music_packs_list_details).setOnClickListener(this);
        findViewById(R.id.test_music_packs_list_read).setOnClickListener(this);
        findViewById(R.id.test_music_packs_default).setOnClickListener(this);
        findViewById(R.id.test_music_packs_fav).setOnClickListener(this);
        findViewById(R.id.hero_icons).setOnClickListener(this);
        findViewById(R.id.hero_avatar).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_get_hero:
                getHeroFromParse();
                break;
            case R.id.test_arc_voice:
                HTTPParseUtils.getInstance().withArcVoices("Arc_Warden");
                break;
            case R.id.test_arc_ability:
                HTTPParseUtils.getInstance().withArcAbility("Arc_Warden");
                break;
            case R.id.test_music_packs_list: {
                startActivity(new Intent(this, MusicPackListActivity.class));
                break;
            }
            case R.id.test_music_packs_list_download: {
                HTTPParseUtils.getInstance().withMusicPacksList();
                break;
            }

            case R.id.test_music_packs_list_details: {
                HTTPParseUtils.getInstance().withMusicPacksDetails2();
                break;
            }
            case R.id.test_music_packs_list_read: {
                HTTPParseUtils.getInstance().readObject();
                break;
            }
            case R.id.test_music_packs_default: {
                HTTPParseUtils.getInstance().withMusicPacksDetails("http://dota2.gamepedia.com/Music");
                break;
            }
            case R.id.test_music_packs_fav: {
                MusicPackFavActivity.startActivity(this);
                break;
            }
            case R.id.hero_icons: {
                mJsoupLoader.withGetHeroBasic_Icon();
                break;
            }
            case R.id.hero_avatar: {
                mJsoupLoader.withGetHeroBasic_Avatar_Description();
                break;
            }
        }
    }

    private void getHeroFromParse() {
        HTTPParseUtils.getInstance().withHeroListFromParse();
    }
}
