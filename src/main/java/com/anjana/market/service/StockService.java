package com.anjana.market.service;

import com.anjana.market.model.Slide;
import com.anjana.market.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Service class to handle the stock orders
 *
 */
@Service
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    //buys ordered by highest price at the top
    private Set<Stock> buys = new TreeSet<>((s1, s2) -> {
        if (s1.getStockUUID() == s2.getStockUUID()) {
            return 0;
        }

        if (s1.getPrice() < s2.getPrice() || (s1.getPrice() == s2.getPrice() && s1.getTime() > s2.getTime())) {
            return 1;
        }

        return -1;
    });

    //sells ordered by lowest price at the top
    private Set<Stock> sells = new TreeSet<>((s1, s2) -> {
        if (s1.getStockUUID() == s2.getStockUUID()) {
            return 0;
        }

        if (s1.getPrice() > s2.getPrice() || (s1.getPrice() == s2.getPrice() && s1.getTime() > s2.getTime())) {
            return 1;
        }

        return -1;
    });

    /**
     * Processing based on the slide -> buy or sell
     *
     * @param stock
     */
    public void processStockOrder(Stock stock) {
        LOGGER.info("processStockOrder", stock);
        if (Slide.BUY.getAction().equalsIgnoreCase(stock.getSlide())) {
            processBuy(stock);
        } else {
            processSell(stock);
        }
        print();
    }

    /**
     * processing buy
     *
     * @param stock
     */
    private void processBuy(Stock stock) {
        buys.add(stock);
        for (Stock s : sells) {
            trade(s);
        }
    }

    /**
     * processing sell
     *
     * @param stock
     */
    private void processSell(Stock stock) {
        sells.add(stock);
        trade(stock);
    }

    /**
     * Trade based on available quantity/price
     *
     * @param stock
     */
    private void trade(Stock stock) {
        long quantity = stock.getQuantity();

        Iterator<Stock> iter = buys.iterator();
        while (iter.hasNext()) {
            Stock s = iter.next();
            //trade only if the buy price is greater than or equal to the sell price
            if (quantity > 0 && stock.getPrice() <= s.getPrice()) {
                //if the sell quantity is greater than selected buy,
                //then trade what is in buy quantity
                // Else, trade the whole sell quantity
                long traded = quantity;
                if (s.getQuantity() < quantity) {
                    traded = s.getQuantity();
                }

                //Printing traded quantity as of now
                System.out.println(traded + "@" + s.getPrice());

                //resetting the buy quantity
                if (quantity >= s.getQuantity()) {
                    iter.remove();
                } else {
                    s.setQuantity(s.getQuantity() - quantity);
                }

                quantity = quantity - s.getQuantity();
            }
        }
        //resetting the sell quantity
        if (quantity <= 0) {
            sells.remove(stock);
        } else {
            stock.setQuantity(quantity);
        }
    }

    /**
     *Print Stock Book
     *
     */
    private void print() {
        System.out.println("--------------------------------------");
        System.out.printf("%-15s %-5s %s\n\n", "Buy", "|", "Sell");

        Map<String, Stock> buyMap = getStockForDisplay(buys);
        Map<String, Stock> sellMap = getStockForDisplay(sells);

        Iterator<String> buyIter = buyMap.keySet().iterator();
        Iterator<String> sellIter = sellMap.keySet().iterator();

        while (buyIter.hasNext() && sellIter.hasNext()) {
            Stock buy = buyMap.get(buyIter.next());
            Stock sell = sellMap.get(sellIter.next());

            System.out.printf("%-15s %-5s %s\n", buy.getQuantity() + "@" + buy.getPrice(), "|", sell.getQuantity() + "@" + sell.getPrice());

        }

        while (buyIter.hasNext()) {
            Stock buy = buyMap.get(buyIter.next());

            System.out.printf("%-15s %-5s\n", buy.getQuantity() + "@" + buy.getPrice(), "|");
        }

        while (sellIter.hasNext()) {
            Stock sell = sellMap.get(sellIter.next());

            System.out.printf("%-15s %-5s %s\n", "", "|", sell.getQuantity() + "@" + sell.getPrice());
        }
        System.out.println("--------------------------------------");
    }

    /**
     * Get stock book
     *
     * @param set
     * @return
     */
    private Map<String, Stock> getStockForDisplay(Set<Stock> set) {
        Map<String, Stock> map = new LinkedHashMap<>();
        for (Stock stock : set) {
            String key = String.valueOf(stock.getPrice());
            //if the same price exists, the quantity is combined together
            if (!ObjectUtils.isEmpty(map.get(key))) {
                Stock t = map.get(key);
                t.setQuantity(t.getQuantity() + stock.getQuantity());
            } else {
                map.put(key, new Stock(stock));
            }
        }
        return map;
    }
}
