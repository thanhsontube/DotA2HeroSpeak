package son.nt.dota2.story.add_simple_story;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.customview.HeroProfileView;
import son.nt.dota2.customview.SoundStoryView;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.story.search_sound.SearchSoundActivity;
import son.nt.dota2.utils.OttoBus;

public class AddSimpleStoryActivity extends BaseActivity implements AddSimpleStoryContract.View {


    AddSimpleStoryContract.Presenter mPresenter;

    @BindView(R.id.choose_hero)
    HeroProfileView mAvatar;

    @BindView(R.id.choose_sound_view)
    SoundStoryView mSoundStoryView;


    @BindView(R.id.des_txt)
    EditText mDesTxt;


    public static void start(Activity context, String side) {
        Intent intent = new Intent(context, AddSimpleStoryActivity.class);
        intent.putExtra("data", side);
        context.startActivity(intent);
    }

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_add_simple_story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddSimpleStoryPresenter(this, new HeroRepository());
        final String data = getIntent().getStringExtra("data");
        if (data.equalsIgnoreCase(MsConst.TYPE_SOUND_MIDDLE)) {
            ButterKnife.findById(this, R.id.choose_sound).setVisibility(View.GONE);
        }

        mAvatar.setVisibility(View.GONE);
        mDesTxt.clearFocus();

        mPresenter.setSide(data);
        OttoBus.register(mPresenter);
    }

    @Override
    protected void onDestroy() {
        OttoBus.unRegister(mPresenter);
        super.onDestroy();
    }

    @OnClick(R.id.choose_hero)
    public void onChooseHero() {
//        startActivity(new Intent(getApplicationContext(), ChooseHeroActivity.class));
    }

    @OnClick(R.id.choose_sound)
    public void onChooseSound() {
        startActivity(new Intent(getApplicationContext(), SearchSoundActivity.class));
    }


    @OnClick(R.id.add_confirm)
    public void onConfirmClick() {
        mPresenter.wrapSimpleStory(mDesTxt.getText().toString());
    }

    @Override
    public void updateAvatar(HeroBasicDto heroBasicDto) {
        mAvatar.setVisibility(View.VISIBLE);
        mAvatar.setData(heroBasicDto);
    }

    @Override
    public void updateSelectedSound(HeroResponsesDto data) {
        mSoundStoryView.setData(data);

    }

    @Override
    public void closeActivity() {
        finish();
    }
}
