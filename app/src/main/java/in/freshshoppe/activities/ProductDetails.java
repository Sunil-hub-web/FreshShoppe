package in.freshshoppe.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.freshshoppe.R;
import in.freshshoppe.adapters.ItemAdapter;
import in.freshshoppe.models.ItemGetSet;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductDetails extends AppCompatActivity {

    ImageView backicon, iv_product_image, iv_wish;
    TextView tv_food_name, tv_food_price, tv_minus_one, tv_count_one, tv_plus_one;
    RecyclerView recyle_iem;
    ArrayList<ItemGetSet> itemArraylist;
    LinearLayout l_add_one, l_add_cart_one;
    String ifitemwishlisted = "No";
    NestedScrollView mainscroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backicon = findViewById(R.id.backicon);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyle_iem = findViewById(R.id.recyle_iem);
        tv_food_name = findViewById(R.id.tv_food_name);
        iv_product_image = findViewById(R.id.iv_product_image);
        tv_food_price = findViewById(R.id.tv_food_price);
        tv_minus_one = findViewById(R.id.tv_minus_one);
        tv_count_one = findViewById(R.id.tv_count_one);
        tv_plus_one = findViewById(R.id.tv_plus_one);
        l_add_one = findViewById(R.id.l_add_one);
        l_add_cart_one = findViewById(R.id.l_add_cart_one);
        iv_wish = findViewById(R.id.iv_wish);
        mainscroll = findViewById(R.id.mainscroll);



        iv_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifitemwishlisted.equalsIgnoreCase("No")) {
                    ifitemwishlisted = "Yes";
                    Toast.makeText(ProductDetails.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    iv_wish.setImageResource(R.drawable.heart_selected);

                }else if (ifitemwishlisted.equalsIgnoreCase("Yes")) {
                    ifitemwishlisted = "No";
                    Toast.makeText(ProductDetails.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                    iv_wish.setImageResource(R.drawable.heart);

                }
            }
        });

        l_add_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    l_add_one.setVisibility(View.GONE);
                    l_add_cart_one.setVisibility(View.VISIBLE);
                    tv_count_one.setText("1");

            }
        });

        tv_minus_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count_value = Integer.valueOf(tv_count_one.getText().toString());
                int count = count_value - 1;
                if (count == 0) {
                    l_add_one.setVisibility(View.VISIBLE);
                    l_add_cart_one.setVisibility(View.GONE);
                } else {

                    tv_count_one.setText(count + "");
                }
            }
        });

        tv_plus_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count_value = Integer.valueOf(tv_count_one.getText().toString());

                int plusValue = count_value + 1;

                tv_count_one.setText(plusValue + "");

            }
        });

        setData();

        tv_food_name.setText(getIntent().getStringExtra("name"));
        tv_food_price.setText("₹ " +getIntent().getStringExtra("price"));


    }

    public void setData(){
        itemArraylist = new ArrayList<ItemGetSet>();

//        itemArraylist.add(new ItemGetSet(R.drawable.prod1,"Chicken Biriyani Cut", "₹240.00", "₹130.00", "4.0★"));
//        itemArraylist.add(new ItemGetSet(R.drawable.prod2,"Chicken Breast Boneless", "₹250.00", "₹105.00", "3.6★"));
//        itemArraylist.add(new ItemGetSet(R.drawable.prod3,"Chieken Boneless Medium Cut", "₹250.00", "₹100.00", "4.2★"));
//        itemArraylist.add(new ItemGetSet(R.drawable.prod4,"Chieken Whole-Skinless (1-1.4Kg)", "₹240.00", "₹100.00", "3.0★"));
//        itemArraylist.add(new ItemGetSet(R.drawable.prod5,"Chicken Keema", "₹260.00", "₹100.00", "4.1★"));
//        itemArraylist.add(new ItemGetSet(R.drawable.prod6,"Chicken Legs", "₹260.00", "₹100.00", "4.0★"));


        ItemAdapter adpater = new ItemAdapter(itemArraylist, getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyle_iem.setLayoutManager(gridLayoutManager);
        recyle_iem.setItemAnimator(new DefaultItemAnimator());
        recyle_iem.setAdapter(adpater);

        mainscroll.fullScroll(View.FOCUS_DOWN);
        mainscroll.fullScroll(View.FOCUS_UP);
    }
}
