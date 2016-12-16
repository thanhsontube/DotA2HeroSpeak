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

import butterknife.BindView;
import butterknife.ButterKnife;
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
             * send to {@link son.nt.dota2.service.PlayService2#onGetAdapterSwipeFragmentClick(GoVoice)}
             */
            OttoBus.post(new GoVoice(selectedItem, isArcana));
        });

        holder.moreView.setOnClickListener(v -> {


        });
        return holder;


    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final HeroResponsesDto dto = mList.get(position);
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
            holder.relatedView.setVisibility(View.GONE);
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

            holder.relatedView.setVisibility(View.VISIBLE);
            holder.relatedView.setText(related);
        }
        if (dto.isPlaying()) {
            holder.view.setBackgroundResource(R.drawable.d_row_speaking);
        } else {
            holder.view.setBackgroundResource(android.R.color.transparent);
        }


    }

    public HeroResponsesDto getItem(int position) {
        return mList.get(position);
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
        @BindView(R.id.row_voice_no)
        TextView txtNo;
        @BindView(R.id.row_voice_text)
        TextView text;
        @BindView(R.id.row_voice_related)
        TextView relatedView;
        @BindView(R.id.row_voice_group)
        TextView voiceGroup;
        @BindView(R.id.row_voice_rival)
        ImageView imgFrom;
        @BindView(R.id.row_voice_to_icon)
        ImageView imgTo;
        @BindView(R.id.row_voice_main)
        View view;
        @BindView(R.id.row_voice_more)
        View moreView;


        public Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public HeroResponsesDto getSelected() {
        if (mList.isEmpty() || previousSelectedItem < 0) {
            return null;
        }
        return mList.get(previousSelectedItem);
    }
}
