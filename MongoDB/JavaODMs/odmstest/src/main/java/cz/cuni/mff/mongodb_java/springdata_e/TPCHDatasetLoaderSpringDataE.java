package cz.cuni.mff.mongodb_java.springdata_e;


import cz.cuni.mff.mongodb_java.TPCHDatasetLoader;
import cz.cuni.mff.mongodb_java.springdata_e.model.CustomerEWithOrders;
import cz.cuni.mff.mongodb_java.springdata_e.model.LineitemE;
import cz.cuni.mff.mongodb_java.springdata_e.model.OrdersE;
import cz.cuni.mff.mongodb_java.springdata_e.model.OrdersEWithLineitems;
import cz.cuni.mff.mongodb_java.springdata_e.model.OrdersEWithLineitemsArrayAsTags;
import cz.cuni.mff.mongodb_java.springdata_e.model.OrdersEWithLineitemsArrayAsTagsIndexed;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * Loading could be done using constructor, but having doubts abour the performance
 */
public class TPCHDatasetLoaderSpringDataE extends TPCHDatasetLoader {


    public static void loadCustomers(String filePath, List<OrdersE> orders, MongoTemplate mongoTemplate) {

        List<String[]> customers = readDataFromCustomSeparator(filePath);

        LongAdder counter = new LongAdder();
        int total = customers.size();

        List<CustomerEWithOrders> customerInstances = customers
                .parallelStream()
                .map(row ->
                {
                    counter.increment();
                    long current = counter.sum();

                    if (current % 10_000 == 0) {
                        System.out.println("Processed " + current + " / " + total);
                    }

                    return new CustomerEWithOrders(
                            Integer.parseInt(row[0]),
                            row[1],
                            row[2],
                            Integer.parseInt(row[3]),
                            row[4],
                            Double.parseDouble(row[5]),
                            row[6],
                            row[7],
                            orders.stream()
                                    .filter(item -> item.getO_custkey() == Integer.parseInt(row[0]))
                                    .collect(Collectors.toList())
                    );
                })
                .toList();

        mongoTemplate.insert(customerInstances, CustomerEWithOrders.class);
    }

    public static List<LineitemE> createLineitemsE(String filePath) {

        List<String[]> lineitems = readDataFromCustomSeparator(filePath);

        LongAdder counter = new LongAdder();
        int total = lineitems.size();

        List<LineitemE> lineitemInstances = lineitems
                .parallelStream()
                .map(row ->
                {
                    counter.increment();
                    long current = counter.sum();

                    if (current % 10_000 == 0) {
                        System.out.println("Processed " + current + " / " + total);
                    }

                    return new LineitemE(
                            Integer.parseInt(row[0]),
                            Integer.parseInt(row[1]),
                            Integer.parseInt(row[2]),
                            Integer.parseInt(row[3]),
                            Integer.parseInt(row[4]),
                            Double.parseDouble(row[5]),
                            Double.parseDouble(row[6]),
                            Double.parseDouble(row[7]),
                            row[8],
                            row[9],
                            LocalDate.parse(row[10]),
                            LocalDate.parse(row[11]),
                            LocalDate.parse(row[12]),
                            row[13],
                            row[14],
                            row[15]
                    );
                })
                .toList();

        return lineitemInstances;
    }



    public static void loadOrdersEWithLineitems(String filePath, List<LineitemE> lineitems, MongoTemplate mongoTemplate) {

        List<String[]> orders = readDataFromCustomSeparator(filePath);

        Map<Integer, List<LineitemE>> lineitemsMappedBy_l_orderkey = groupListsByKey(lineitems, LineitemE::get_l_orderkey);

        LongAdder counter = new LongAdder();
        int total = orders.size();

        List<OrdersEWithLineitems> orderInstances = orders
                .parallelStream()
                .map(row ->
                {
                    counter.increment();
                    long current = counter.sum();

                    if (current % 10_000 == 0) {
                        System.out.println("Processed " + current + " / " + total);
                    }

                    return new OrdersEWithLineitems(
                            Integer.parseInt(row[0]),
                            Integer.parseInt(row[1]),
                            row[2],
                            row[4],
                            LocalDate.parse(row[4]),
                            row[5],
                            row[6],
                            row[7],
                            row[8],
                            lineitemsMappedBy_l_orderkey.get(Integer.parseInt(row[0]))
                    );
                })
                .toList();

        var batches = partition(orderInstances, 200_000);

        System.out.println("Inserting many ordersEWithLineitems!");

        for (var batch : batches) {
            mongoTemplate.insert(batch, OrdersEWithLineitems.class);
            System.out.println("Batch inserted!");
        }

        System.out.println("ordersEWithLineitems inserted!");
    }

