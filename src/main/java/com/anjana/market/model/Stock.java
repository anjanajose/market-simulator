package com.anjana.market.model;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.UUID;

/**
 * Model object to hold each inbound order
 *
 * UUID is -> to each order to make it  distinct
 * time is -> to to keep the order entered time
 * slide -> B(buy) or S(sell)
 * quantity -> number of sell or buy
 * price -> Sell or Buy price
 *
 */
public class Stock {
    private UUID stockUUID;
    private long time;
    private String slide = "";
    private long quantity = 0;
    private double price = 0;

    public Stock() {
    }

    public Stock(Stock stock) {
        setStockUUID(stock.getStockUUID());
        setTime(stock.getTime());
        this.slide = stock.getSlide();
        this.quantity = stock.getQuantity();
        this.price = stock.getPrice();
    }

    public Stock(List<String> orderList) {
        setStockUUID(UUID.randomUUID());
        setTime(System.currentTimeMillis());
        setSlide(orderList.get(0));
        setQuantity(orderList.get(1));
        setPrice(orderList.get(2));
    }

    public UUID getStockUUID() {
        return stockUUID;
    }

    private void setStockUUID(UUID stockUUID) {
        this.stockUUID = stockUUID;
    }

    public long getTime() {
        return time;
    }

    private void setTime(long time) {
        this.time = time;
    }

    public String getSlide() {
        return slide;
    }

    public void setSlide(String slide) {
        this.slide = slide;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        long quantityLong = 0;
        try {
            quantityLong = Long.parseLong(quantity);
        } catch (NumberFormatException e) {
            quantityLong = 0;
        }
        this.quantity = quantityLong;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(String price) {
        double priceVal = 0;
        try {
            priceVal = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            priceVal = 0;
        }
        this.price = priceVal;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get all errors in the inbound order
     *
     * @return String
     */
    public String getErrors() {
        StringBuilder msg = new StringBuilder();

        if (!(Slide.BUY.getAction().equals(getSlide()) || Slide.SELL.getAction().equals(getSlide()))) {
            msg.append("B for Buy and S for Sell are the valid values for slide!").append("\n");
        }

        if (getQuantity() <= 0) {
            msg.append("Quantity should be a value greater than zero!").append("\n");
        }

        if (getPrice() <= 0) {
            msg.append("Price should be greater than zero!").append("\n");
        }

        String priceStr = String.valueOf(getPrice());
        if (!StringUtils.isEmpty(priceStr)) {
            String decimal = priceStr.substring(priceStr.indexOf(".") + 1);
            if (decimal.length() > 3)
                msg.append("Please enter price up to 3 decimal places!").append("\n");
        }


        return msg.toString();
    }

    @Override
    public String toString() {

        return "Stock{" +
                "stockUUID=" + stockUUID +
                ", time=" + time +
                ", slide='" + slide + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
