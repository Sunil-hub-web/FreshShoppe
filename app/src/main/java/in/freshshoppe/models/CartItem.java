package in.freshshoppe.models;

public class CartItem {

    public String product_id, product_name, productimage, varient_id, varient_name, sales_price, quantity, itemtotal;


    public CartItem(String product_id, String product_name, String productimage, String varient_id, String varient_name, String sales_price, String quantity, String itemtotal) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.productimage = productimage;
        this.varient_id = varient_id;
        this.varient_name = varient_name;
        this.sales_price = sales_price;
        this.quantity = quantity;
        this.itemtotal = itemtotal;
    }

    public String getProduct_id() {
        return product_id;
    }

    public CartItem setProduct_id(String product_id) {
        this.product_id = product_id;
        return this;
    }

    public String getProduct_name() {
        return product_name;
    }

    public CartItem setProduct_name(String product_name) {
        this.product_name = product_name;
        return this;
    }

    public String getProductimage() {
        return productimage;
    }

    public CartItem setProductimage(String productimage) {
        this.productimage = productimage;
        return this;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public CartItem setVarient_id(String varient_id) {
        this.varient_id = varient_id;
        return this;
    }

    public String getVarient_name() {
        return varient_name;
    }

    public CartItem setVarient_name(String varient_name) {
        this.varient_name = varient_name;
        return this;
    }

    public String getSales_price() {
        return sales_price;
    }

    public CartItem setSales_price(String sales_price) {
        this.sales_price = sales_price;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public CartItem setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getItemtotal() {
        return itemtotal;
    }

    public CartItem setItemtotal(String itemtotal) {
        this.itemtotal = itemtotal;
        return this;
    }
}
