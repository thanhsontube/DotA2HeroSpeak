package son.nt.dota2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.HeroDto;

/**
 * Created by Sonnt on 7/7/15.
 */
public class AdapterRcvHome extends RecyclerView.Adapter<AdapterRcvHome.ViewHolder> {

    List<HeroDto> mValues;
    Context context;
    private int mBackground;
    private final TypedValue mTypedValue = new TypedValue();
    private  final WeakReference<Context> contextWeakReference;
    public AdapterRcvHome (Context cx, List<HeroDto> list, IAdapter iAdapter) {
        this.listener = iAdapter;
        this.mValues = list;
        this.contextWeakReference = new WeakReference<Context>(cx);
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hero_list, viewGroup, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final HeroDto dto = mValues.get(i);
        String url = "http://38.media.tumblr.com/600b4ea2d2770bec97fd836a6b3c91f9/tumblr_n5u5px6X1Z1rwq84jo1_r1_400.gif";
        viewHolder.txtName.setText(dto.name);
        if (contextWeakReference.get() != null) {
            Glide.with(viewHolder.imageView.getContext()).load(dto.avatarThubmail)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageView);
        }

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onIAdapterItemCLick(dto, i);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtName;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.imageView = (ImageView) view.findViewById(R.id.row_avatar);
            this.txtName = (TextView) view.findViewById(R.id.row_name);

        }
    }
    IAdapter listener;
    public interface IAdapter {
        void onIAdapterItemCLick (HeroDto heroDto, int position);
    }
}
