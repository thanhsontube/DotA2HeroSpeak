package son.nt.dota2.customview;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import son.nt.dota2.R;

/**
 * Created by sonnt on 12/10/16.
 * CommentView duoc su dung o comment story screen.
 */

public class CommentView extends LinearLayout {

    @BindView(R.id.cmts_image)
    RoundedImageView mAvatar;

    @BindView(R.id.cmts_message)
    EditText messageView;


    ICommentViewCallback mCallback;

    public interface ICommentViewCallback {
        void onSendClick(String mess);
    }

    public void setCallback(ICommentViewCallback callback) {
        mCallback = callback;
    }

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.include_commend_view, this);
        ButterKnife.bind(view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Glide.with(getContext()).load(user.getPhotoUrl())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mAvatar);
        }
    }

    @OnClick(R.id.cmts_send_btn)
    public void onSendClick() {
        final String msg = messageView.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(getContext(), "Error ! Message is empty !", Toast.LENGTH_SHORT).show();
            return;

        }
        if (mCallback != null) {
            mCallback.onSendClick(msg);
            messageView.setText("");
            messageView.clearFocus();
        }

    }

}
