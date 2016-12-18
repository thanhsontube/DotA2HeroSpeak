package son.nt.dota2.adapter;

import com.afollestad.materialdialogs.MaterialDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import son.nt.dota2.FacebookManager;
import son.nt.dota2.R;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.gridmenu.GridMenuItem;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.RealmUtils;
import son.nt.dota2.utils.SoundUtils;
import son.nt.dota2.utils.TsGaTools;
import timber.log.Timber;

/**
 * Created by 4210047 on 3/30/2015.
 */
public class AdapterGridMenu extends RecyclerView.Adapter<AdapterGridMenu.Holder> {
    private List<GridMenuItem> list;
    private Context context;

    public static final int CASE_RINGTONE = 0;
    public static final int CASE_NOTIFICATION = 1;
    public static final int CASE_ALARM = 2;
    public static final int CASE_COPY = 3;
    public static final int CASE_PLAYLIST = 4;
    public static final int CASE_SHARE_OTHERS = 5;
//    public static final int CASE_SHARE_FACEBOOK = 5;
    ISound speakDto = null;
    AdapterGridMenuCallback mCallback;

    public interface AdapterGridMenuCallback {
        void onCheckWriteSettingPermission(ISound speakDto);
    }

    public AdapterGridMenu(Context context, List<GridMenuItem> list, ISound dto) {
        this.list = list;
        this.context = context;
        this.speakDto = dto;
    }

    public void setCallback(AdapterGridMenuCallback callback) {
        mCallback = callback;
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
            holder.img.setImageResource(dto.iconID);
            holder.txtName.setText(dto.title);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case CASE_RINGTONE:
                        Timber.d(">>>" + "CASE_RINGTONE:" + speakDto.getTitle());
                        TsGaTools.trackPages("/set_ringtone");
                        new MaterialDialog.Builder(context)
                                .title("Confirm Set Ringtone (Need the Write Setting Permission)")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);

                                        if (Build.VERSION.SDK_INT >= 23) {
                                            if (Settings.System.canWrite(context)) {
                                                Timber.d(">>>" + "can write");
                                                SoundUtils.setRingTone(context, speakDto);
                                            } else {
                                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                            }
                                        } else {
                                            if (mCallback != null) {
                                                mCallback.onCheckWriteSettingPermission(speakDto);
                                            }
                                        }

                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                        break;
                    case CASE_NOTIFICATION:
                        TsGaTools.trackPages("/set_notification");
                        new MaterialDialog.Builder(context)
                                .title("Confirm Set Notification (Need the Write Setting Permission)")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            if (Settings.System.canWrite(context)) {
                                                Timber.d(">>>" + "can write");
                                                SoundUtils.setNotificationSound(context, speakDto);
                                            } else {
                                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                            }
                                        } else {
                                            if (mCallback != null) {
                                                mCallback.onCheckWriteSettingPermission(speakDto);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                        break;

                    case CASE_ALARM:
                        TsGaTools.trackPages("/set_alarm");
                        new MaterialDialog.Builder(context)
                                .title("Confirm Set Alarm (Need the Write Setting Permission)")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            if (Settings.System.canWrite(context)) {
                                                Timber.d(">>>" + "can write");
                                                SoundUtils.setAlarmSound(context, speakDto);
                                            } else {
                                                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                            }
                                        } else {
                                            if (mCallback != null) {
                                                mCallback.onCheckWriteSettingPermission(speakDto);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                        break;

                    case CASE_COPY:
                        TsGaTools.trackPages("/set_copy");
                        FileUtil.copy(context, speakDto.getTitle(), speakDto.getTitle());
                        Toast.makeText(context, "copied:" + speakDto.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case CASE_PLAYLIST:
                        TsGaTools.trackPages("/set_playlist");

                        if (speakDto instanceof HeroResponsesDto) {
                            ((HeroResponsesDto) speakDto).setFavorite(true);
                            RealmUtils.setFavorite((HeroResponsesDto) speakDto);
                            Toast.makeText(context, "favorited: " + speakDto.getTitle(), Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case CASE_SHARE_OTHERS:
                        TsGaTools.trackPages("/set_share_others");
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
