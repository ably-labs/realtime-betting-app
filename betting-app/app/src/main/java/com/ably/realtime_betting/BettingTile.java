package com.ably.realtime_betting;

import com.google.gson.Gson;

public class BettingTile {
    private final String name;
    private final String imageURL;
    private Float odds;

    public BettingTile(String name, String imageURL, Float odds){
        this.name = name;
        this.imageURL = imageURL;
        this.odds = odds;
    }

    public String getName(){
        return name;
    }
    public String getImageURL(){
        return imageURL;
    }
    public String getOdds(){
        return convertDecimalToFraction( odds);
    }
    public void setOdds(float newOdds){
        this.odds = newOdds;
    }
    static private String convertDecimalToFraction(double x){
        if (x < 0){
            return "-" + convertDecimalToFraction(-x);
        }
        double tolerance = 1.0E-6;
        double h1=1; double h2=0;
        double k1=0; double k2=1;
        double b = x;
        do {
            double a = Math.floor(b);
            double aux = h1; h1 = a*h1+h2; h2 = aux;
            aux = k1; k1 = a*k1+k2; k2 = aux;
            b = 1/(b-a);
        } while (Math.abs(x-h1/k1) > x*tolerance);

        return (int)h1+"/"+(int)k1;
    }
}
