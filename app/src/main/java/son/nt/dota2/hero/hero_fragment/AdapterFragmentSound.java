package son.nt.dota2.hero.hero_fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.ottobus_entry.GoVoice;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AdapterFragmentSound extends RecyclerView.Adapter<AdapterFragmentSound.Holder> {

    public static final String TAG = AdapterFragmentSound.class.getSimpleName();

    private List<HeroResponsesDto> mList;
    private boolean isArcana;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_SPEAK = 1;

    private int previousSelectedItem = -1;

    public AdapterFragmentSound(Context context, List<HeroResponsesDto> list) {
        this.mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;

    }

    public void setArcana(boolean arcana) {
        isArcana = arcana;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_TITLE:
                view = mInflater.inflate(R.layout.row_speak_title, parent, false);
                break;
            case TYPE_SPEAK:
                view = mInflater.inflate(R.layout.row_voice_2, parent, false);
                Holder holder = new Holder(view);
                holder.view.setOnClickListener(v -> {
                    TsGaTools.trackPages("/row_voice_1_click");

                    int position = holder.getAdapterPosition();

                    if (previousSelectedItem != -1) {
                        mList.get(previousSelectedItem).setPlaying(false);
                        notifyItemChanged(previousSelectedItem);
                    }
                    previousSelectedItem = position;
                    final HeroResponsesDto selectedItem = mList.get(position);
                    selectedItem.setPlaying(true);
                    notifyItemChanged(position);

                    /**
                     * send to {@link son.nt.dota2.service.PlayService2#onGetAdapterSwipeFragmentClick(HeroResponsesDto)}
                     */
                    OttoBus.post(new GoVoice(selectedItem, isArcana));
                });
                return holder;


        }
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final HeroResponsesDto dto = mList.get(position);
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
                    holder.text.setText(dto.getText());
                }

                holder.voiceGroup.setText(TextUtils.isEmpty(dto.getVoiceGroup()) ? "Unknown" : dto.getVoiceGroup());

                if (dto.isPlaying()) {
                    holder.view.setBackgroundResource(R.drawable.d_row_speaking);
                } else {
                    holder.view.setBackgroundResource(android.R.color.transparent);
                }

                final boolean isSwap = dto.getVoiceGroup().endsWith(MsConst.LORD_KILLING) || dto.getVoiceGroup().endsWith(MsConst.LORD_MEETING);
                final String fromIcon = isSwap
                        ? dto.getToHeroIcon() : dto.getHeroIcon();

                final String toIcon = isSwap
                        ? dto.getHeroIcon() : dto.getToHeroIcon();
                Glide.with(mContext).load(fromIcon)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgFrom);
                String related = " -> ";

                if (TextUtils.isEmpty(dto.toHeroId)) {
                    holder.imgTo.setVisibility(View.GONE);
                    holder.related.setVisibility(View.GONE);
                } else {
                    holder.imgTo.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(toIcon)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgTo);

                    if (dto.isAlliMeetingGroup() || dto.getVoiceGroup().equals(MsConst.LORD_MEETING)) {

                        related = " " + mContext.getString(R.string.meets) + " ";
                    }

                    if (dto.isEnemiesKillingGroup() || dto.getVoiceGroup().equals(MsConst.LORD_KILLING)) {
                        related = " " + mContext.getString(R.string.killed) + " ";
                    }

                    if (dto.getVoiceGroup().contains(MsConst.BUYS_ITEMS)) {
                        related = " " + mContext.getString(R.string.buy) + " ";
                    }

                    holder.related.setVisibility(View.VISIBLE);
                    holder.related.setText(related);
                }


//                holder.view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TsGaTools.trackPages("/row_voice_1_click");
//                        mList.get(position).position = position;
//                        OttoBus.post(mList.get(position));
//                    }
//                });
//
//                holder.view.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        TsGaTools.trackPages("/row_voice_long_click");
////                        OttoBus.post(new SpeakLongClick(mList.get(position)));
//                        return true;
//                    }
//                });
                if (dto.isPlaying()) {
                    holder.view.setBackgroundResource(R.drawable.d_row_speaking);
                } else {
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

    public HeroResponsesDto getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
//        return mList.get(position).isTitle ? TYPE_ADD : TYPE_MORE;
//        return position == 0 ? TYPE_ADD : TYPE_MORE;
        return TYPE_SPEAK;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<HeroResponsesDto> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView txtNo;
        TextView title;
        TextView text;
        TextView related;
        TextView voiceGroup;
        ImageView imgFrom;
        ImageView imgTo;
        View view;

        static int[] colors = new int[]{R.color.md_purple_500, R.color.md_orange_500, R.color.md_blue_500, R.color.md_green_500,
                R.color.md_red_500, R.color.md_cyan_500};

        public Holder(View v) {
            super(v);
            txtNo = (TextView) v.findViewById(R.id.row_voice_no);
            related = (TextView) v.findViewById(R.id.row_voice_related);
            voiceGroup = (TextView) v.findViewById(R.id.row_voice_group);
            text = (TextView) v.findViewById(R.id.row_voice_text);
            imgTo = (ImageView) v.findViewById(R.id.row_voice_to_icon);
            imgFrom = (ImageView) v.findViewById(R.id.row_voice_rival);
            view = v.findViewById(R.id.row_voice_main);
            title = (TextView) v.findViewById(R.id.row_title);
        }
    }
}
