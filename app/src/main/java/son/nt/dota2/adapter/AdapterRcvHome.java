package son.nt.dota2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterRcvHome extends RecyclerView.Adapter<AdapterRcvHome.ViewHolder> {

    String url = "http://38.media.tumblr.com/600b4ea2d2770bec97fd836a6b3c91f9/tumblr_n5u5px6X1Z1rwq84jo1_r1_400.gif";
    List<HeroEntry> mValues;
    Context context;
    private  final WeakReference<Context> contextWeakReference;
    public AdapterRcvHome (Context cx, List<HeroEntry> list) {
        this.mValues = list;
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hero_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final HeroEntry dto = mValues.get(i);

        viewHolder.txtName.setText(dto.fullName);
        if (dto.group.equalsIgnoreCase("Str")) {
            viewHolder.txtName.setBackgroundColor(context.getResources().getColor(R.color.strength));
        } else if (dto.group.equalsIgnoreCase("Agi")){
            viewHolder.txtName.setBackgroundColor(context.getResources().getColor(R.color.agi));
        } else {
            viewHolder.txtName.setBackgroundColor(context.getResources().getColor(R.color.intel));
        }
        if (contextWeakReference.get() != null) {
            Glide.with(viewHolder.imageView.getContext()).load(dto.avatarThumbnail)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageView);
        }

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OttoBus.post(dto);

            }
        });

    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtName;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.card_view);
            this.imageView = (ImageView) itemView.findViewById(R.id.row_avatar);
            this.txtName = (TextView) itemView.findViewById(R.id.row_name);

        }
    }
}
