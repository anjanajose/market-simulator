package com.anjana.market.service;

import com.anjana.market.model.Slide;
import com.anjana.market.model.Stock;

import java.util.*;

/**
 *
 *
 */
public class StockService {


    private Set<Stock> buys = new TreeSet<>((s1, s2) -> {
        if (s1.getStockUUID() == s2.getStockUUID())
            return 0;

        if (s1.getPrice() < s2.getPrice() || (s1.getPrice() == s2.getPrice() && s1.getTime() > s2.getTime()))
            return 1;

        return -1;
    });

    private Set<Stock> sells = new TreeSet<>((s1, s2) -> {
        if (s1.getStockUUID() == s2.getStockUUID())
            return 0;

        if (s1.getPrice() > s2.getPrice() || (s1.getPrice() == s2.getPrice() && s1.getTime() > s2.getTime()))
            return 1;

        return -1;
    });

    /**
     * @param stock
     */
    public void processStockOrder(Stock stock) {
        if (Slide.BUY.getAction().equalsIgnoreCase(stock.getSlide()))
            processBuy(stock);
        else
            processSell(stock);
        print();

    }

    /**
     * @param stock
     */
    private void processBuy(Stock stock) {
        buys.add(stock);
        for (Stock s : sells) {
            trade(s);
        }
    }

    /**
     * @param stock
     */
    private void processSell(Stock stock) {
        sells.add(stock);
        trade(stock);
    }

    /**
     * @param stock
     */
    private void trade(Stock stock) {
        long quantity = stock.getQuantity();

        Iterator<Stock> iter = buys.iterator();
        while (iter.hasNext()) {
            Stock s = iter.next();
            if (quantity > 0 && stock.getPrice() <= s.getPrice()) {
                long traded = quantity;
                if (s.getQuantity() < quantity)
                    traded = s.getQuantity();

                System.out.println(traded + "@" + s.getPrice());

                if (quantity >= s.getQuantity())
                    iter.remove();
                else
                    s.setQuantity(s.getQuantity() - quantity);

                quantity = quantity - s.getQuantity();
            }
        }

        if (quantity <= 0)
            sells.remove(stock);
        else
            stock.setQuantity(quantity);
    }

    /**
     *
     *
     */
    private void print() {
        System.out.printf("\n%-20s %-20s %s\n", "Buy", "|", "Sell");

        Map<String, Stock> buyMap = getStockForDisplay(buys);
        Map<String, Stock> sellMap = getStockForDisplay(sells);

        Iterator<String> buyIter = buyMap.keySet().iterator();
        Iterator<String> sellIter = sellMap.keySet().iterator();

        while (buyIter.hasNext() && sellIter.hasNext()) {
            Stock buy = buyMap.get(buyIter.next());
            Stock sell = sellMap.get(sellIter.next());

            System.out.printf("%-20s %-20s %s\n", buy.getQuantity() + "@" + buy.getPrice(), "|", sell.getQuantity() + "@" + sell.getPrice());

        }

        while (buyIter.hasNext()) {
            Stock buy = buyMap.get(buyIter.next());

            System.out.printf("%-20s %-20s\n", buy.getQuantity() + "@" + buy.getPrice(), "|");
        }

        while (sellIter.hasNext()) {
            Stock sell = sellMap.get(sellIter.next());

            System.out.printf("%-20s %-20s %s\n", "", "|", sell.getQuantity() + "@" + sell.getPrice());
        }
    }

    /**
     * @param set
     * @return
     */
    private Map<String, Stock> getStockForDisplay(Set<Stock> set) {
        Map<String, Stock> map = new LinkedHashMap<>();
        for (Stock stock : set) {
            String key = String.valueOf(stock.getPrice());
            if (map.get(key) != null) {
                Stock t = map.get(key);
                t.setQuantity(t.getQuantity() + stock.getQuantity());
            } else {
                map.put(key, new Stock(stock));
            }
        }
        return map;
    }
}
