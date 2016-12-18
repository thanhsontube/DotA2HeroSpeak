package son.nt.dota2.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.HeroActivity;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 7/7/15.
 * Adapter showed hero item (Basic) on the Home screen.
 */
public class HeroListAdapter extends RecyclerView.Adapter<HeroListAdapter.ViewHolder> {

    List<HeroBasicDto> mValues;
    Context mContext;


    public HeroListAdapter(Context cx) {
        this.mContext = cx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hero_list, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.txtName.setOnClickListener(v -> {
            HeroBasicDto dto = mValues.get(viewHolder.getAdapterPosition());
            Toast.makeText(viewHolder.txtName.getContext(), "" + dto.fullName, Toast.LENGTH_SHORT).show();

        });

        viewHolder.view.setOnClickListener(v -> {
            HeroBasicDto dto = mValues.get(viewHolder.getAdapterPosition());
            TsGaTools.trackHero("/hero:" + dto.heroId);
            HeroActivity.startActivity(mContext, dto);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final HeroBasicDto dto = mValues.get(i);

        viewHolder.txtName.setText(dto.name);
        if (dto.group != null) {
            if (dto.group.equalsIgnoreCase(MsConst.GROUP_STR)) {
                viewHolder.imageView.setBorderColor(Color.RED);
            } else if (dto.group.equalsIgnoreCase(MsConst.GROUP_AGI)) {
                viewHolder.imageView.setBorderColor(Color.GREEN);
            } else {
                viewHolder.imageView.setBorderColor(Color.BLUE);
            }
        }
        Glide.with(viewHolder.imageView.getContext()).load(dto.avatar)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);

//        viewHolder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TsGaTools.trackHero("/hero:" + dto.heroId);
//                OttoBus.post(dto);
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setData(List<HeroBasicDto> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView txtName;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.card_view);
            this.imageView = (RoundedImageView) itemView.findViewById(R.id.row_avatar);
            this.txtName = (TextView) itemView.findViewById(R.id.row_name);

        }
    }
}
