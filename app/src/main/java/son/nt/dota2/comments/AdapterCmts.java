package son.nt.dota2.comments;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.ottobus_entry.GoAdapterCmt;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsDate;

/**
 * Created by Sonnt on 7/9/15.
 */
public class AdapterCmts extends RecyclerView.Adapter<AdapterCmts.ViewHolder> {

    Context context;
    List<CommentDto> mValues;

    public AdapterCmts(Context context, List<CommentDto> mValues) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);
        return new ViewHolder(view);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView fromName;
        TextView message;
        TextView createTime;

        ImageView heroAvatar;
        TextView heroText;

        View viewVoice;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.avatar = (ImageView) itemView.findViewById(R.id.row_chat_avatar);
            this.fromName = (TextView) itemView.findViewById(R.id.row_chat_fromName);
            this.message = (TextView) itemView.findViewById(R.id.row_chat_message);
            this.createTime = (TextView) itemView.findViewById(R.id.row_chat_create_day);
            this.heroAvatar = (ImageView) itemView.findViewById(R.id.row_chat_hero);
            this.heroText = (TextView) itemView.findViewById(R.id.row_chat_hero_text);
            this.viewVoice = itemView.findViewById(R.id.row_chat_play);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final CommentDto dto = mValues.get(position);
        viewHolder.fromName.setText(dto.getFromName());
        viewHolder.message.setText(dto.getMessage());
            Glide.with(viewHolder.avatar.getContext()).load(dto.getImage())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fallback(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(viewHolder.avatar);
        Glide.with(viewHolder.heroAvatar.getContext()).load(HeroManager.getInstance().getHero(dto.getSpeakDto().heroId).avatarThumbnail)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.heroAvatar);


        viewHolder.createTime.setText(TsDate.getTimeAgo(dto.getCreateTime(), context));
//        viewHolder.createTime.setText(dto.getCreateAt());
        viewHolder.heroText.setText(dto.getSpeakDto().text);

        viewHolder.viewVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OttoBus.post(new GoAdapterCmt(dto.getSpeakDto().link, dto.getSpeakDto().heroId));
            }
        });


        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
