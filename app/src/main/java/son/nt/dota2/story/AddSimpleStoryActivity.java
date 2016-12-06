package son.nt.dota2.story;

import android.content.Intent;
import android.os.Bundle;

import butterknife.OnClick;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.story.choose_hero.ChooseHeroActivity;

public class AddSimpleStoryActivity extends BaseActivity {



    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_add_simple_story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @OnClick(R.id.choose_hero)
    public void onChooseHero () {
        startActivity(new Intent(getApplicationContext(), ChooseHeroActivity.class));

    }
}
