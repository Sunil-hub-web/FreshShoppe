package in.freshshoppe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.freshshoppe.R;
import in.freshshoppe.activities.ProductList;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.models.CategoryGetSet;
import in.freshshoppe.models.SubCatGetSet;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    CategoryGetSet category;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView cat_name;
        public ImageView  icon;
        public CardView mainlay;

        public MyViewHolder(View view) {
            super(view);
            cat_name = (TextView) view.findViewById(R.id.cat_name);
            icon = (ImageView) view.findViewById(R.id.icon);
            mainlay = (CardView) view.findViewById(R.id.mainlay);


        }
    }
    public CategoryAdapter(List<CategoryGetSet> categoryList, Context mContext) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        return new CategoryAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {

        category = categoryList.get(position);
        holder.cat_name.setText(category.getCate_name());
                Glide.with(mContext).load(
//                    APIClient.Base_URL + "/" +
                        category.getCate_img()).into(holder.icon);

        holder.mainlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.itemfilters = new ArrayList<SubCatGetSet>();

                Constants.itemfilters = categoryList.get(position).getSubcats();
                Intent in = new Intent(mContext, ProductList.class);
                in.putExtra("title", categoryList.get(position).getCate_name());
                in.putExtra("id", categoryList.get(position).getCategory_id());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}