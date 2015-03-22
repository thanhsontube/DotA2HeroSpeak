package son.nt.dota2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.dto.LeftDrawerDto;

/**
 * Created by 4210047 on 3/20/2015.
 */
public class AdapterDrawerLeft extends RecyclerView.Adapter<AdapterDrawerLeft.Holder> {
    private Context context;
    List<LeftDrawerDto> list;
    AQuery aq;
    public interface IAdapterCallback {
        void onClick(int position, LeftDrawerDto leftDrawerDto);
    }

    IAdapterCallback listenerAdapter;

    public void setOnCallback(IAdapterCallback callback) {
        this.listenerAdapter = callback;
    }

    public AdapterDrawerLeft(Context context, List<LeftDrawerDto> list) {
        this.context = context;
        this.list = list;
        aq = new AQuery(context);

    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtText;
        ImageView imgIcon;
        View view;

        TextView txtHeroName;
        ImageView imgCircle;
        TextView txtLikes;
        int holderId;

        public Holder(View view, int viewType, IHolderListener callback) {
            super(view);
            this.mListener = callback;
            this.view = view;
            this.view.setOnClickListener(this);
            holderId = viewType;
            if (viewType == 0) {
                txtText = (TextView) view.findViewById(R.id.left_drawer_text);
                imgIcon = (ImageView) view.findViewById(R.id.left_drawer_icon);

            } else {
                txtHeroName = (TextView) view.findViewById(R.id.header_name);
                imgCircle = (ImageView) view.findViewById(R.id.head_image_circle);
            }

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v, getPosition());
            }
        }

        IHolderListener mListener;

        public static interface IHolderListener {
            void onClick(View v, int position);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Holder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.row_drawer_main, viewGroup, false);
            holder = new Holder(view, viewType, listener);
        } else {
            view = inflater.inflate(R.layout.row_header, viewGroup, false);
            holder = new Holder(view, viewType, listener);
        }
        return holder;
    }

    Holder.IHolderListener listener = new Holder.IHolderListener() {
        @Override
        public void onClick(View v, int position) {
            Log.v("", "log>>>" + "Click at:" + position);
            for (LeftDrawerDto dto : list) {
                dto.isSelected = false;
            }
            if(position == 0) {
                list.get(1).isSelected = true;
            } else {

                list.get(position).isSelected = true;
            }
            notifyDataSetChanged();

            if (listenerAdapter != null) {
                listenerAdapter.onClick(position, list.get(position));
            }
        }
    };

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        LeftDrawerDto dto = list.get(position);
        if (holder.holderId == 0) {
            holder.txtText.setText(dto.text);
            if (dto.isSelected) {
                holder.view.setBackgroundResource(R.drawable.layer_drawer);
            } else {
                holder.view.setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            holder.txtHeroName.setText(dto.heroName);
            ImageOptions options = new ImageOptions();
            options.round = 15;

            aq.id(holder.imgCircle).image(dto.heroUrl, options);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return list.get(position).isHeader ? 1 : 0;
        return position == 0 ? 1 : 0;
    }




}
