package com.example.dz_2_7;

public class Currency  {

    public final double dollarPriceWeight = 1;
    private String nameCurrency;
    private double priceWeight;
    private double volatil = 0;
    private double purshWeight;
    private double buyWeight;

    public Currency() {}

    public Currency(String nameCurrency, double priceWeight, double volatil) {
        this.nameCurrency = nameCurrency;
        this.priceWeight = priceWeight;
        this.volatil = volatil;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public double getPriceWeight() {
        return priceWeight;
    }

    public void setPriceWeight(double priceWeight) {
        this.priceWeight = priceWeight;
    }

    public double getVolatil() {
        return volatil;
    }

    public void setVolatil(double volatil) {
        this.volatil = volatil;
    }

    public double getPurshWeight() {
        return purshWeight;
    }

    public void setPurshWeight(double purshWeight) {
        this.purshWeight = purshWeight;
    }

    public void setPurshWeight() {
        if(volatil != 0)
            this.purshWeight = priceWeight-priceWeight*volatil;
        else
            this.purshWeight = priceWeight;
    }

    public double getBuyWeight() {
        return buyWeight;
    }

    public void setBuyWeight(double buyWeight) {
        this.buyWeight = buyWeight;
    }
    public void setBuyWeight() {
        if(volatil != 0)
            this.buyWeight = priceWeight+priceWeight*volatil;
        else buyWeight = priceWeight;
    }

    @Override
    public String toString() {
        return nameCurrency;
    }
}
