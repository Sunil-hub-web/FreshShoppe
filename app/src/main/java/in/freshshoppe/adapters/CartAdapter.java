package in.freshshoppe.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.freshshoppe.R;
import in.freshshoppe.activities.Cart;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.models.CartItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
    int indx;
    SharedPreference sharedPreference = new SharedPreference();

    public CartAdapter(Context context, ArrayList<CartItem> favouritesBeanSampleList) {
        this.context = context;
        this.favouritesBeanSampleList = favouritesBeanSampleList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cartitem_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int pos) {

//        viewHolder.iv_product_image.setImageResource(favouritesBeanSampleList.get(pos).getProductimage());
        Glide.with(context)
                .load(favouritesBeanSampleList.get(pos).getProductimage())
                .into(viewHolder.iv_product_image);

        viewHolder.tv_new_price.setText("\u20B9" + " " + favouritesBeanSampleList.get(pos).getSales_price());
        viewHolder.tv_old_price.setVisibility(View.GONE);
        viewHolder.tv_old_price.setPaintFlags(viewHolder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tv_product_weight.setText(favouritesBeanSampleList.get(pos).getVarient_name());
        String pnm = favouritesBeanSampleList.get(pos).getProduct_name();

        if(pnm.contains("&amp;")){
            pnm.replace("&amp;", "&");
        }
        viewHolder.tv_product_name.setText(pnm);



        viewHolder.l_add_cart.setVisibility(View.VISIBLE);
        viewHolder.tv_count.setText(favouritesBeanSampleList.get(pos).getQuantity());

        viewHolder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count_value = Integer.valueOf(viewHolder.tv_count.getText().toString());
//                int count = count_value - 1;
                if (count_value == 1) {

//                    viewHolder.l_add_cart.setVisibility(View.GONE);
                } else {
                    int count = count_value - 1;
                    String vsubmit_id = favouritesBeanSampleList.get(pos).getVarient_id();
                    String vsubmit_price = favouritesBeanSampleList.get(pos).getSales_price();
                    getindex(vsubmit_id);

                    double t = Double.parseDouble(vsubmit_price) * count;

                    sharedPreference.editFavorite(context, pos, String.valueOf(t), String.valueOf(count));
//                    Cart.sum=0.00;
                    getTotal();
                    viewHolder.tv_count.setText(count + "");
                }
            }
        });

        viewHolder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("efvfvdcs_AAA", "Clicked");
                    int count_value = Integer.valueOf(viewHolder.tv_count.getText().toString());
                    int count = count_value + 1;

                    String vsubmit_id = favouritesBeanSampleList.get(pos).getVarient_id();
                    String vsubmit_price = favouritesBeanSampleList.get(pos).getSales_price();

                    double t = Double.parseDouble(vsubmit_price) * count;
                    sharedPreference.editFavorite(context, pos, String.valueOf(t), String.valueOf(count));

//                    ItemCounter();
//                    Cart.sum=0.00;
                    getTotal();
                    viewHolder.tv_count.setText(count + "");
                }catch (Exception e){
                    Log.d("efvfvdcs", String.valueOf(e));
                }

            }
        });

        viewHolder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.removeFavorite(context,pos);
                favouritesBeanSampleList.remove(pos);
                notifyDataSetChanged();
//                Cart.sum=0.00;
                getTotal();
            }
        });

    }

    public void getTotal(){

        favouritesBeanSampleList = sharedPreference.loadFavorites(context);

        Log.d("RanjeetCartPage",String.valueOf(favouritesBeanSampleList.size()));
        Cart.sum = 0.00;
        if(favouritesBeanSampleList.size()==0){
//            Cart.itemcounter.setText("0");
            ((Activity)context).finish();
        }else {

            for (int j = 0; j < favouritesBeanSampleList.size(); j++) {


                CartItem cartIt = favouritesBeanSampleList.get(j);


                Cart.sum = Cart.sum + Double.parseDouble(cartIt.getSales_price()) * Double.parseDouble(cartIt.getQuantity());




            }
            Cart.itemtotal.setText("₹" + Cart.sum);

            Log.d("Frfbw_2", String.valueOf(Cart.sum));
            double totsm = Cart.sum + Cart.delivery_ch - Cart.coupon_amt;

            Cart.paybleamount.setText("₹" + totsm);
            Log.d("Frfbw_3", String.valueOf(totsm));
        }
    }

    public void setFinalPrices() {

        Cart.sum = sharedPreference.getCartAmount(context);
        Log.d("efeawwq", String.valueOf( Cart.sum));

        Cart.itemtotal.setText("₹" +  Cart.sum);

        Log.d("Frfbw_2", String.valueOf( Cart.sum));
        double totsm =  Cart.sum +  Cart.delivery_ch -  Cart.coupon_amt;

        Cart.paybleamount.setText("₹" + totsm);
        Log.d("Frfbw_3", String.valueOf(totsm));
    }


    public boolean getindex(String checkProduct) {
        boolean check = false;
        List<CartItem> favorites = sharedPreference.loadFavorites(context);
        if (favorites != null) {
            for (CartItem product : favorites) {

                if (product.getVarient_id().equals(checkProduct)) {

                    indx = favorites.indexOf(product);

                    Log.d("indx", String.valueOf(indx));

                    check = true;
                    break;

                }

            }
        }
        return check;


    }

    @Override
    public int getItemCount() {
        return favouritesBeanSampleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout l_cart;
        ImageView iv_product_image, iv_remove;
        TextView tv_new_price, tv_old_price, tv_product_name, tv_product_weight, tv_minus, tv_plus, tv_count;
        LinearLayout l_add_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            l_cart = itemView.findViewById(R.id.l_cart);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            iv_remove = itemView.findViewById(R.id.iv_remove);
            tv_new_price = itemView.findViewById(R.id.tv_new_price);
            tv_old_price = itemView.findViewById(R.id.tv_old_price);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_weight = itemView.findViewById(R.id.tv_product_weight);


            l_add_cart = itemView.findViewById(R.id.l_add_cart);
            tv_minus = itemView.findViewById(R.id.tv_minus);
            tv_plus = itemView.findViewById(R.id.tv_plus);
            tv_count = itemView.findViewById(R.id.tv_count);


        }
    }
}