package in.freshshoppe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.freshshoppe.R;
import in.freshshoppe.models.OrderItemGetSet;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderItemGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    OrderItemGetSet category;
    ArrayList<OrderItemGetSet> orderitemarray;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_product_name, tv_new_price, tv_product_weight;
        public ImageView iv_product_image;

        public MyViewHolder(View view) {
            super(view);
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            tv_new_price = (TextView) view.findViewById(R.id.tv_new_price);
            tv_product_weight =   view.findViewById(R.id.tv_product_weight);
            iv_product_image =   view.findViewById(R.id.iv_product_image);
        }
    }
    public OrderItemAdapter(Context mContext, List<OrderItemGetSet> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }
    @Override
    public OrderItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderhistory_items, parent, false);
        return new OrderItemAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final OrderItemAdapter.MyViewHolder holder, int position) {
        category = categoryList.get(position);

        holder. tv_product_name.setText(category.getName()+"("+category.getWeight()+")");
//        holder. date.setText(category.getDate());
        holder. tv_product_weight.setText("Qty : "+category.getQty());
        holder. tv_new_price.setText("₹ "+category.getPrice());

//        Glide.with(mContext)
//                .load(category.getImg())
//                .into(holder.iv_product_image);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}