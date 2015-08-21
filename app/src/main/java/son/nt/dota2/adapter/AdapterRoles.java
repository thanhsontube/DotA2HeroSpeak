package son.nt.dota2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.ottobus_entry.GoAdapterRoles;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/14/15.
 */
public class AdapterRoles extends RecyclerView.Adapter<AdapterRoles.Holder> {
    private List<RoleDto> list;
    private Context context;

    public AdapterRoles (Context  cx, List<RoleDto> list1) {
        this.list = list1;
        this.context = cx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_roles, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final RoleDto dto = list.get(position);
        holder.txtName.setText(dto.name);
        holder.txtSlogan.setText(dto.slogan);
        Glide.with(holder.img.getContext()).load(dto.linkIcon).diskCacheStrategy(DiskCacheStrategy.ALL)
        .fitCenter().into(holder.img);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OttoBus.post(new GoAdapterRoles(dto.name.trim()));

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtSlogan;
        View view;
        public Holder(View view) {
            super(view);
            this.view = view.findViewById(R.id.row_roles_ll);
            img = (ImageView) view.findViewById(R.id.row_roles_icon);
            txtName = (TextView) view.findViewById(R.id.row_roles_name);
            txtSlogan = (TextView) view.findViewById(R.id.row_roles_slogan);
        }
    }
}
