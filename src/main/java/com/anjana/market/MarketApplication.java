package com.anjana.market;

import com.anjana.market.model.Stock;
import com.anjana.market.service.StockService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
public class MarketApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class, args);
    }


    /**
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        Scanner keys = new Scanner(System.in);
        String order = "";
        StockService stockService = new StockService();
        System.out.println("Format for inbound order: <S|B> <quantity> <price>. Type 'exit' to stop.");
        while (true) {
            order = keys.nextLine();

            if ("exit".equalsIgnoreCase(order)) {
                System.out.println("Thank you!");
                System.exit(1);
            }

            List<String> orderList = Stream.of(order.split(" "))
                    .map(elem -> new String(elem))
                    .collect(Collectors.toList());

            if (orderList.size() < 3) {
                System.out.println("All required values are not available! Format is <S|B> <quantity> <price>");
            }

            Stock stock = new Stock();
            stock.setInbound(orderList);

            String msg = stock.getErrors();
            if (msg == null || msg.length() <= 0) {
                stockService.processStockOrder(stock);
            } else {
                System.out.println(msg);
            }

        }

    }


}