    public static void loadOrdersEWithLineitemsArrayAsTags(String filePathOrders, String filePathLineitems, MongoTemplate mongoTemplate) {
        List<String[]> orders = readDataFromCustomSeparator(filePathOrders);

        List<String[]> lineitems = readDataFromCustomSeparator(filePathLineitems);

        String[] linetemsRow2 = lineitems.get(1); // 2nd row - unique elements

        LongAdder counter = new LongAdder();
        int total = orders.size();

        List<OrdersEWithLineitemsArrayAsTags> orderInstances = orders
                .parallelStream()
                .map(row ->
                {
                    counter.increment();
                    long current = counter.sum();

                    if (current % 10_000 == 0) {
                        System.out.println("Processed " + current + " / " + total);
                    }

                    return new OrdersEWithLineitemsArrayAsTags(
                            Integer.parseInt(row[0]),
                            LocalDate.parse(row[4]),
                            Arrays.asList(getShuffledLineitemsTagsFromRow(linetemsRow2, Integer.parseInt(row[0])))
                    );
                })
                .toList();

        var batches = partition(orderInstances, 200_000);

        System.out.println("Inserting many ordersEWithLineitemsArrayAsTags!");

        for (var batch : batches) {
            mongoTemplate.insert(batch, OrdersEWithLineitemsArrayAsTags.class);
            System.out.println("Batch inserted!");
        }

        System.out.println("ordersEWithLineitemsArrayAsTags inserted!");
    }

    public static void loadOrdersEWithLineitemsArrayAsTagsIndexed(String filePathOrders, String filePathLineitems, MongoTemplate mongoTemplate) {
        List<String[]> orders = readDataFromCustomSeparator(filePathOrders);

        List<String[]> lineitems = readDataFromCustomSeparator(filePathLineitems);

        String[] linetemsRow2 = lineitems.get(1); // 2nd row - unique elements

        LongAdder counter = new LongAdder();
        int total = orders.size();

        List<OrdersEWithLineitemsArrayAsTagsIndexed> orderInstances = orders
                .parallelStream()
                .map(row ->
                {
                    counter.increment();
                    long current = counter.sum();

                    if (current % 10_000 == 0) {
                        System.out.println("Processed " + current + " / " + total);
                    }

                    return new OrdersEWithLineitemsArrayAsTagsIndexed(
                            Integer.parseInt(row[0]),
                            LocalDate.parse(row[4]),
                            Arrays.asList(getShuffledLineitemsTagsFromRow(linetemsRow2, Integer.parseInt(row[0])))
                    );
                })
                .toList();

        var batches = partition(orderInstances, 200_000);

        System.out.println("Inserting many ordersEWithLineitemsArrayAsTagsIndexed!");

        for (var batch : batches) {
            mongoTemplate.insert(batch, OrdersEWithLineitemsArrayAsTagsIndexed.class);
            System.out.println("Batch inserted!");
        }

        System.out.println("ordersEWithLineitemsArrayAsTagsIndexed inserted!");
    }

    public static List<OrdersE> loadOrders(String filePath) {

        List<String[]> orders = readDataFromCustomSeparator(filePath);

        LongAdder counter = new LongAdder();
        int total = orders.size();

        List<OrdersE> orderInstances = orders
                .parallelStream()
                .map(row ->
                {
                    counter.increment();
                    long current = counter.sum();

                    if (current % 10_000 == 0) {
                        System.out.println("Processed " + current + " / " + total);
                    }

                    return new OrdersE(
                            Integer.parseInt(row[0]),
                            Integer.parseInt(row[1]),
                            row[2],
                            row[4],
                            LocalDate.parse(row[4]),
                            row[5],
                            row[6],
                            row[7],
                            row[8]
                    );
                })
                .toList();


        System.out.println("orders created!");
        return orderInstances;
    }

}
