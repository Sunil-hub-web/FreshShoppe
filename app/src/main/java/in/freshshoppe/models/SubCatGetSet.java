package in.freshshoppe.models;

public class SubCatGetSet {

    String subcate_id, subcate_name, subcate_img;

    public SubCatGetSet(String subcate_id, String subcate_name, String subcate_img) {
        this.subcate_id = subcate_id;
        this.subcate_name = subcate_name;
        this.subcate_img = subcate_img;
    }

    public String getSubcate_id() {
        return subcate_id;
    }

    public SubCatGetSet setSubcate_id(String subcate_id) {
        this.subcate_id = subcate_id;
        return this;
    }

    public String getSubcate_name() {
        return subcate_name;
    }

    public SubCatGetSet setSubcate_name(String subcate_name) {
        this.subcate_name = subcate_name;
        return this;
    }

    public String getSubcate_img() {
        return subcate_img;
    }

    public SubCatGetSet setSubcate_img(String subcate_img) {
        this.subcate_img = subcate_img;
        return this;
    }
}
