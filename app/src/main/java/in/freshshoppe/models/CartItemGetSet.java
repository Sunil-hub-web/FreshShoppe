package in.freshshoppe.models;

public class CartItemGetSet {

    String title, price, qty;

    public CartItemGetSet(String title, String price, String qty) {
        this.title = title;
        this.price = price;
        this.qty = qty;
    }

    public String getTitle() {
        return title;
    }

    public CartItemGetSet setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public CartItemGetSet setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getQty() {
        return qty;
    }

    public CartItemGetSet setQty(String qty) {
        this.qty = qty;
        return this;
    }
}
