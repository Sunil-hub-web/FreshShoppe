package in.freshshoppe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.freshshoppe.R;
import in.freshshoppe.models.VariationGetSet;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VariationAdapterforProductlist extends RecyclerView.Adapter<VariationAdapterforProductlist.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<VariationGetSet> accepetedJobLists;

    public TextView name, price, regprice;
    //    public Button viewprofile;
    public ImageView thumbnail;

    public VariationAdapterforProductlist(List<VariationGetSet> accepetedJobLists, Context context) {
        this.accepetedJobLists = accepetedJobLists;
        this.context = context;

    }

    @Override
    public VariationAdapterforProductlist.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.from(parent.getContext()).inflate(R.layout.sub_cat_item, parent, false);
        VariationAdapterforProductlist.MyViewHolder holder = new VariationAdapterforProductlist.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final VariationAdapterforProductlist.MyViewHolder holder, final int position) {

        VariationGetSet movie = accepetedJobLists.get(position);

        holder.heading.setText(movie.getVarations());


    }


    @Override
    public int getItemCount() {
        return accepetedJobLists.size();
//        return 6;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView heading;

        public MyViewHolder(View itemView) {
            super(itemView);

            heading = (TextView) itemView.findViewById(R.id.heading);

        }
    }

}