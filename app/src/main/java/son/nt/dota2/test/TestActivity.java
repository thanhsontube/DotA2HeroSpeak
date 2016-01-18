package son.nt.dota2.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import son.nt.dota2.R;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;

public class TestActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.test_get_hero).setOnClickListener(this);
        findViewById(R.id.test_arc_voice).setOnClickListener(this);
        findViewById(R.id.test_arc_ability).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_get_hero:
                getHeroFromParse();
                break;
            case R.id.test_arc_voice:
                HTTPParseUtils.getInstance().withVoices("Arc_Warden");
                break;
            case R.id.test_arc_ability:
                HTTPParseUtils.getInstance().withAbility("Arc_Warden");
                break;
        }
    }

    private void getHeroFromParse() {
        HTTPParseUtils.getInstance().withHeroListFromParse();
    }
}
