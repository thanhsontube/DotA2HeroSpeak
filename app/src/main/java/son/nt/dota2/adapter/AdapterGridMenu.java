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

import son.nt.dota2.FacebookManager;
import son.nt.dota2.R;
import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.gridmenu.GridMenuItem;
import son.nt.dota2.ottobus_entry.GoLoginDto;
import son.nt.dota2.ottobus_entry.GoSaved;
import son.nt.dota2.ottobus_entry.GoShare;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.SoundUtils;

/**
 * Created by 4210047 on 3/30/2015.
 */
public class AdapterGridMenu extends RecyclerView.Adapter<AdapterGridMenu.Holder> {
    private List<GridMenuItem> list;
    private Context context;

    public static final int CASE_RINGTONE = 0;
    public static final int CASE_NOTIFICATION = 1;
    public static final int CASE_ALARM = 2;
    public static final int CASE_COMMENTS = 3;
    public static final int CASE_COPY = 4;
    public static final int CASE_PLAYLIST = 5;
    public static final int CASE_SHARE_FACEBOOK = 6;
    public static final int CASE_SHARE_OTHERS = 7;
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
        if (dto.title.equals("Add to Playlist")) {
            if (TsSqlite.getInstance().isInsert(speakDto.link)) {
                holder.img.setImageResource(dto.tempIcon);
                holder.txtName.setText(dto.tempTitle);
            } else {
                holder.img.setImageResource(dto.iconID);
                holder.txtName.setText(dto.title);
            }
        }
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

                    case CASE_ALARM:
                        new MaterialDialog.Builder(context)
                                .title("Confirm Set Alarm")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        SoundUtils.setAlarmSound(context, speakDto);
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
                            GoLoginDto dto = new GoLoginDto(true);
                            dto.speakDto = speakDto;
                            OttoBus.post(dto);

                        } else {
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

                    case CASE_COPY:
                        FileUtil.copy(context, speakDto.text, speakDto.text);
                        Toast.makeText(context, "copied:" + speakDto.text, Toast.LENGTH_SHORT).show();
                        break;

                    case CASE_PLAYLIST:
                        if (TsSqlite.getInstance().isInsert(speakDto.link)) {
                            new MaterialDialog.Builder(context)
                                    .title("Confirm Remove")
                                    .positiveText("Yes")
                                    .negativeText("Cancel")
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            super.onPositive(dialog);
                                            TsSqlite.getInstance().remove(speakDto.link);
                                            notifyDataSetChanged();
                                            OttoBus.post(new GoSaved());
                                        }

                                        @Override
                                        public void onNegative(MaterialDialog dialog) {
                                            super.onNegative(dialog);
                                        }
                                    })
                                    .show();
                        } else {
                            long insert  = TsSqlite.getInstance().insert(speakDto);
                            if (insert == -2) {
                                Toast.makeText(context, "This voice was added to Playlist before", Toast.LENGTH_SHORT).show();
                            } else if (insert > 0){
                                Toast.makeText(context, "Add to PlayList successful:" + speakDto.text, Toast.LENGTH_SHORT).show();
                                OttoBus.post(new GoSaved());
                            }
                            if (listenerAdapter != null) {
                                listenerAdapter.onClick(position, list.get(position));
                            }
                        }


                        break;
                    case CASE_SHARE_FACEBOOK:
                        //HeroActivity.goShare
                        OttoBus.post(new GoShare("facebook", speakDto));
                        break;
                    case CASE_SHARE_OTHERS:
                        FacebookManager.getInstance().shareViaTwitter(context, speakDto);

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
