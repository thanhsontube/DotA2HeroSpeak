package son.nt.dota2.customview;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 12/10/16.
 */

public class SoundView extends LinearLayout {


    @BindView(R.id.row_voice_no)
    TextView txtNo;
    @BindView(R.id.row_title)
    TextView title;
    @BindView(R.id.row_voice_text)
    TextView text;
    @BindView(R.id.row_voice_related)
    TextView relatedView;
    @BindView(R.id.row_voice_group)
    TextView voiceGroup;
    @BindView(R.id.row_voice_rival)
    ImageView imgFrom;
    @BindView(R.id.row_voice_to_icon)
    ImageView imgTo;
    @BindView(R.id.row_voice_main)
    View view;
    @BindView(R.id.row_voice_more)
    View moreView;

    public SoundView(Context context) {
        this(context, null);
    }

    public SoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.row_sound_2016, this);
        ButterKnife.bind(view);

        moreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void setData(@NonNull HeroResponsesDto dto, String no) {

        if (no.length() == 1) {
            no = no + "  ";
        } else if (no.length() == 2) {
            no = no + " ";
        }
        txtNo.setText(no);
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
            relatedView.setVisibility(View.GONE);
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

            relatedView.setVisibility(View.VISIBLE);
            relatedView.setText(related);
        }

        if (dto.isPlaying()) {
            view.setBackgroundResource(R.drawable.d_row_speaking);
        } else {
            view.setBackgroundResource(android.R.color.transparent);
        }


    }

}
