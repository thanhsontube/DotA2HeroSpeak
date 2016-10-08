package son.nt.dota2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.glide.transformations.CropSquareTransformation;
import son.nt.dota2.R;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 7/7/15.
 * Adapter showed hero item (Basic) on the Home screen.
 */
public class AdapterRcvHome extends RecyclerView.Adapter<AdapterRcvHome.ViewHolder> {

    List<HeroEntry> mValues;
    Context context;
    private final WeakReference<Context> contextWeakReference;

    public AdapterRcvHome(Context cx, List<HeroEntry> list) {
        this.mValues = list;
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hero_list, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeroEntry dto = mValues.get(viewHolder.getAdapterPosition());
                Toast.makeText(viewHolder.txtName.getContext(), "" + dto.fullName, Toast.LENGTH_SHORT).show();

            }
        });

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeroEntry dto = mValues.get(viewHolder.getAdapterPosition());
                TsGaTools.trackHero("/hero:" + dto.heroId);
                OttoBus.post(dto);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final HeroEntry dto = mValues.get(i);

        viewHolder.txtName.setText(dto.name);
        if (dto.group.equalsIgnoreCase("Str")) {
            viewHolder.imageView.setBorderColor(Color.RED);
        } else if (dto.group.equalsIgnoreCase("Agi")) {
            viewHolder.imageView.setBorderColor(Color.GREEN);
        } else {
            viewHolder.imageView.setBorderColor(Color.BLUE);
        }
        if (contextWeakReference.get() != null) {
            Glide.with(viewHolder.imageView.getContext()).load(dto.avatarThumbnail)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropSquareTransformation(viewHolder.imageView.getContext()))
                    .into(viewHolder.imageView);
        }

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
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        com.makeramen.roundedimageview.RoundedImageView imageView;
        TextView txtName;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.card_view);
            this.imageView = (com.makeramen.roundedimageview.RoundedImageView) itemView.findViewById(R.id.row_avatar);
            this.txtName = (TextView) itemView.findViewById(R.id.row_name);

        }
    }
}
