package son.nt.dota2.comments;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import son.nt.dota2.utils.DatetimeUtils;

/**
 * Created by Sonnt on 7/9/15.
 */
public class AdapterCmtsHistory extends RecyclerView.Adapter<AdapterCmtsHistory.ViewHolder> {

    Context context;
    List<FullCmtsDto> mValues;

    AdapterCmtsHistory mCallback;

    public interface IAdapterStoryListCallback {
        void onAdapterHeroClick(FullCmtsDto FullCmtsDto);
    }

    public AdapterCmtsHistory(Context context, List<FullCmtsDto> mValues, AdapterCmtsHistory callback) {
        this.mValues = mValues;
        this.context = context;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_story_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setData(List<FullCmtsDto> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_profile)
        ImageView avatar;


        @BindView(R.id.user_name)
        TextView name;

        @BindView(R.id.story_name)
        TextView storyName;

        @BindView(R.id.create_date)
        TextView createdDate;

        @BindView(R.id.add_a_comment)
        TextView addCmtView;

        @BindView(R.id.card_view)
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final FullCmtsDto dto = mValues.get(position);

        final CmtsDto cmtsDto = dto.getCmtsDto();
        viewHolder.name.setText(cmtsDto.getFromName());
        viewHolder.storyName.setText(cmtsDto.getMessage());
        viewHolder.createdDate.setText(DatetimeUtils.getTimeAgo(cmtsDto.getCreateTime(), context));
        Glide.with(context)
                .load(cmtsDto.getFromImage())
                .into(viewHolder.avatar);

        if (cmtsDto.getType().equals(MsConst.COMMENT_TYPE_SIMPLE_STORY)) {
            viewHolder.addCmtView.setText(" added a new comment on a story");
        } else if (cmtsDto.getType().equals(MsConst.COMMENT_TYPE_SIMPLE_SOUND)) {
            viewHolder.addCmtView.setText(" added a new comment on a sound");
        }

    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }
}
