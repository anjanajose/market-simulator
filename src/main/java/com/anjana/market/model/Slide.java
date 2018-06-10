package com.anjana.market.model;

public enum Slide {
    BUY("B"),SELL("S");

    private  String action;

    private Slide(String action)
    {
        this.action = action;
    }

    public String getAction()
    {
        return this.action;
    }
}
