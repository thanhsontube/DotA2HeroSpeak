package son.nt.dota2.hero;

import com.makeramen.roundedimageview.RoundedImageView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.R;
import son.nt.dota2.dto.CircleFeatureDto;
import son.nt.dota2.ottobus_entry.GoCircle;
import son.nt.dota2.utils.OttoBus;

/**
 * This adapter for the recycler view on the top of {@link son.nt.dota2.fragment.SwipeHeroFragment}
 */
public class AdapterCircleFeature extends RecyclerView.Adapter<AdapterCircleFeature.Holder> {

    public static final String TAG = AdapterCircleFeature.class.getSimpleName();

    private List<CircleFeatureDto> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_FEATURE = 1;

    private int previousSelectedItem = 0;

    public AdapterCircleFeature(Context context, List<CircleFeatureDto> list) {
        this.mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_TITLE:
                view = mInflater.inflate(R.layout.row_speak_title, parent, false);
                break;
            case TYPE_FEATURE:
                view = mInflater.inflate(R.layout.row_circle_feature, parent, false);
                Holder holder = new Holder(view);
                holder.view.setOnClickListener(v -> {
                    int position = holder.getAdapterPosition();

                    mList.get(previousSelectedItem).setSelected(false);
                    notifyItemChanged(previousSelectedItem);

                    previousSelectedItem = position;
                    final CircleFeatureDto selectedItem = mList.get(position);
                    selectedItem.setSelected(true);
                    OttoBus.post(new GoCircle(selectedItem, position));
                    notifyItemChanged(position);
                });
                return holder;


        }
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final CircleFeatureDto dto = mList.get(position);
        switch (getItemViewType(position)) {
            case TYPE_FEATURE: {
                holder.icon.setImageResource(dto.getIcon());
                holder.text.setText(dto.getName());
                holder.icon.setBorderColor(dto.isSelected() ? Color.GREEN : Color.GRAY);
                break;
            }

            case TYPE_TITLE: {
                break;

            }

        }

    }

    public CircleFeatureDto getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_FEATURE;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<CircleFeatureDto> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_feature_text)
        TextView text;

        @BindView(R.id.row_feature_icon)
        RoundedImageView icon;

        @BindView(R.id.row_feature)
        View view;

        public Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
