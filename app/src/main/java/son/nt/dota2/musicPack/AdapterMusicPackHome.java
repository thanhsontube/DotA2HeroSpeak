package son.nt.dota2.musicPack;

import android.content.Context;
import android.graphics.Color;
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
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterMusicPackHome extends RecyclerView.Adapter<AdapterMusicPackHome.ViewHolder> {

    private static final String TAG = AdapterMusicPackHome.class.getSimpleName();
    List<MusicPackDto> mValues;
    Context context;
    private final WeakReference<Context> contextWeakReference;

    public AdapterMusicPackHome(Context cx) {
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }

    public void setData(List<MusicPackDto> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_music_pack_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final MusicPackDto dto = mValues.get(i);

        viewHolder.txtName.setText(dto.getName());
        Logger.debug(TAG, ">>>" + "dto.getHref():" + dto.getHref());

        if (contextWeakReference.get() != null) {
            Glide.with(viewHolder.imageView.getContext()).load(dto.getHref())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageView);
            if (i == 0) {
                viewHolder.viewCover.setBackgroundColor(Color.TRANSPARENT);
            } else {

                viewHolder.viewCover.setBackgroundColor(Color.parseColor(dto.getCoverColor()));
            }
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
        return mValues == null ? 0 : mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName;
        View view;
        View viewCover;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.row_music_pack_home_image_view);
            this.viewCover = itemView.findViewById(R.id.row_music_pack_home_cover);
            this.imageView = (ImageView) itemView.findViewById(R.id.row_music_pack_home_image);
            this.txtName = (TextView) itemView.findViewById(R.id.row_music_pack_home_name);

        }
    }
}
