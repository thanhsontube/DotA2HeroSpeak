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
import com.androidquery.callback.ImageOptions;

import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.data.SaveDto;

/**
 * Created by 4210047 on 3/20/2015.
 */
public class AdapterSaved extends RecyclerView.Adapter<AdapterSaved.Holder> {
    private Context context;
    List<SaveDto> list;
    AQuery aq;
    public AdapterSaved(Context context, List<SaveDto> list) {
        this.context = context;
        this.list = list;
        aq = new AQuery(context);

    }

    public static class Holder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView txtText;
        TextView txtNo;
        TextView txtTime;
        ImageView imgHero;
        ImageView imgIcon;
        View view;
        int holderId;

        public Holder(View view, int viewType, IHolderListener callback) {
            super(view);
            this.mListener = callback;
            this.view = view;
            this.view.setOnClickListener(this);
            holderId = viewType;
            txtNo = (TextView) view.findViewById(R.id.row_txt_no);
            txtText = (TextView) view.findViewById(R.id.row_text);
            txtTime = (TextView) view.findViewById(R.id.row_saved_time);

            imgIcon = (ImageView) view.findViewById(R.id.row_img_more);
            imgHero = (ImageView) view.findViewById(R.id.row_img_saved_hero);

            imgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoreClick(v, getPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v, getPosition());
            }
        }
        IHolderListener mListener;
        public static interface IHolderListener {
            void onClick(View v, int position);
            void onMoreClick(View v, int position);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Holder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.row_saved, viewGroup, false);
        holder = new Holder(view, viewType,listener);
        return holder;
    }

    Holder.IHolderListener listener = new Holder.IHolderListener() {
        @Override
        public void onClick(View v, int position) {
            if (listenerAdapter != null) {
                listenerAdapter.onClick(position, list.get(position));
            }

        }

        @Override
        public void onMoreClick(View v, int position) {
            if (listenerAdapter != null) {
                createPopupMenu(v, position);
            }
        }
    };

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SaveDto dto = list.get(position);
        holder.txtText.setText(dto.speakContent);
        holder.txtTime.setText(dto.saveTime);
        holder.txtNo.setText(dto.no);
        ImageOptions options = new ImageOptions();
        options.round = 90;
        aq.id(holder.imgHero).image(dto.heroLink, options);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface IAdapterCallback {
        void onClick(int position, SaveDto dto);
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
