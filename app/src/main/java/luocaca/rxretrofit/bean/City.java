package luocaca.rxretrofit.bean;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public class City {
    private String city = "";
    private String district = "";
    private String id = "";
    private String province = "";

    //省略getter,setter,toString方法


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", id='" + id + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
