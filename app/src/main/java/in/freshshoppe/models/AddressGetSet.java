package in.freshshoppe.models;

public class AddressGetSet {

    String addres_id, name, state_id, state_name, city_id, city_name, pin_id, pincode, number, address;

    public AddressGetSet(String addres_id, String name, String state_id, String state_name, String city_id, String city_name, String pin_id, String pincode, String number, String address) {
        this.addres_id = addres_id;
        this.name = name;
        this.state_id = state_id;
        this.state_name = state_name;
        this.city_id = city_id;
        this.city_name = city_name;
        this.pin_id = pin_id;
        this.pincode = pincode;
        this.number = number;
        this.address = address;
    }

    public String getAddres_id() {
        return addres_id;
    }

    public AddressGetSet setAddres_id(String addres_id) {
        this.addres_id = addres_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AddressGetSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getState_id() {
        return state_id;
    }

    public AddressGetSet setState_id(String state_id) {
        this.state_id = state_id;
        return this;
    }

    public String getState_name() {
        return state_name;
    }

    public AddressGetSet setState_name(String state_name) {
        this.state_name = state_name;
        return this;
    }

    public String getCity_id() {
        return city_id;
    }

    public AddressGetSet setCity_id(String city_id) {
        this.city_id = city_id;
        return this;
    }

    public String getCity_name() {
        return city_name;
    }

    public AddressGetSet setCity_name(String city_name) {
        this.city_name = city_name;
        return this;
    }

    public String getPin_id() {
        return pin_id;
    }

    public AddressGetSet setPin_id(String pin_id) {
        this.pin_id = pin_id;
        return this;
    }

    public String getPincode() {
        return pincode;
    }

    public AddressGetSet setPincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public AddressGetSet setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public AddressGetSet setAddress(String address) {
        this.address = address;
        return this;
    }
}
