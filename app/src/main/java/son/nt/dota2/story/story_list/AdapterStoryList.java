package son.nt.dota2.story.story_list;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.story.story_details.StoryDetailActivity;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hero_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryDetailActivity.start(((Activity) context), mValues.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    public void setData(List<StoryFireBaseDto> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        ImageView group;
        TextView name;
        TextView fullname;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
            this.group = (ImageView) itemView.findViewById(R.id.group);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.fullname = (TextView) itemView.findViewById(R.id.fullname);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final StoryFireBaseDto dto = mValues.get(position);
        viewHolder.name.setText(dto.getTitle());
        viewHolder.fullname.setText(dto.getStoryId());
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }
}
