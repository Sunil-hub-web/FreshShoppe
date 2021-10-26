package in.freshshoppe.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import in.freshshoppe.models.CartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {


    public static final String PREFS_NAME = "NKDROID_APP";
    public static final String FAVORITES = "Favorite";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    double sum = 0.00;
    ArrayList<CartItem> itms;


    public SharedPreference() {
        super();
    }


    public void storeFavorites(Context context, List<CartItem> favorites) {


        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public ArrayList<CartItem> loadFavorites(Context context) {
        SharedPreferences settings;
        List<CartItem> favorites;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            CartItem[] favoriteItems = gson.fromJson(jsonFavorites,CartItem[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<CartItem>(favorites);
        } else
            return null;

        return (ArrayList<CartItem>) favorites;
    }


    public void addFavorite(Context context, CartItem beanSampleList) {
        List<CartItem> favorites = loadFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<CartItem>();
        favorites.add(beanSampleList);
        storeFavorites(context, favorites);
    }

    public void removeFavorite(Context context, int beanSampleList) {

        ArrayList<CartItem> favorites = loadFavorites(context);

        Log.d("RanjeetShared", String.valueOf(favorites.size()));
        if (favorites != null) {
            favorites.remove(beanSampleList);

            Log.d("RanjeetShared", String.valueOf(favorites.size()));
            storeFavorites(context, favorites);
        }



    }


    public void editFavorite(Context context, int beanSampllist, String textprice, String quanity){

        Log.d("wertjn_1","");
        Log.d("wertjn_2", String.valueOf(beanSampllist));
        Log.d("wertjn_3",textprice);
        Log.d("wertjn_4",quanity);

        ArrayList<CartItem> favorites =loadFavorites(context);

        if(favorites!=null){

            String ranjeet= favorites.get(beanSampllist).getItemtotal();
            Log.d("RanjeetShared",ranjeet);

            favorites.get(beanSampllist).setItemtotal(textprice);

            Log.d("RanjeetShared",favorites.get(beanSampllist).getItemtotal());

            favorites.get(beanSampllist).setQuantity(quanity);
            storeFavorites(context, favorites);
        }

    }


    public void clearDate(Context context)
    {

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        // Clearing all data from Shared Preferences

        editor.remove(FAVORITES);
        editor.apply();
        editor.clear();
        editor.commit();

    }


    public double getCartAmount(Context context){
        itms = new ArrayList<>();

        itms =loadFavorites(context);
        for (int j = 0; j < itms.size(); j++) {


            CartItem cartIt = itms.get(j);

            sum = sum + (Double.parseDouble(cartIt.getSales_price())* Double.parseDouble(cartIt.getQuantity()));
//            itemtotal.setText("₹"+sum);
//                shippingsum = shippingsum + Double.parseDouble(cartIt.getShipingcost());
        }

        return sum;
    }


}
