package in.freshshoppe.models;

public class VariationGetSet {

    String price_id, price, varations;

    public VariationGetSet(String price_id, String price, String varations) {
        this.price_id = price_id;
        this.price = price;
        this.varations = varations;
    }

    public String getPrice_id() {
        return price_id;
    }

    public VariationGetSet setPrice_id(String price_id) {
        this.price_id = price_id;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public VariationGetSet setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getVarations() {
        return varations;
    }

    public VariationGetSet setVarations(String varations) {
        this.varations = varations;
        return this;
    }
}
