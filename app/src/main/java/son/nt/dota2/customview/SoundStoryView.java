package son.nt.dota2.customview;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.ottobus_entry.GoVoice;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by sonnt on 12/7/16.
 */

public class SoundStoryView extends LinearLayout {

    TextView txtNo;
    TextView title;
    TextView text;
    TextView related;
    TextView voiceGroup;
    ImageView imgFrom;
    ImageView imgTo;
    View view;

    private ISound mSound;

    public SoundStoryView(Context context) {
        this(context, null);
    }

    public SoundStoryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundStoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(this.getContext()).inflate(R.layout.row_voice_2, this, true);


        txtNo = (TextView) findViewById(R.id.row_voice_no);
        related = (TextView) findViewById(R.id.row_voice_related);
        voiceGroup = (TextView) findViewById(R.id.row_voice_group);
        text = (TextView) findViewById(R.id.row_voice_text);
        imgTo = (ImageView) findViewById(R.id.row_voice_to_icon);
        imgFrom = (ImageView) findViewById(R.id.row_voice_rival);
        view = findViewById(R.id.row_voice_main);
        title = (TextView) findViewById(R.id.row_title);
        view.setOnClickListener(v -> {
            if (mSound == null) {
                return;
            }
            OttoBus.post(new GoVoice(mSound, false));

        });
        setData(null);

        if (this.isInEditMode()) {
            return;
        }
    }

    public void setData(HeroResponsesDto dto) {
        this.mSound = dto;
        view.setVisibility(dto == null ? GONE : VISIBLE);
        if (dto == null) {
            return;
        }
        if (dto.text != null) {
            text.setText(dto.getText());
        }

        voiceGroup.setText(TextUtils.isEmpty(dto.getVoiceGroup()) ? "Unknown" : dto.getVoiceGroup());

        if (dto.isPlaying()) {
            view.setBackgroundResource(R.drawable.d_row_speaking);
        } else {
            view.setBackgroundResource(android.R.color.transparent);
        }

        final boolean isSwap = dto.getVoiceGroup().endsWith(MsConst.LORD_KILLING) || dto.getVoiceGroup().endsWith(MsConst.LORD_MEETING);
        final String fromIcon = isSwap
                ? dto.getToHeroIcon() : dto.getHeroIcon();

        final String toIcon = isSwap
                ? dto.getHeroIcon() : dto.getToHeroIcon();
        Glide.with(getContext()).load(fromIcon)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgFrom);
        String related = " -> ";

        if (TextUtils.isEmpty(dto.toHeroId)) {
            imgTo.setVisibility(View.GONE);
            this.related.setVisibility(View.GONE);
        } else {
            imgTo.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(toIcon)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgTo);

            if (dto.isAlliMeetingGroup() || dto.getVoiceGroup().equals(MsConst.LORD_MEETING)) {

                related = " " + getContext().getString(R.string.meets) + " ";
            }

            if (dto.isEnemiesKillingGroup() || dto.getVoiceGroup().equals(MsConst.LORD_KILLING)) {
                related = " " + getContext().getString(R.string.killed) + " ";
            }

            if (dto.getVoiceGroup().contains(MsConst.BUYS_ITEMS)) {
                related = " " + getContext().getString(R.string.buy) + " ";
            }

            this.related.setVisibility(View.VISIBLE);
            this.related.setText(related);
        }
    }
}
