package son.nt.dota2.story.story_list;

import com.bumptech.glide.Glide;

import android.app.Activity;
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
import son.nt.dota2.R;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.story.story_details.StoryDetailActivity;
import son.nt.dota2.utils.DatetimeUtils;

/**
 * Created by Sonnt on 7/9/15.
 */
public class AdapterStoryList extends RecyclerView.Adapter<AdapterStoryList.ViewHolder> {

    Context context;
    List<StoryFireBaseDto> mValues;

    IAdapterStoryListCallback mCallback;

    public interface IAdapterStoryListCallback {
        void onAdapterHeroClick(StoryFireBaseDto StoryFireBaseDto);
    }

    public AdapterStoryList(Context context, List<StoryFireBaseDto> mValues, IAdapterStoryListCallback callback) {
        this.mValues = mValues;
        this.context = context;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_story_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(v -> StoryDetailActivity.start(((Activity) context), mValues.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    public void setData(List<StoryFireBaseDto> data) {
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
        final StoryFireBaseDto dto = mValues.get(position);

        viewHolder.name.setText(dto.getUsername() == null ? "No name" : dto.getUsername());
        viewHolder.storyName.setText(dto.getTitle());
        viewHolder.createdDate.setText(DatetimeUtils.getTimeAgo(dto.getCreatedTime(), context));
        Glide.with(context)
                .load(dto.getUserPicture())
                .into(viewHolder.avatar);

    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }
}
