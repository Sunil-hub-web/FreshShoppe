package in.freshshoppe.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import in.freshshoppe.R;
import in.freshshoppe.activities.ProductList;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.extras.RecyclerTouchListener;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.models.CartItem;
import in.freshshoppe.models.ItemGetSet;
import in.freshshoppe.models.VariationGetSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ProgramViewHolder> implements Filterable {
    int count = 0;
    private ArrayList<ItemGetSet> fooditem;
    Context context;
    int indx, noit;
    double sum = 0.00;
    Dialog dialogMenu;
    ArrayList<VariationGetSet> variations;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<CartItem> favouritesBeanSampleList;
    String ct, qnty, productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price;

    private List<ItemGetSet> filteredlist;

    public ItemAdapter(ArrayList<ItemGetSet> fooditem, Context context) {
        this.fooditem = fooditem;
        this.filteredlist = fooditem;
        this.context = context;

    }

    @NonNull
    @Override
    public ItemAdapter.ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.restaurant_item_layout, viewGroup, false);

        return new ItemAdapter.ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.ProgramViewHolder programViewHolder, final int i) {
        final ItemGetSet My_list = filteredlist.get(i);

        try {

            String nm = My_list.getName();
            if(nm.contains("")){
                nm = nm.replace("&amp;", "&");
            }

            programViewHolder.restrurant_food_name.setText(nm);

            programViewHolder.spinertext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    variations = new ArrayList<VariationGetSet>();
                    variations = filteredlist.get(i).getVariations();

                    dialogMenu = new Dialog(context, android.R.style.Theme_Light_NoTitleBar);
                    dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogMenu.setContentView(R.layout.variationrecycler_layout);
                    dialogMenu.setCancelable(true);
                    dialogMenu.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialogMenu.setCanceledOnTouchOutside(true);

                    RecyclerView rv_vars = dialogMenu.findViewById(R.id.rv_vars);

                    rv_vars.setLayoutManager(new LinearLayoutManager(context));
                    rv_vars.setNestedScrollingEnabled(false);
                    VariationAdapterforProductlist varad = new VariationAdapterforProductlist(variations, context);
                    rv_vars.setAdapter(varad);

                    rv_vars.addOnItemTouchListener(new RecyclerTouchListener(context, rv_vars, new RecyclerTouchListener.ClickListener() {

                        @Override
                        public void onClick(View view, int post) {

                            Log.d("gbrdsfbfbvdz", "clicked");

                            VariationGetSet parenting = variations.get(post);

                            filteredlist.get(i).setVar_id(parenting.getPrice_id());
                            filteredlist.get(i).setVar_price(parenting.getPrice());
                            filteredlist.get(i).setVar_name(parenting.getVarations());


                            programViewHolder.restt_price.setText("₹ " + parenting.getPrice());
//                            programViewHolder.discount.setText(parenting.getWeighname());
                            programViewHolder.spinertext.setText(parenting.getVarations());

                            if (checkIfSameItem(parenting.getPrice_id())) {
                                programViewHolder.l_add.setVisibility(View.GONE);
                                programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                                programViewHolder.tv_count.setText(qnty);
                            } else {
                                programViewHolder.l_add.setVisibility(View.VISIBLE);
                                programViewHolder.l_add_cart.setVisibility(View.GONE);
                                programViewHolder.tv_count.setText(qnty);
                            }
                            dialogMenu.dismiss();
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }


                    }));

                    dialogMenu.show();

                }
            });

            Glide.with(context).load(
                    My_list.getImage()).into(programViewHolder.imag_food);

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

            programViewHolder.discount.setOnClickListener(new View.OnClickListener() {
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

            programViewHolder.addtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productid = filteredlist.get(i).getProducts_id();
                    product_name = filteredlist.get(i).getName();
                    vsubmit_id = filteredlist.get(i).getVar_id();
                    attributevalue = filteredlist.get(i).getVar_name();
                    if(filteredlist.get(i).getVar_price().length()==0){
                        vsubmit_price = filteredlist.get(i).getSales_price();
                    }else{
                        vsubmit_price = filteredlist.get(i).getVar_price();
                    }

                    productimage = filteredlist.get(i).getImage();

                    favouritesBeanSampleList = sharedPreference.loadFavorites(context);
                    Log.d("gbgvdw_9", ""+vsubmit_price);
                    if (favouritesBeanSampleList == null) {
//                            Log.d("gbgvdw_9", vsubmit_price);
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


            programViewHolder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productid = filteredlist.get(i).getProducts_id();
                    product_name = filteredlist.get(i).getName();
                    vsubmit_id = filteredlist.get(i).getVar_id();
                    attributevalue = filteredlist.get(i).getVar_name();
                    if(filteredlist.get(i).getVar_price().length()==0){
                        vsubmit_price = filteredlist.get(i).getSales_price();
                    }else{
                        vsubmit_price = filteredlist.get(i).getVar_price();
                    }
                    productimage = filteredlist.get(i).getImage();

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
                    productid = filteredlist.get(i).getProducts_id();
                    product_name = filteredlist.get(i).getName();
                    vsubmit_id = filteredlist.get(i).getVar_id();
                    attributevalue = filteredlist.get(i).getVar_name();
                    if(filteredlist.get(i).getVar_price().length()==0){
                        vsubmit_price = filteredlist.get(i).getSales_price();
                    }else{
                        vsubmit_price = filteredlist.get(i).getVar_price();
                    }
                    productimage = filteredlist.get(i).getImage();

                    Log.d("gvrtgv", vsubmit_id);

                    getindex(vsubmit_id);

                    int plusValue = count_value + 1;

                    double t = Double.parseDouble(vsubmit_price) * plusValue;

                    sharedPreference.editFavorite(context, indx, String.valueOf(t), String.valueOf(plusValue));

                    ItemCounter();
                    programViewHolder.tv_count.setText(plusValue + "");

                }
            });

            Log.d("wedfd", My_list.getName());
