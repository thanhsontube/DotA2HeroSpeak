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

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 7/9/15.
 */
public class AdapterSearchHero  extends RecyclerView.Adapter<AdapterSearchHero.ViewHolder> {

    Context context;
    List<HeroEntry> mValues;

    public AdapterSearchHero(Context context, List<HeroEntry> mValues) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_heroes, parent, false);
        return new ViewHolder(view);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        ImageView group;
        TextView name;
        TextView fullname;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
            this.group = (ImageView) itemView.findViewById(R.id.group);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.fullname = (TextView) itemView.findViewById(R.id.fullname);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final HeroEntry dto = mValues.get(position);
        viewHolder.name.setText(dto.name);
        viewHolder.fullname.setText(dto.fullName);
        Glide.with(viewHolder.avatar.getContext()).load(dto.avatarThumbnail)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.avatar);
        if (dto.group.equalsIgnoreCase("Str")) {
            viewHolder.group.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_str_40));
        } else if (dto.group.equalsIgnoreCase("Agi")){
            viewHolder.group.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_agi_40));
        } else {
            viewHolder.group.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_intel_40));
        }

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TsGaTools.trackPages("/search:" + dto.heroId);
                OttoBus.post(dto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
