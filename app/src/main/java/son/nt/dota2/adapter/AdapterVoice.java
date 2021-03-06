package son.nt.dota2.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Random;

import son.nt.dota2.R;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.gridmenu.SpeakLongClick;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AdapterVoice extends RecyclerView.Adapter<AdapterVoice.Holder> {

    public static final String TAG = "AdapterVoice";

    private List<SpeakDto> list;
    LayoutInflater inflater;
    Context context;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_SPEAK = 1;
    ColorStateList sColorStatePlaying;

    public AdapterVoice(Context context, List<SpeakDto> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_TITLE:
                view = inflater.inflate(R.layout.row_speak_title, parent, false);
                break;
            case TYPE_SPEAK:
                view = inflater.inflate(R.layout.row_voice, parent, false);
                break;


        }
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final SpeakDto dto = list.get(position);
        switch (getItemViewType(position)) {
            case TYPE_SPEAK:
                String no = String.valueOf(position).trim();
                if (no.length() == 1) {
                    no = no + "  ";
                } else if (no.length() == 2) {
                    no = no + " ";
                }
                holder.txtNo.setText(no);
                if (dto.text != null) {
                    holder.text.setText(dto.text);
                }

                if (dto.isPlaying) {
                    holder.view.setBackgroundResource(R.drawable.d_row_speaking);
                    holder.imgPlaying.setVisibility(View.VISIBLE);
                } else {
                    holder.imgPlaying.setVisibility(View.GONE);
                    holder.view.setBackgroundResource(android.R.color.transparent);
                }

                Glide.with(holder.imgRival.getContext()).load(dto.rivalImage)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgRival);

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TsGaTools.trackPages("/row_voice_1_click");
                        list.get(position).position = position;
                        OttoBus.post(list.get(position));
                    }
                });

                holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        TsGaTools.trackPages("/row_voice_long_click");
                        OttoBus.post(new SpeakLongClick(list.get(position)));
                        return true;
                    }
                });
                if (dto.isPlaying) {
                    holder.view.setBackgroundResource(R.drawable.d_row_speaking);
                    holder.imgPlaying.setVisibility(View.VISIBLE);
                } else {
                    holder.imgPlaying.setVisibility(View.GONE);
                    holder.view.setBackgroundResource(android.R.color.transparent);
                }

                break;
            case TYPE_TITLE:
                if (dto.text != null) {
                    holder.title.setText(dto.text.replace("_", " "));
                }
                break;
        }

    }

    public SpeakDto getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTitle ? TYPE_TITLE : TYPE_SPEAK;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView txtNo;
        TextView title;
        TextView text;
        ImageView imgPlaying;
        ImageView imgRival;
        View view;

        static int[] colors = new int[]{R.color.md_purple_500, R.color.md_orange_500, R.color.md_blue_500, R.color.md_green_500,
                R.color.md_red_500, R.color.md_cyan_500};

        public Holder(View v) {
            super(v);
            txtNo = (TextView) v.findViewById(R.id.row_voice_no);
            text = (TextView) v.findViewById(R.id.row_voice_text);
            imgPlaying = (ImageView) v.findViewById(R.id.row_voice_playing);
            imgRival = (ImageView) v.findViewById(R.id.row_voice_rival);
            view = v.findViewById(R.id.row_voice_main);
            title = (TextView) v.findViewById(R.id.row_title);
            if (imgPlaying != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    AnimationDrawable animation = (AnimationDrawable)
                            imgPlaying.getContext().getDrawable(R.drawable.ic_playing_drawable);
                    imgPlaying.setImageDrawable(animation);
                    int col = new Random().nextInt(colors.length - 1);
                    ColorStateList sColorStatePlaying = ColorStateList.valueOf(imgPlaying.getContext().getResources().getColor(
                            colors[col]));
                    imgPlaying.setImageTintList(sColorStatePlaying);
                    if (animation != null) animation.start();
                }

            }
        }
    }
}
