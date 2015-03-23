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
import son.nt.dota2.facebook.UserDto;

/**
 * Created by 4210047 on 3/20/2015.
 */
public class AdapterDrawerRight extends RecyclerView.Adapter<AdapterDrawerRight.Holder> {
    private Context context;
    List<UserDto> list;
    AQuery aq;

    public interface IAdapterCallback {
        void onClick(int position, UserDto UserDto);
    }

    IAdapterCallback listenerAdapter;

    public void setOnCallback(IAdapterCallback callback) {
        this.listenerAdapter = callback;
    }

    public AdapterDrawerRight(Context context, List<UserDto> list) {
        this.context = context;
        this.list = list;
        aq = new AQuery(context);

    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgAvatar;
        TextView txtCmt, txtName, txtTime;
        View view;

        public Holder(View view, int viewType, IHolderListener callback) {
            super(view);
            this.mListener = callback;
            this.view = view;
            this.view.setOnClickListener(this);
            imgAvatar = (ImageView) view.findViewWithTag("icon");
            txtCmt = (TextView) view.findViewWithTag("text");
            txtName = (TextView) view.findViewWithTag("name");
            txtTime = (TextView) view.findViewWithTag("time");
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
        view = inflater.inflate(R.layout.row_right_drawer, viewGroup, false);
        holder = new Holder(view, viewType, listener);
        return holder;
    }

    Holder.IHolderListener listener = new Holder.IHolderListener() {
        @Override
        public void onClick(View v, int position) {
            if (listenerAdapter != null) {
                listenerAdapter.onClick(position, list.get(position));
            }
        }
    };

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        UserDto dto = list.get(position);
        holder.txtCmt.setText (dto.cmt);
        holder.txtName.setText (dto.name);
        holder.txtTime.setText (dto.datePost);
        if (!TextUtils.isEmpty(dto.avatar)) {
            aq.id(holder.imgAvatar).image(dto.avatar, true, true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
