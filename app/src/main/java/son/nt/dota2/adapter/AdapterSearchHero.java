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
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/9/15.
 */
public class AdapterSearchHero  extends RecyclerView.Adapter<AdapterSearchHero.ViewHolder> {

    Context context;
    List<HeroDto> mValues;

    public AdapterSearchHero(Context context, List<HeroDto> mValues) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        ImageView group;
        TextView name;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
            this.group = (ImageView) itemView.findViewById(R.id.group);
            this.name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final HeroDto dto = mValues.get(position);
        viewHolder.name.setText(dto.name);
        Glide.with(viewHolder.avatar.getContext()).load(dto.avatarThubmail)
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
                OttoBus.post(dto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
