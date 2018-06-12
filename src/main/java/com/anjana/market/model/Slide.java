package com.anjana.market.model;

/**
 * ENUM for Slide options
 *
 * B -> Buy
 * S -> Sell
 */
public enum Slide {
    BUY("B"),SELL("S");
    private String action;

    Slide(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}

