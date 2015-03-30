package son.nt.dota2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.YoutubeVideoDto;

/**
 * Created by 4210047 on 3/30/2015.
 */
public class AdapterVideoList extends RecyclerView.Adapter<AdapterVideoList.Holder> {
    private List <YoutubeVideoDto> list;
    private Context context;
    AQuery aq;

    public AdapterVideoList(Context context, List<YoutubeVideoDto> list) {
        this.list = list;
        this.context = context;
        aq = new AQuery(context);
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_videos_list, parent, false);
        return new Holder (view, new Holder.IHolderListener() {
            @Override
            public void onClick(View v, int position) {
                listenerAdapter.onClick(position, list.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        YoutubeVideoDto dto = list.get(position);
        if (!TextUtils.isEmpty(dto.title)) {
            holder.txtName.setText(dto.title);
        }

//        String duration = (new SimpleDateFormat("HH:mm:ss")).format(new Date(dto.duration * 1000));

        int totalSecs = dto.duration;
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String duration  = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        holder.txtDuration.setText(duration);
        aq.id(holder.img).image(dto.thumbnail, true, true);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName;
        TextView txtDuration;

        public Holder(View view,IHolderListener callback ) {
            super(view);
            this.mListener = callback;
            img = (ImageView) view.findViewWithTag("image");
            txtName = (TextView) view.findViewWithTag("text");
            txtDuration = (TextView) view.findViewWithTag("duration");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, getPosition());
                }
            });
        }

        IHolderListener mListener;

        public static interface IHolderListener {
            void onClick(View v, int position);
        }
    }

    public interface IAdapterCallback {
        void onClick(int position, YoutubeVideoDto YoutubeVideoDto);
    }

    IAdapterCallback listenerAdapter;

    public void setOnCallback(IAdapterCallback callback) {
        this.listenerAdapter = callback;
    }
}
