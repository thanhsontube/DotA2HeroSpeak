package son.nt.dota2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.squareup.picasso.Picasso;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.HeroDto;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class AdapterHeroList extends ArrayAdapter <HeroDto> {

    private List<HeroDto> list;
    LayoutInflater inflater;
    Context context;
    AQuery aq;
    public AdapterHeroList(Context context, List<HeroDto> list){
        super(context, 0, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        aq = new AQuery(context);
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
        if(dto.name != null) {
            holder.text.setText(dto.name);
        }
        if(dto.avatarThubmail != null) {
//            aq.id(holder.img).image(dto.avatarThubmail, true, true);
            Picasso.with(context).load(dto.avatarThubmail).into(holder.img);
        }
        return v;
    }

    static class Holder {
        ImageView img;
        TextView text;
    }
}
