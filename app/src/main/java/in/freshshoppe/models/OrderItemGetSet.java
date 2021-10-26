package in.freshshoppe.models;

public class OrderItemGetSet {

    String id, name, qty, img, price, weight;


    public OrderItemGetSet(String id, String name, String qty, String img, String price, String weight) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.img = img;
        this.price = price;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public OrderItemGetSet setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public OrderItemGetSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getQty() {
        return qty;
    }

    public OrderItemGetSet setQty(String qty) {
        this.qty = qty;
        return this;
    }

    public String getImg() {
        return img;
    }

    public OrderItemGetSet setImg(String img) {
        this.img = img;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public OrderItemGetSet setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public OrderItemGetSet setWeight(String weight) {
        this.weight = weight;
        return this;
    }
}
