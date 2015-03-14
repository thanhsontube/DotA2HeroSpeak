package son.nt.dota2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.HeroDto;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class AdapterHeroList extends ArrayAdapter <HeroDto> {

    private List<HeroDto> list;
    LayoutInflater inflater;
    public AdapterHeroList(Context context, List<HeroDto> list){
        super(context, 0, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            holder = new Holder();
            v = inflater.inflate(R.layout.row_hero_list, parent, false);
            holder.img = (ImageView) v.findViewWithTag("icon");
            holder.text = (TextView) v.findViewWithTag("text");
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        HeroDto dto = list.get(position);
        return v;
    }

    static class Holder {
        ImageView img;
        TextView text;
    }
}
