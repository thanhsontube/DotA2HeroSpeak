package son.nt.dota2.gridmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import son.nt.dota2.CommentManager;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.comments.CommentDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.ottobus_entry.GoChatFragment;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsParse;

/**
 * Created by Sonnt on 8/10/15.
 */
public class CommentDialog extends DialogFragment {
    EditText editTextContent;
    TextInputLayout textInputLayout;
    SpeakDto speakDto;
    String imageAvatar = "";

    public static CommentDialog createInstance(SpeakDto dto) {
        CommentDialog d = new CommentDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", dto);
        d.setArguments(bundle);
        return d;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speakDto = (SpeakDto) getArguments().getSerializable("data");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.comment_dialog, null);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.cmt_text_input_layout);
        editTextContent = (EditText) view.findViewById(R.id.cmt_edt_think);
        ImageView avatar = (ImageView) view.findViewById(R.id.cmt_avatar);
        View submit = view.findViewById(R.id.cmt_submit);

        TextView txtFromName = (TextView) view.findViewById(R.id.cmt_from_name);
        final ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            txtFromName.setText(parseUser.getString("name"));
            if (ParseFacebookUtils.isLinked(parseUser)) {
                imageAvatar = parseUser.getString("avatar");
                Glide.with(this).load(imageAvatar)
                        .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
            }
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextContent.getText())) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Please say anything!");
                } else {
                    textInputLayout.setErrorEnabled(false);
                    CommentDto d = new CommentDto();
                    d.setSpeakDto(speakDto);
                    d.setCreateTime(System.currentTimeMillis());
                    d.setFromID(ParseUser.getCurrentUser().getUsername());
                    d.setFromName(ParseUser.getCurrentUser().getString("name"));
                    d.setImage(imageAvatar);

                    d.setMessage(editTextContent.getText().toString().trim());

                    pushOnServer(d);

                    dismiss();

                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);


        return alertDialog;
    }

    private void pushOnServer(final CommentDto d) {

        ParseObject p = new ParseObject(CommentDto.class.getSimpleName());
        p.put("message", d.getMessage());
        p.put("createTime", d.getCreateTime());
        p.put("fromID", d.getFromID());
        p.put("fromName", d.getFromName());
        p.put("fromImage", d.getImage());


        p.put("heroText", d.getSpeakDto().text);
        p.put("heroLink", d.getSpeakDto().link);
        p.put("heroID", d.getSpeakDto().heroId);
        p.put("heroGroup", d.getSpeakDto().voiceGroup);

        p.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(ResourceManager.getInstance().getContext(), "There is something wrong! Could n't push your comment on Server ! Sorry !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResourceManager.getInstance().getContext(), "Comment success !", Toast.LENGTH_SHORT).show();
                    TsParse.push(d);
                    CommentManager.getInstance().updateHistory();
                    OttoBus.post(new GoChatFragment(d.getSpeakDto().heroId));
                }

            }
        });

    }
}
