package in.freshshoppe.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.freshshoppe.R;
import in.freshshoppe.activities.DashBoard;
import in.freshshoppe.activities.ProductDetails;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.models.CartItem;
import in.freshshoppe.models.ComboGetSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ProgramViewHolder> {
    int count = 0;
    private ArrayList<ComboGetSet> fooditem;
    Context context;
    String qnttty;
    int indx, noit;
    double sum = 0.00;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<CartItem> favouritesBeanSampleList;
    String ct, qnty, productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price;

    public ComboAdapter(ArrayList<ComboGetSet> fooditem, Context context) {
        this.fooditem = fooditem;
        this.context = context;

    }

    @NonNull
    @Override
    public ComboAdapter.ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.top_restaurant_layout, viewGroup, false);

        return new ComboAdapter.ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ComboAdapter.ProgramViewHolder programViewHolder, final int i) {
        final ComboGetSet My_list = fooditem.get(i);

        try {


            programViewHolder.restrurant_food_name.setText(My_list.getName());
            programViewHolder.restt_price.setText("₹ " +My_list.getSales_price());
//            programViewHolder.discount.setText(My_list.getCategory());

            Glide.with(context).load(
                    My_list.getImage()).into(programViewHolder.imag_food);

            programViewHolder.addtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(fooditem.get(i).getVariations().isEmpty()){
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = "";
                        vsubmit_price = fooditem.get(i).getSales_price();
                        productimage = fooditem.get(i).getImage();
                    }else {
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = fooditem.get(i).getVar_name();
                        vsubmit_price = fooditem.get(i).getVar_price();
                        productimage = fooditem.get(i).getImage();
                    }

                    favouritesBeanSampleList = sharedPreference.loadFavorites(context);

                    if (favouritesBeanSampleList == null) {
                        Log.d("gbgvdw_9", vsubmit_id);
                        CartItem cartitem = new CartItem(productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price, "1", vsubmit_price);
                        sharedPreference.addFavorite(context, cartitem);

                        Toast.makeText(context, "Item Added ", Toast.LENGTH_SHORT).show();

                        programViewHolder.l_add.setVisibility(View.GONE);
                        programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                        programViewHolder.tv_count.setText("1");
                        ItemCounter();
                    } else if (favouritesBeanSampleList.size() == 0) {

                        CartItem cartitem = new CartItem(productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price,"1", vsubmit_price);
                        sharedPreference.addFavorite(context, cartitem);

                        Toast.makeText(context, "Item Added ", Toast.LENGTH_SHORT).show();

                        programViewHolder.l_add.setVisibility(View.GONE);
                        programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                        programViewHolder.tv_count.setText("1");
                        ItemCounter();
                    } else {

                        CartItem cartitem = new CartItem(productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price, "1", vsubmit_price);
                        sharedPreference.addFavorite(context, cartitem);

                        Toast.makeText(context, "Item Added ", Toast.LENGTH_SHORT).show();

                        programViewHolder.l_add.setVisibility(View.GONE);
                        programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                        programViewHolder.tv_count.setText("1");
                        ItemCounter();
                    }


                }
            });
            programViewHolder.restt_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent in = new Intent(context, ProductDetails.class);
//                    in.putExtra("name", fooditem.get(i).getName());
//                    in.putExtra("price", fooditem.get(i).getSales_price());
//                    in.putExtra("image", fooditem.get(i).getImage());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
                }
            });
            programViewHolder.imag_food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent in = new Intent(context, ProductDetails.class);
//                    in.putExtra("name", fooditem.get(i).getName());
//                    in.putExtra("price", fooditem.get(i).getSales_price());
//                    in.putExtra("image", fooditem.get(i).getImage());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
                }
            });

            programViewHolder.restrurant_food_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent in = new Intent(context, ProductDetails.class);
