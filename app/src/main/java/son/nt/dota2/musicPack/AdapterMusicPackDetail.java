package son.nt.dota2.musicPack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterMusicPackDetail extends RecyclerView.Adapter<AdapterMusicPackDetail.ViewHolder> {

    private static final String TAG = AdapterMusicPackDetail.class.getSimpleName();
    List<MusicPackSoundDto> mValues;
    Context context;
    private final WeakReference<Context> contextWeakReference;

    public AdapterMusicPackDetail(Context cx) {
        this.context = cx;
        this.contextWeakReference = new WeakReference<>(cx);
    }

    public void setData(List<MusicPackSoundDto> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_music_pack_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final MusicPackSoundDto dto = mValues.get(position);

        String no = String.valueOf(position).trim();
        if (no.length() == 1) {
            no = no + "  ";
        } else if (no.length() == 2) {
            no = no + " ";
        }
        viewHolder.txtNo.setText(no);

        viewHolder.txtName.setText(dto.getName());

        if (true) {
            viewHolder.view.setBackgroundResource(R.drawable.d_row_speaking);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.view.setBackgroundResource(android.R.color.transparent);
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
        TextView txtNo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.row_voice_main);
            this.imageView = (ImageView) itemView.findViewById(R.id.row_voice_playing);
            this.txtName = (TextView) itemView.findViewById(R.id.row_voice_text);
            txtNo = (TextView) itemView.findViewById(R.id.row_voice_no);

        }
    }
}
