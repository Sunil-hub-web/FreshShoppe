package in.freshshoppe.models;

import java.util.ArrayList;

public class ItemGetSet {

    String products_id, name, image, description, regular_price, sales_price, var_id, var_price, var_name;
    private ArrayList<VariationGetSet> variations;

    public ItemGetSet(String products_id, String name, String image, String description, String regular_price, String sales_price, String var_id, String var_price, String var_name, ArrayList<VariationGetSet> variations) {
        this.products_id = products_id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.regular_price = regular_price;
        this.sales_price = sales_price;
        this.var_id = var_id;
        this.var_price = var_price;
        this.var_name = var_name;
        this.variations = variations;
    }


    public String getProducts_id() {
        return products_id;
    }

    public ItemGetSet setProducts_id(String products_id) {
        this.products_id = products_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemGetSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemGetSet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public ItemGetSet setRegular_price(String regular_price) {
        this.regular_price = regular_price;
        return this;
    }

    public String getSales_price() {
        return sales_price;
    }

    public ItemGetSet setSales_price(String sales_price) {
        this.sales_price = sales_price;
        return this;
    }

    public ArrayList<VariationGetSet> getVariations() {
        return variations;
    }

    public ItemGetSet setVariations(ArrayList<VariationGetSet> variations) {
        this.variations = variations;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ItemGetSet setImage(String image) {
        this.image = image;
        return this;
    }

    public String getVar_id() {
        return var_id;
    }

    public ItemGetSet setVar_id(String var_id) {
        this.var_id = var_id;
        return this;
    }

    public String getVar_price() {
        return var_price;
    }

    public ItemGetSet setVar_price(String var_price) {
        this.var_price = var_price;
        return this;
    }

    public String getVar_name() {
        return var_name;
    }

    public ItemGetSet setVar_name(String var_name) {
        this.var_name = var_name;
        return this;
    }
}
