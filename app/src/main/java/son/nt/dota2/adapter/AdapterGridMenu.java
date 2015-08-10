package son.nt.dota2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.gridmenu.GridMenuItem;
import son.nt.dota2.ottobus_entry.GoLoginDto;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.SoundUtils;
import son.nt.dota2.youtube.FacebookManager;

/**
 * Created by 4210047 on 3/30/2015.
 */
public class AdapterGridMenu extends RecyclerView.Adapter<AdapterGridMenu.Holder> {
    private List<GridMenuItem> list;
    private Context context;

    public static final int CASE_RINGTONE = 0;
    public static final int CASE_NOTIFICATION = 1;
    public static final int CASE_SAVE = 2;
    public static final int CASE_COMMENTS = 3;
    public static final int CASE_SHARE = 4;
    SpeakDto speakDto = null;

    public AdapterGridMenu(Context context, List<GridMenuItem> list, SpeakDto dto) {
        this.list = list;
        this.context = context;
        this.speakDto = dto;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_grid_menu, parent, false);
        return new Holder(view, new Holder.IHolderListener() {
            @Override
            public void onClick(View v, int position) {
                listenerAdapter.onClick(position, list.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        GridMenuItem dto = list.get(position);
        if (!TextUtils.isEmpty(dto.title)) {
            holder.txtName.setText(dto.title);
        }
        holder.img.setImageResource(dto.iconID);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case CASE_RINGTONE:
                        new MaterialDialog.Builder(context)
                                .title("Confirm Set Ringtone")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        SoundUtils.setRingTone(context, speakDto);
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                        break;
                    case CASE_NOTIFICATION:
                        new MaterialDialog.Builder(context)
                                .title("Confirm Set Notification")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        SoundUtils.setNotificationSound(context, speakDto);
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                        break;

                    case CASE_COMMENTS:
                        if (FacebookManager.getInstance().isLogin()) {
                            Toast.makeText(context, "Loggined IN", Toast.LENGTH_SHORT).show();
                            GoLoginDto dto = new GoLoginDto(true);
                            dto.speakDto = speakDto;
                            OttoBus.post(dto);

                        } else {
                            Toast.makeText(context, "Please Login First", Toast.LENGTH_SHORT).show();
                            new MaterialDialog.Builder(context)
                                    .title("Please Login First")
                                    .positiveText("Login")
                                    .negativeText("Cancel")
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            super.onPositive(dialog);
                                            OttoBus.post(new GoLoginDto(false));
                                        }

                                        @Override
                                        public void onNegative(MaterialDialog dialog) {
                                            super.onNegative(dialog);
                                        }
                                    })
                                    .show();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName;
        View view;

        public Holder(View view, IHolderListener callback) {
            super(view);
            this.view = view;
            this.mListener = callback;
            img = (ImageView) view.findViewWithTag("image");
            txtName = (TextView) view.findViewWithTag("text");

        }

        IHolderListener mListener;

        public static interface IHolderListener {
            void onClick(View v, int position);
        }
    }

    public interface IAdapterCallback {
        void onClick(int position, GridMenuItem GridMenuItem);
    }

    IAdapterCallback listenerAdapter;

    public void setOnCallback(IAdapterCallback callback) {
        this.listenerAdapter = callback;
    }
}
