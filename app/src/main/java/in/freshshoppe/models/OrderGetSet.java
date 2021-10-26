package in.freshshoppe.models;

import java.util.ArrayList;

public class OrderGetSet {

    String order_id, order_status, shiping_type, shipping_charge, payment_mode, subtotal, total, datetime;
    ArrayList<OrderItemGetSet> itemsarray;

    public OrderGetSet(String order_id, String order_status, String shiping_type, String shipping_charge, String payment_mode, String subtotal, String total, String datetime, ArrayList<OrderItemGetSet> itemsarray) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.shiping_type = shiping_type;
        this.shipping_charge = shipping_charge;
        this.payment_mode = payment_mode;
        this.subtotal = subtotal;
        this.total = total;
        this.datetime = datetime;
        this.itemsarray = itemsarray;
    }


    public String getOrder_id() {
        return order_id;
    }

    public OrderGetSet setOrder_id(String order_id) {
        this.order_id = order_id;
        return this;
    }

    public String getOrder_status() {
        return order_status;
    }

    public OrderGetSet setOrder_status(String order_status) {
        this.order_status = order_status;
        return this;
    }

    public String getShiping_type() {
        return shiping_type;
    }

    public OrderGetSet setShiping_type(String shiping_type) {
        this.shiping_type = shiping_type;
        return this;
    }

    public String getShipping_charge() {
        return shipping_charge;
    }

    public OrderGetSet setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
        return this;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public OrderGetSet setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
        return this;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public OrderGetSet setSubtotal(String subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public String getTotal() {
        return total;
    }

    public OrderGetSet setTotal(String total) {
        this.total = total;
        return this;
    }

    public ArrayList<OrderItemGetSet> getItemsarray() {
        return itemsarray;
    }

    public OrderGetSet setItemsarray(ArrayList<OrderItemGetSet> itemsarray) {
        this.itemsarray = itemsarray;
        return this;
    }

    public String getDatetime() {
        return datetime;
    }

    public OrderGetSet setDatetime(String datetime) {
        this.datetime = datetime;
        return this;
    }
}
