package com.anjana.market;

import com.anjana.market.model.Stock;
import com.anjana.market.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
public class MarketApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketApplication.class);

    @Autowired
    private StockService stockService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MarketApplication.class);
        app.run(args);
    }


    /**
     * Read and process inputs
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) {

        Scanner keys = null;
        try {
            keys = new Scanner(System.in);
            String order = "";

            System.out.println("Format for inbound order: <S|B> <quantity> <price>. Type 'exit' to stop.");
            while (true) {
                order = keys.nextLine();
                if ("exit".equalsIgnoreCase(order)) {
                    System.out.println("Thank you!");
                    System.exit(0);
                }

                List<String> orderList = Stream.of(order.split("\\s+"))
                        .map(elem -> new String(elem))
                        .collect(Collectors.toList());

                if (orderList.size() < 3) {
                    System.out.println("All required values are not available! Format is <S|B> <quantity> <price>");
                    continue;
                }

                Stock stock = new Stock();
                stock.setInbound(orderList);

                String msg = stock.getErrors();
                if (StringUtils.isEmpty(msg)) {
                    stockService.processStockOrder(stock);
                } else {
                    System.out.println(msg);
                }

            }
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage());
        }
        finally {
            if(!ObjectUtils.isEmpty(keys))
                keys.close();
        }

    }
}