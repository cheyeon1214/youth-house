package com.project.util;

import com.project.vo.GuestHouse;

public class RawGHData {
    public GuestHouse guesthouse;
    public double price;
   public double rating;
   public double sales;
    
    public RawGHData(GuestHouse guesthouse, double rating, double sales) {
        this.guesthouse = guesthouse;
        this.price = 0;
        this.rating = rating;
        this.sales = sales;
    }

    public RawGHData(GuestHouse guesthouse, double price, double rating, double sales) {
        this.guesthouse = guesthouse;
        this.price = price;
        this.rating = rating;
        this.sales = sales;
    }
    
  

   public double getPrice() {
      return price;
   }

   public GuestHouse getGuesthouse() {
      return guesthouse;
   }

   public void setGuesthouse(GuestHouse guesthouse) {
      this.guesthouse = guesthouse;
   }

   public void setPrice(double price) {
      this.price = price;
   }
    
    
}
