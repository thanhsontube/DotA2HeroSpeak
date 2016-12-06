package son.nt.dota2.story;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.R;
import son.nt.dota2.dto.story.StoryDto;

public class AdapterCreateStory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StoryDto> list;
    LayoutInflater inflater;
    Context context;
    public static final int TYPE_ADD = 0;
    public static final int TYPE_MORE = 1;
    private List<StoryDto> mData;

    ICreateStoryListener mListener;

    public interface ICreateStoryListener {
        void onAddLeftClick();

        void onAddRightClick();

        void onAddMiddleClick();
    }

    public AdapterCreateStory(List<StoryDto> list, Context context, ICreateStoryListener listener) {
        this.list = list;
        this.context = context;
        this.mListener = listener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADD: {
                View view = inflater.inflate(R.layout.row_story_add, parent, false);
                AddHolder holder = new AddHolder(view);
                holder.left.setOnClickListener(v -> {
                    mListener.onAddLeftClick();

                });
                holder.right.setOnClickListener(v -> {
                    mListener.onAddRightClick();

                });
                holder.viewMiddle.setOnClickListener(v -> {
                    mListener.onAddMiddleClick();

                });
                return holder;
            }


            case TYPE_MORE: {
                View view = inflater.inflate(R.layout.row_story_add, parent, false);
                AddHolder holder = new AddHolder(view);
                return holder;
            }


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_ADD;
    }

    public void setData(List<StoryDto> data) {
        mData = data;
        notifyDataSetChanged();
    }


    static class AddHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.left_add)
        View left;
        @BindView(R.id.right_add)
        View right;
        @BindView(R.id.middle_add)
        View viewMiddle;

        public AddHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