//            Log.d("wedfd", ""+My_list.getVariations().get(0).getPrice_id());

            if(My_list.getVariations().size()==0){
                programViewHolder.varlayout.setVisibility(View.GONE);
                programViewHolder.restt_price.setText("₹"+My_list.getSales_price());
            }else{
                programViewHolder.varlayout.setVisibility(View.VISIBLE);
                programViewHolder.restt_price.setText("₹ " + My_list.getVariations().get(0).getPrice());
                programViewHolder.spinertext.setText(My_list.getVariations().get(0).getVarations());

                filteredlist.get(i).setVar_id(My_list.getVariations().get(0).getPrice_id());
                filteredlist.get(i).setVar_name(My_list.getVariations().get(0).getVarations());
                filteredlist.get(i).setVar_price(My_list.getVariations().get(0).getPrice());
            }

            if(My_list.getVariations().size()==0){
                if (checkIfSameItem2(My_list.getProducts_id())) {
                    programViewHolder.l_add.setVisibility(View.GONE);
                    programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                    programViewHolder.tv_count.setText(qnty);
                } else {
                    programViewHolder.l_add.setVisibility(View.VISIBLE);
                    programViewHolder.l_add_cart.setVisibility(View.GONE);
                    programViewHolder.tv_count.setText(qnty);
                }
            }else {
                if (checkIfSameItem(My_list.getVariations().get(0).getPrice_id())) {
                    programViewHolder.l_add.setVisibility(View.GONE);
                    programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                    programViewHolder.tv_count.setText(qnty);
                } else {
                    programViewHolder.l_add.setVisibility(View.VISIBLE);
                    programViewHolder.l_add_cart.setVisibility(View.GONE);
                    programViewHolder.tv_count.setText(qnty);
                }
            }

        } catch (Exception e) {
            Log.d("wedfd", String.valueOf(e));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return filteredlist.size();
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView restrurant_food_name, restt_price, tv_minus, tv_plus, tv_count, addtext, discount, spinertext;
        LinearLayout l_add, l_add_cart, lay;
        Button add;
        ImageView imag_food;
        RelativeLayout item_qty_relative, instock, varlayout;
        LinearLayout foodItemMain;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            spinertext = (TextView) itemView.findViewById(R.id.spinertext);
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
            varlayout = (RelativeLayout) itemView.findViewById(R.id.varlayout);


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

    public boolean checkIfSameItem(String checkProduct) {
        boolean check = false;
        List<CartItem> favorites = sharedPreference.loadFavorites(context);
        if (favorites != null) {
            for (CartItem product : favorites) {

                if (product.getVarient_id().equals(checkProduct)) {

                    qnty = product.getQuantity();

                    Log.d("oapsd", qnty);

                    check = true;
                    break;

                }

            }
        }
        return check;

    }

    public boolean checkIfSameItem2(String checkProduct) {
        boolean check = false;
        List<CartItem> favorites = sharedPreference.loadFavorites(context);
        if (favorites != null) {
            for (CartItem product : favorites) {

                if (product.getProduct_id().equals(checkProduct)) {

                    qnty = product.getQuantity();

                    Log.d("oapsd", qnty);

                    check = true;
                    break;

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


            ProductList.itemcounter.setText(Constants.cartitems);


        } catch (Exception e) {


        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredlist = fooditem;
                } else {
                    ArrayList<ItemGetSet> filteredList = new ArrayList<>();
                    for (ItemGetSet row : fooditem) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredlist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredlist = (ArrayList<ItemGetSet>) filterResults.values;
                if(filteredlist.size()==0){
                    notifyDataSetChanged();
                    ProductList.recyle_iem.setVisibility(View.GONE);
                    ProductList.noproductfound.setVisibility(View.VISIBLE);
                }else {
                    notifyDataSetChanged();
                    ProductList.recyle_iem.setVisibility(View.VISIBLE);
                    ProductList.noproductfound.setVisibility(View.GONE);
                }
            }
        };
    }

}
