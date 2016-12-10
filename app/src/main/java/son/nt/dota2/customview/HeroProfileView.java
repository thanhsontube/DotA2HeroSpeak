package son.nt.dota2.customview;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import son.nt.dota2.R;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 12/10/16.
 */

public class HeroProfileView extends LinearLayout {

    @BindView(R.id.hero_profile_image)
    RoundedImageView mAvatar;

    @BindView(R.id.row_sub_image)
    ImageView mSubImage;

    @BindView(R.id.row_name)
    TextView mNameView;

    public HeroProfileView(Context context) {
        this(context, null);
    }

    public HeroProfileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeroProfileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.include_hero_profile, this);
        ButterKnife.bind(view);
    }

    public void setData(@NonNull HeroBasicDto dto) {

        mNameView.setText(dto.name);
        if (dto.group != null) {
            if (dto.group.equalsIgnoreCase("Str")) {
                mAvatar.setBorderColor(Color.RED);
            } else if (dto.group.equalsIgnoreCase("Agi")) {
                mAvatar.setBorderColor(Color.GREEN);
            } else {
                mAvatar.setBorderColor(Color.BLUE);
            }
        }

        Glide.with(getContext()).load(dto.avatar)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropSquareTransformation(getContext()))
                .into(mAvatar);

        if (TextUtils.isEmpty(dto.heroIcon)) {
            mSubImage.setVisibility(GONE);
        } else {
            mSubImage.setVisibility(VISIBLE);
            Glide.with(getContext()).load(dto.heroIcon)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropSquareTransformation(getContext()))
                    .into(mSubImage);
        }


    }

    public void setAvatar(String avatar) {
        mAvatar.setBorderColor(Color.WHITE);
        Glide.with(getContext()).load(avatar)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropSquareTransformation(getContext()))
                .into(mAvatar);

        mNameView.setVisibility(GONE);
        mSubImage.setVisibility(GONE);
    }
}
