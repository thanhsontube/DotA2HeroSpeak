package son.nt.dota2.story.story_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.customview.HeroProfileView;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.ottobus_entry.GoVoice;
import son.nt.dota2.utils.OttoBus;

public class AdapterStoryDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StoryPartDto> list;
    LayoutInflater inflater;
    Context mContext;
    public static final int TYPE_ADD = 0;
    public static final int TYPE_MORE = 1;
    public static final int TYPE_SOUND_LEFT = 2;
    public static final int TYPE_SOUND_RIGHT = 3;
    public static final int TYPE_SOUND_MIDDLE = 4;
    private List<StoryPartDto> mData;

    IStoryDetailListener mListener;

    public interface IStoryDetailListener {
        void onAddLeftClick();

        void onAddRightClick();

        void onAddMiddleClick();
    }

    public AdapterStoryDetail(List<StoryPartDto> list, Context context, IStoryDetailListener listener) {
        this.list = list;
        this.mContext = context;
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




            case TYPE_SOUND_LEFT: {
                View view = inflater.inflate(R.layout.row_story_sound, parent, false);
                SoundHolder holder = new SoundHolder(view);

                holder.soundText.setOnClickListener(v -> {
                    if (mData == null || mData.isEmpty()) {
                        return;
                    }
                    OttoBus.post(new GoVoice(mData.get(holder.getAdapterPosition()), false));
                });

                return holder;
            }
            case TYPE_SOUND_RIGHT: {
                View view = inflater.inflate(R.layout.row_story_sound_right, parent, false);
                SoundHolder holder = new SoundHolder(view);

                holder.soundText.setOnClickListener(v -> {
                    OttoBus.post(new GoVoice(mData.get(holder.getAdapterPosition()), false));
                });

                return holder;
            }
            case TYPE_SOUND_MIDDLE: {
                View view = inflater.inflate(R.layout.row_story_sound_middle, parent, false);
                SoundHolderMiddle holder = new SoundHolderMiddle(view);



                return holder;
            }


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ADD: {
                return;

            }


            case TYPE_MORE: {
                return;

            }
            case TYPE_SOUND_RIGHT:
            case TYPE_SOUND_LEFT: {
                StoryPartDto dto = mData.get(position);
                SoundHolder soundHolder = (SoundHolder) holder;

                soundHolder.avatar.setAvatar(dto.getHeroImage());

                soundHolder.description.setText(TextUtils.isEmpty(dto.getDescription()) ? "" : dto.getDescription());
                soundHolder.soundText.setText(dto.getSoundText());
                return;
            }

            case TYPE_SOUND_MIDDLE: {
                StoryPartDto dto = mData.get(position);
                SoundHolderMiddle soundHolder = (SoundHolderMiddle) holder;



                soundHolder.soundText.setText(dto.getDescription());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public int getItemViewType(int position) {
        final StoryPartDto storyPartDto = mData.get(position);
        if (storyPartDto.getViewType().equals(MsConst.TYPE_ADD)) {
            return TYPE_ADD;
        }



        if (storyPartDto.getViewType().equals(MsConst.TYPE_SOUND_LEFT)) {
            return TYPE_SOUND_LEFT;
        }
        if (storyPartDto.getViewType().equals(MsConst.TYPE_SOUND_RIGHT)) {
            return TYPE_SOUND_RIGHT;
        }
        if (storyPartDto.getViewType().equals(MsConst.TYPE_SOUND_MIDDLE)) {
            return TYPE_SOUND_MIDDLE;
        }

        return TYPE_ADD;
    }

    public void setData(List<StoryPartDto> data) {
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



    static class SoundHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_avatar)
        HeroProfileView avatar;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.soundtext)
        TextView soundText;

        public SoundHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class SoundHolderMiddle extends RecyclerView.ViewHolder {

        @BindView(R.id.soundtext)
        TextView soundText;

        public SoundHolderMiddle(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
