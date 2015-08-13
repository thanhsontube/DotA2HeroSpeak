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

import java.util.ArrayList;
import java.util.List;

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

        list.add(new GridMenuItem("Make a Ringtone", R.drawable.ic_agi_24));
        list.add(new GridMenuItem("Set Notification Sound", R.drawable.ic_str_24));
        list.add(new GridMenuItem("Set Alarm ", R.drawable.ic_agi_24));
        list.add(new GridMenuItem("Comments", R.drawable.ic_more));
        list.add(new GridMenuItem("Share", R.drawable.ic_agi_24));
        list.add(new GridMenuItem("Like", R.drawable.ic_agi_24));
        list.add(new GridMenuItem("Make a Ringtone", R.drawable.ic_agi_24));

        adapter = new AdapterGridMenu(getActivity(), list, speakDto);
        recyclerView.setAdapter(adapter);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
