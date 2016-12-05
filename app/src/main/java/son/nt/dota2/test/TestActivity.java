package son.nt.dota2.test;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.firebase.FireBaseActivity;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.jsoup.JsoupLoader;
import son.nt.dota2.musicPack.MusicPackListActivity;
import son.nt.dota2.musicPack.fav.MusicPackFavActivity;
import son.nt.dota2.utils.Logger;

public class TestActivity extends FireBaseActivity implements View.OnClickListener {
    private static final String TAG = TestActivity.class.getSimpleName();
    JsoupLoader mJsoupLoader = new JsoupLoader();

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        findViewById(R.id.hero_upload_icons).setOnClickListener(this);
        findViewById(R.id.hero_lord).setOnClickListener(this);
        findViewById(R.id.push_hero_lord).setOnClickListener(this);
        findViewById(R.id.hero_response).setOnClickListener(this);
        findViewById(R.id.items).setOnClickListener(this);
        findViewById(R.id.abilities).setOnClickListener(this);
        findViewById(R.id.underlord_abilities).setOnClickListener(this);
        findViewById(R.id.old_comments).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.old_comments:
                mJsoupLoader.getCmts();
                break;
            case R.id.underlord_abilities:
                mJsoupLoader.getNewAbilities();
                break;
            case R.id.abilities:
                mJsoupLoader.getAbilities();
                break;
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
            case R.id.hero_upload_icons: {
//                Realm realm = Realm.getDefaultInstance();
//                RealmResults<HeroBasicDto> heroBasicDtos = realm.where(HeroBasicDto.class).findAll();
//
//                uploadHeroBasic(heroBasicDtos);
                break;
            }
            case R.id.hero_lord: {
                mJsoupLoader.withGetHeroBasic_Lord();
                break;
            }
            case R.id.push_hero_lord: {
                mJsoupLoader.withGetHeroBasic_PUSHLord();
                break;
            }
            case R.id.hero_response: {
                mJsoupLoader.withGetHeroBasic_Response();
                break;
            }
            case R.id.items: {
                mJsoupLoader.withGetItems();
                break;
            }
        }
    }

    private void getHeroFromParse() {
        HTTPParseUtils.getInstance().withHeroListFromParse();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_test;
    }


    //firebase

    /**
     * upload heroID< HeroName < heroIcon < hero Avatar1 to Firebase.
     */

    private void uploadHeroBasic(List<HeroBasicDto> list) {
        Logger.debug(TAG, ">>>" + "uploadHeroBasic:" + list.size());
//        if (!list.isEmpty()) {
//            String icon = list.get(0).heroIcon;
//            Logger.debug(TAG, ">>>" + "icon 0:" + list.get(0).toString());
//        }
//
//        HeroBasicDto heroBasicDto = list.get(0);

//        new Upload().execute(heroBasicDto);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        HeroBasicDto heroBasicDto = new HeroBasicDto();
        heroBasicDto.fullName = "This is FULL name 2";
        reference.child(HeroBasicDto.class.getSimpleName()).push().setValue(heroBasicDto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Logger.debug(TAG, ">>>" + "onSuccess 2");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Logger.error(TAG, ">>> Error:" + "onFailure:" + e);

                    }
                })
        ;


    }

    class Upload extends AsyncTask<HeroBasicDto, Void, Void> {
        @Override
        protected Void doInBackground(HeroBasicDto... params) {
            pushAHeroBasic(params[0]);
            return null;
        }
    }

    private void pushAHeroBasic(HeroBasicDto heroBasicDto) {
        Logger.debug(TAG, ">>>" + "pushAHeroBasic");
//        //Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");


        databaseReference.child("DoTa2HeroBasic").push().setValue(heroBasicDto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Logger.debug(TAG, ">>>" + "onComplete");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Logger.debug(TAG, ">>>" + "fail:" + e);

            }
        });
    }
}

