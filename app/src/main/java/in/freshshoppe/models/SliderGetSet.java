package in.freshshoppe.models;

public class SliderGetSet {

   String banner_id, title, img;

    public SliderGetSet(String banner_id, String title, String img) {
        this.banner_id = banner_id;
        this.title = title;
        this.img = img;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public SliderGetSet setBanner_id(String banner_id) {
        this.banner_id = banner_id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SliderGetSet setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImg() {
        return img;
    }

    public SliderGetSet setImg(String img) {
        this.img = img;
        return this;
    }
}