//                    in.putExtra("name", fooditem.get(i).getName());
//                    in.putExtra("price", fooditem.get(i).getSales_price());
//                    in.putExtra("image", fooditem.get(i).getImage());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
                }
            });

            programViewHolder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(fooditem.get(i).getVariations().isEmpty()){
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = "";
                        vsubmit_price = fooditem.get(i).getSales_price();
                        productimage = fooditem.get(i).getImage();
                    }else {
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = fooditem.get(i).getVar_name();
                        vsubmit_price = fooditem.get(i).getVar_price();
                        productimage = fooditem.get(i).getImage();
                    }

                    int count_value = Integer.valueOf(programViewHolder.tv_count.getText().toString());
                    int count = count_value - 1;
                    if (count == 0) {
                        programViewHolder.l_add.setVisibility(View.VISIBLE);
                        programViewHolder.l_add_cart.setVisibility(View.GONE);
                        getindex(vsubmit_id);
                        sharedPreference.removeFavorite(context, indx);
                        ItemCounter();
                    } else {

                        getindex(vsubmit_id);

                        double t = Double.parseDouble(vsubmit_price) * count;

                        sharedPreference.editFavorite(context, indx, String.valueOf(t), String.valueOf(count));

                        ItemCounter();
                        programViewHolder.tv_count.setText(count + "");
                    }

                }
            });


            programViewHolder.tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count_value = Integer.valueOf(programViewHolder.tv_count.getText().toString());
                    int count = count_value + 1;
//                programViewHolder.tv_count.setText(count + "");
                    if(fooditem.get(i).getVariations().isEmpty()){
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = "";
                        vsubmit_price = fooditem.get(i).getSales_price();
                        productimage = fooditem.get(i).getImage();
                    }else {
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = fooditem.get(i).getVar_name();
                        vsubmit_price = fooditem.get(i).getVar_price();
                        productimage = fooditem.get(i).getImage();
                    }

                    Log.d("gvrtgv", vsubmit_id);

                    getindex(vsubmit_id);

                    int plusValue = count_value + 1;

                    double t = Double.parseDouble(vsubmit_price) * plusValue;

                    sharedPreference.editFavorite(context, indx, String.valueOf(t), String.valueOf(plusValue));

                    ItemCounter();
                    programViewHolder.tv_count.setText(plusValue + "");

                }
            });

            if (checkIfSameItem(My_list.getProducts_id(), "")) {
                programViewHolder.l_add.setVisibility(View.GONE);
                programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                programViewHolder.tv_count.setText(qnty);
            } else {
                programViewHolder.l_add.setVisibility(View.VISIBLE);
                programViewHolder.l_add_cart.setVisibility(View.GONE);
                programViewHolder.tv_count.setText(qnty);
            }

        } catch (Exception e) {
            Log.d("wedfd", String.valueOf(e));
        }
    }

    @Override
    public int getItemCount() {

        return fooditem.size();
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView restrurant_food_name, restt_price, tv_minus, tv_plus, tv_count, addtext, discount;
        LinearLayout l_add, l_add_cart, lay;
        Button add;
        ImageView imag_food;
        RelativeLayout item_qty_relative, instock;
        LinearLayout foodItemMain;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            restrurant_food_name = (TextView) itemView.findViewById(R.id.restrurant_food_name);
            restt_price = (TextView) itemView.findViewById(R.id.restt_price);
            tv_minus = (TextView) itemView.findViewById(R.id.tv_minus);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
            addtext = (TextView) itemView.findViewById(R.id.addtext);
            discount = (TextView) itemView.findViewById(R.id.discount);
            imag_food = (ImageView) itemView.findViewById(R.id.imag_food);
            l_add = (LinearLayout) itemView.findViewById(R.id.l_add);
            l_add_cart = (LinearLayout) itemView.findViewById(R.id.l_add_cart);


        }
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

    public boolean checkIfSameItem(String productid, String variationid) {
        boolean check = false;
        List<CartItem> favorites = sharedPreference.loadFavorites(context);
        if (favorites != null) {
            for (CartItem product : favorites) {

                if(variationid.length()==0){
                    if (product.getProduct_id().equals(productid)) {

                        qnty = product.getQuantity();

                        Log.d("oapsd", qnty);

                        check = true;
                        break;

                    }
                }else {

                    if (product.getVarient_id().equals(variationid)) {

                        qnty = product.getQuantity();

                        Log.d("oapsd", qnty);

                        check = true;
                        break;

                    }
                }

            }
        }
        return check;

    }

    public void ItemCounter() {

        try {
            favouritesBeanSampleList = sharedPreference.loadFavorites(context);
//        Log.d("fbfds", String.valueOf(favouritesBeanSampleList.size()));
//        ct=String.valueOf(favouritesBeanSampleList.size());


            ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;


            DashBoard.itemcounter.setText(Constants.cartitems);


        } catch (Exception e) {


        }

    }

}