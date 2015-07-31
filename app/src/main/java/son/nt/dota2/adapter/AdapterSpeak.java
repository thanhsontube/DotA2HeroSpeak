package son.nt.dota2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.dto.SpeakDto;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class AdapterSpeak extends ArrayAdapter<SpeakDto> {

    private List<SpeakDto> list;
    LayoutInflater inflater;
    Context context;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_SPEAK = 1;

    public AdapterSpeak(Context context, List<SpeakDto> list) {
        super(context, 0, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTitle ? TYPE_TITLE : TYPE_SPEAK;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);
        Holder holder;
        if (v == null) {
            holder = new Holder();
            switch (type) {
                case TYPE_TITLE:
                    v = inflater.inflate(R.layout.row_speak_title, parent, false);
                    holder.title = (TextView) v.findViewById(R.id.row_title);
                    break;
                case TYPE_SPEAK:
                    v = inflater.inflate(R.layout.row_speak, parent, false);
                    holder.text = (TextView) v.findViewById(R.id.row_text);
                    holder.imgPlaying = (ImageView) v.findViewById(R.id.row_img_playing);
                    holder.imgMore = (ImageView) v.findViewById(R.id.row_img_more);
                    holder.imgMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer pos = (Integer) v.getTag();
                            createPopupMenu(v, pos);
                        }
                    });
                    holder.txtNo = (TextView) v.findViewById(R.id.row_txt_no);
                    break;
            }
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }

        SpeakDto dto = list.get(position);
        switch (type) {
            case TYPE_TITLE:


                if (dto.text != null) {
                    holder.title.setText(dto.text);
                }
                break;
            case TYPE_SPEAK:
                holder.imgMore.setTag(new Integer(position));
                String no = String.valueOf(position).trim();
                if (no.length() == 1) {
                    no = no + "  ";
                } else if (no.length() == 2) {
                    no = no + " ";
                }
                holder.txtNo.setText(no);
                if (dto.text != null) {
                    holder.text.setText(dto.text);
                }

                if (dto.isPlaying) {
                    v.setBackgroundResource(R.drawable.d_row_speaking);
                    holder.imgPlaying.setVisibility(View.VISIBLE);
                } else {
                    holder.imgPlaying.setVisibility(View.GONE);
                    v.setBackgroundResource(android.R.color.transparent);
                }

                break;
        }
        return v;
    }

    static class Holder {
        TextView txtNo;
        TextView title;
        TextView text;
        ImageView imgPlaying;
        ImageView imgMore;
    }

    private void createPopupMenu(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.menu_more);
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
                if (mListener != null) {
                    mListener.onMenuClick(action, position);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    MsConst.MenuSelect action;
    IAdapterListener mListener;

    public interface IAdapterListener {
        void onMenuClick(MsConst.MenuSelect action, int position);
    }

    public void setOnIAdapterListener(IAdapterListener callback) {
        this.mListener = callback;
    }
}
