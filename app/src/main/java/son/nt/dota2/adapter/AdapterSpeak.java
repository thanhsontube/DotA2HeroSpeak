package son.nt.dota2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

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
                    break;
            }
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        SpeakDto dto = list.get(position);
        switch (type) {
            case TYPE_TITLE:

                if (dto.title != null) {
                    holder.title.setText(dto.title);
                }
                break;
            case TYPE_SPEAK:
                if (dto.text != null) {
                    holder.text.setText(dto.text);
                }
                break;
        }
        return v;
    }

    static class Holder {
        TextView title;
        TextView text;
    }
}
