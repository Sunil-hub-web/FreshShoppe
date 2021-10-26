package in.freshshoppe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.freshshoppe.R;
import in.freshshoppe.models.OrderGetSet;
import in.freshshoppe.models.OrderItemGetSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderGetSet> categoryList;
    //    private RecyclerTouchListener listener;
    OrderGetSet category;
    ArrayList<OrderItemGetSet> orderitemarray;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderid, date, status, amount, deliverydate;
        public RecyclerView orderitems_rv;
        public RelativeLayout deliverylay;

        public MyViewHolder(View view) {
            super(view);
            orderid = (TextView) view.findViewById(R.id.orderid);
            date = (TextView) view.findViewById(R.id.orderdate);
            status =   view.findViewById(R.id.status);
            amount =   view.findViewById(R.id.totalamt);
            orderitems_rv =   view.findViewById(R.id.orderitems_rv);
            deliverydate =   view.findViewById(R.id.deliverydate);
            deliverylay =   view.findViewById(R.id.deliverylay);


        }
    }
    public OrderHistoryAdapter(Context mContext, List<OrderGetSet> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
//        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderhistory_item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        category = categoryList.get(position);

        holder. orderid.setText(category.getOrder_id());
//        holder. date.setText(category.getDate());
        if(category.getDatetime().contains("null")){
            holder. deliverylay.setVisibility(View.GONE);
        }else{
            holder. deliverylay.setVisibility(View.VISIBLE);
        }
        holder. deliverydate.setText("Delivery Date : "+category.getDatetime());
        holder. status.setText("Status : "+category.getOrder_status());
        holder. amount.setText("₹ "+category.getTotal());

        String inputPattern = "dd.MM.yyyy";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        holder. date.setVisibility(View.GONE);
//        try {
//            date = inputFormat.parse(category.get);
//            str = outputFormat.format(date);
//            holder. date.setText( str);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        orderitemarray = new ArrayList<OrderItemGetSet>();

        orderitemarray = category.getItemsarray();

        OrderItemAdapter adpater = new OrderItemAdapter(mContext, orderitemarray);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        holder.orderitems_rv.setLayoutManager(layoutManager);
        holder.orderitems_rv.setItemAnimator(new DefaultItemAnimator());
        holder.orderitems_rv.setAdapter(adpater);
        holder.orderitems_rv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}