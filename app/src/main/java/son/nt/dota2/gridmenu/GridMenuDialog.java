package son.nt.dota2.gridmenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.share.widget.LikeView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterGridMenu;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 8/7/15.
 */
public class GridMenuDialog extends DialogFragment {
    public static final String TAG = "GridMenuDialog";
    SpeakDto speakDto;
    RecyclerView recyclerView;
    AdapterGridMenu adapter;
    List<GridMenuItem> list = new ArrayList<>();
    public static GridMenuDialog newInstance (SpeakDto dto) {
        GridMenuDialog dialog = new GridMenuDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", dto);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog getDialog() {

        return super.getDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        speakDto = (SpeakDto) getArguments().getSerializable("data");
        Logger.debug(TAG, ">>>" + "onCreateDialog:" + speakDto.heroId);

        LayoutInflater  layoutInflater  = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.grid_menu,null);

        TextView txtContent = (TextView) view.findViewById(R.id.grid_menu_text);
        txtContent.setText(speakDto.text);

        LikeView likeView = (LikeView) view.findViewById(R.id.grid_menu_like);
        likeView.setLikeViewStyle(LikeView.Style.BOX_COUNT);
        likeView.setObjectIdAndType(MsConst.FB_ID_POST_TO, LikeView.ObjectType.PAGE);
        likeView.setFragment(this);

        View viewClose = view.findViewById(R.id.grid_menu_close);
        viewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.grid_menu_recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        list.add(new GridMenuItem("Make a Ringtone", R.drawable.ic_music_48));
        list.add(new GridMenuItem("Set Notification Sound", R.drawable.ic_alert_48));
        list.add(new GridMenuItem("Set Alarm ", R.drawable.ic_alarm_48));
        list.add(new GridMenuItem("Comments", R.drawable.ic_comment_48));
        list.add(new GridMenuItem("Copy Text", R.drawable.ic_copy_48));
        GridMenuItem playList = new GridMenuItem("Add to Playlist", R.drawable.ic_start_off_48);
        playList.tempIcon = R.drawable.ic_start_on_48;
        playList.tempTitle = "Remove out Playlist";
        list.add(playList);
        list.add(new GridMenuItem("Facebook Share", R.drawable.ic_facebook_48));
        list.add(new GridMenuItem("Other Share", R.drawable.ic_share_48));

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (!ParseFacebookUtils.isLinked(parseUser)) {
            likeView.setVisibility(View.INVISIBLE);
        }

        adapter = new AdapterGridMenu(getActivity(), list, speakDto);
        adapter.setOnCallback(new AdapterGridMenu.IAdapterCallback() {
            @Override
            public void onClick(int position, GridMenuItem dto) {
                if (dto.title.equals("Add to Playlist")) {
                    dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
