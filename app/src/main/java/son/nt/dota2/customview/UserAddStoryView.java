package son.nt.dota2.customview;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.R;
import son.nt.dota2.utils.DatetimeUtils;

/**
 * Created by sonnt on 12/10/16.
 */

public class UserAddStoryView extends LinearLayout {

    @BindView(R.id.image)
    RoundedImageView mAvatar;

    @BindView(R.id.name)
    TextView mNameView;

    @BindView(R.id.created_time)
    TextView mCreatedTimeView;

    @BindView(R.id.message)
    TextView mMessageView;


    public UserAddStoryView(Context context) {
        this(context, null);
    }

    public UserAddStoryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserAddStoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.include_user_add_story, this);
        ButterKnife.bind(view);
    }

    public void setData(String avatar, String name, String message, long createdTime) {

        Glide.with(getContext()).load(avatar)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mAvatar);

        mNameView.setText(name);
        mMessageView.setText(message);
        mCreatedTimeView.setText(DatetimeUtils.getTimeAgo(createdTime, getContext()));

    }


}
