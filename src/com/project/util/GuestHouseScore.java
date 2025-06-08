package com.project.util;

import com.project.vo.GuestHouse;

public class GuestHouseScore {
    public GuestHouse guesthouse;
    public double score;

    public GuestHouseScore(GuestHouse guesthouse, double score) {
        this.guesthouse = guesthouse;
        this.score = score;
    }

   public GuestHouse getGuesthouse() {
      return guesthouse;
   }

   public void setGuesthouse(GuestHouse guesthouse) {
      this.guesthouse = guesthouse;
   }

   public double getScore() {
      return score;
   }

   public void setScore(double score) {
      this.score = score;
   }
   

}
