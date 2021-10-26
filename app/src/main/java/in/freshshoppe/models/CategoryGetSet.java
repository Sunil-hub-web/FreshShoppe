package in.freshshoppe.models;

import java.util.ArrayList;

public class CategoryGetSet {

    String category_id, cate_name, cate_img;
    private ArrayList<SubCatGetSet> subcats;


    public CategoryGetSet(String category_id, String cate_name, String cate_img, ArrayList<SubCatGetSet> subcats) {
        this.category_id = category_id;
        this.cate_name = cate_name;
        this.cate_img = cate_img;
        this.subcats = subcats;
    }

    public String getCategory_id() {
        return category_id;
    }

    public CategoryGetSet setCategory_id(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public String getCate_name() {
        return cate_name;
    }

    public CategoryGetSet setCate_name(String cate_name) {
        this.cate_name = cate_name;
        return this;
    }

    public String getCate_img() {
        return cate_img;
    }

    public CategoryGetSet setCate_img(String cate_img) {
        this.cate_img = cate_img;
        return this;
    }

    public ArrayList<SubCatGetSet> getSubcats() {
        return subcats;
    }

    public CategoryGetSet setSubcats(ArrayList<SubCatGetSet> subcats) {
        this.subcats = subcats;
        return this;
    }
}
