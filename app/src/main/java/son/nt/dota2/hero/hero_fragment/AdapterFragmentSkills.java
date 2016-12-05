package son.nt.dota2.hero.hero_fragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.R;
import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.ottobus_entry.GoVoice;
import son.nt.dota2.utils.OttoBus;
import timber.log.Timber;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AdapterFragmentSkills extends RecyclerView.Adapter<AdapterFragmentSkills.Holder> {

    public static final String TAG = AdapterFragmentSkills.class.getSimpleName();

    private List<AbilitySoundDto> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public AdapterFragmentSkills(Context context, List<AbilitySoundDto> list) {
        this.mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_abi, parent, false);
        Holder holder = new Holder(view);

        holder.abiImage.setOnClickListener(v -> {
            Timber.d(">>>" + "abiImage click:" + holder.getAdapterPosition());
            OttoBus.post(new GoVoice(mList.get(holder.getAdapterPosition() +1), false));
        });
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final AbilitySoundDto dto = mList.get(position);

        Glide.with(mContext).load(dto.abiImage)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.abiImage);

        holder.abiName.setText(dto.getTitle());
        holder.abiDes.setText(dto.abiDescription);
        holder.abiNote.setText(dto.abiNotes);

    }

    public AbilitySoundDto getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<AbilitySoundDto> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.abi_name)
        TextView abiName;
        @BindView(R.id.abi_des)
        TextView abiDes;
        @BindView(R.id.abi_notes)
        TextView abiNote;
        @BindView(R.id.abi_img)
        ImageView abiImage;

        @BindView(R.id.abi_view)
        View view;

        public Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }
}
