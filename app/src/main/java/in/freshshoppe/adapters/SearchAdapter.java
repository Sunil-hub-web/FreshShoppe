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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.freshshoppe.R;
import in.freshshoppe.activities.ProductList;
import in.freshshoppe.extras.Constants;
import in.freshshoppe.extras.RecyclerTouchListener;
import in.freshshoppe.extras.SharedPreference;
import in.freshshoppe.models.CartItem;
import in.freshshoppe.models.ItemGetSet;
import in.freshshoppe.models.VariationGetSet;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ProgramViewHolder> {
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

    public SearchAdapter(ArrayList<ItemGetSet> fooditem, Context context) {
        this.fooditem = fooditem;
        this.context = context;

    }

    @NonNull
    @Override
    public SearchAdapter.ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.restaurant_item_layout, viewGroup, false);

        return new SearchAdapter.ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ProgramViewHolder programViewHolder, final int i) {
        final ItemGetSet My_list = fooditem.get(i);

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
                    variations = fooditem.get(i).getVariations();

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

                            fooditem.get(i).setVar_id(parenting.getPrice_id());
                            fooditem.get(i).setVar_price(parenting.getPrice());
                            fooditem.get(i).setVar_name(parenting.getVarations());


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

                    productid = fooditem.get(i).getProducts_id();
                    product_name = fooditem.get(i).getName();
                    vsubmit_id = fooditem.get(i).getVar_id();
                    attributevalue = fooditem.get(i).getVar_name();
                    vsubmit_price = fooditem.get(i).getVar_price();
                    productimage = fooditem.get(i).getImage();

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


            programViewHolder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productid = fooditem.get(i).getProducts_id();
                    product_name = fooditem.get(i).getName();
                    vsubmit_id = fooditem.get(i).getVar_id();
                    attributevalue = fooditem.get(i).getVar_name();
                    vsubmit_price = fooditem.get(i).getVar_price();
                    productimage = fooditem.get(i).getImage();

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
                    productid = fooditem.get(i).getProducts_id();
                    product_name = fooditem.get(i).getName();
                    vsubmit_id = fooditem.get(i).getVar_id();
                    attributevalue = fooditem.get(i).getVar_name();
                    vsubmit_price = fooditem.get(i).getVar_price();
                    productimage = fooditem.get(i).getImage();

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

                fooditem.get(i).setVar_id(My_list.getVariations().get(0).getPrice_id());
                fooditem.get(i).setVar_name(My_list.getVariations().get(0).getVarations());
                fooditem.get(i).setVar_price(My_list.getVariations().get(0).getPrice());
            }

            if (checkIfSameItem(My_list.getVariations().get(0).getPrice_id())) {
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return fooditem.size();
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

    public void ItemCounter() {

        try {
            favouritesBeanSampleList = sharedPreference.loadFavorites(context);
//        Log.d("fbfds", String.valueOf(favouritesBeanSampleList.size()));
//        ct=String.valueOf(favouritesBeanSampleList.size());


            ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;


//            ProductList.itemcounter.setText(Constants.cartitems);


        } catch (Exception e) {


        }

    }

}