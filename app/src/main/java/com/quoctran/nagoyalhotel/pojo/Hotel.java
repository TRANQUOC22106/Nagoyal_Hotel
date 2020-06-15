package com.quoctran.nagoyalhotel.pojo;

public class Hotel {
    private String nameHotel;
    private String locationHotel;
    private int priceHotel;
    private String imageHotel;
    private String link;
    public Hotel() {
    }

    public String getNameHotel() {
        return nameHotel;
    }

    public String getLocationHotel() {
        return locationHotel;
    }

    public int getPriceHotel() {
        return priceHotel;
    }

    public String getImageHotel() {
        return imageHotel;
    }

    public String getLink() {
        return link;
    }

    public Hotel(String nameHotel, String locationHotel, int priceHotel, String imageHotel, String link) {
        this.nameHotel = nameHotel;
        this.locationHotel = locationHotel;
        this.priceHotel = priceHotel;
        this.imageHotel = imageHotel;
        this.link = link;
    }
}
