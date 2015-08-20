package son.nt.dota2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by 4210047 on 3/20/2015.
 */
public class AdapterSaved extends RecyclerView.Adapter<AdapterSaved.Holder> {
    private Context context;
    List<SpeakDto> list;
    AQuery aq;
    public AdapterSaved(Context context, List<SpeakDto> list) {
        this.context = context;
        this.list = list;
        aq = new AQuery(context);

    }

    public static class Holder extends RecyclerView.ViewHolder{

        TextView txtText;
        TextView txtNo;
        TextView txtTime;
        ImageView imgHero;
        ImageView imgIcon;
        View view;
        int holderId;

        public Holder(View view, int viewType) {
            super(view);
            this.view = view;
            holderId = viewType;
            txtNo = (TextView) view.findViewById(R.id.row_txt_no);
            txtText = (TextView) view.findViewById(R.id.row_text);
            txtTime = (TextView) view.findViewById(R.id.row_saved_time);

            imgIcon = (ImageView) view.findViewById(R.id.row_img_more);
            imgHero = (ImageView) view.findViewById(R.id.row_img_saved_hero);

        }


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_saved, viewGroup, false);
        return new Holder(view, viewType);
    }


    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        SpeakDto dto = list.get(position);
        holder.txtText.setText(dto.text);
        holder.txtTime.setText(dto.voiceGroup);
        holder.txtNo.setText("" + dto.no);
        Glide.with(holder.imgHero.getContext()).load(HeroManager.getInstance().getHero(dto.heroId).avatarThumbnail)
                .centerCrop()
                .into(holder.imgHero);

        Glide.with(holder.imgIcon.getContext()).load(dto.rivalImage)
                .centerCrop()
                .into(holder.imgIcon);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).position = position;
                OttoBus.post(list.get(position));
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerAdapter != null) {
                    listenerAdapter.onClick(position, list.get(position));
                }
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listenerAdapter != null) {
                    listenerAdapter.onLongClick(position, list.get(position));
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface IAdapterCallback {
        void onClick(int position, SpeakDto dto);
        void onLongClick (int position, SpeakDto dto);
        void onMenuClick(MsConst.MenuSelect action, int position);
    }

    IAdapterCallback listenerAdapter;

    public void setOnCallback(IAdapterCallback callback) {
        this.listenerAdapter = callback;
    }


    //popup menu
    MsConst.MenuSelect action;


    private void createPopupMenu(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.menu_more_saved);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.more_share:
                        action = MsConst.MenuSelect.FB_SHARE;
                        break;
                    case R.id.more_save:
                        action = MsConst.MenuSelect.FAVORITE;
                        break;
                    case R.id.more_copy:
                        action = MsConst.MenuSelect.COPY;
                        break;
                    case R.id.more_set_ringtone:
                        action = MsConst.MenuSelect.RINGTONE;
                        break;

                }
                if (listenerAdapter != null) {
                    listenerAdapter.onMenuClick(action, position);
                }
                return false;
            }
        });
        popupMenu.show();
    }


}
