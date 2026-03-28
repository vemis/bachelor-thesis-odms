package cz.cuni.mff.mongodb_java.springdata_r.benchmarks;


import cz.cuni.mff.mongodb_java.springdata_r.model.*;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class QueriesSpringDataR {
    /**
     * A1) Non-Indexed Columns
     *
     * This query selects all records from the lineitem table
     * ```sql
     *         SELECT * FROM lineitem;
     * ```
     */
    public static List<LineitemR> A1(MongoTemplate mongoTemplate) {
        List<LineitemR> a1 = mongoTemplate.findAll(LineitemR.class);

        return a1;
    }

    /**
     * A2) Non-Indexed Columns — Range Query
     *
     * This query selects all records from the orders table where the order date is between '1996-01-01' and '1996-12-31'
     * ```sql
     * SELECT * FROM orders
     * WHERE o_orderdate
     *     BETWEEN '1996-01-01' AND '1996-12-31';
     * ```
     */
    public static List<OrdersR> A2(MongoTemplate mongoTemplate) {
        /*List<OrdersR> a2 = datastore
                .find(OrdersR.class)
                .filter(gte("o_orderdate", LocalDate.parse("1996-01-01")), lte("o_orderdate", LocalDate.parse("1996-12-31")))
                .iterator()
                .toList();*/
        Query query = new Query().addCriteria(
                Criteria.where("o_orderdate")
                        .gte(LocalDate.parse("1996-01-01"))
                        .lte(LocalDate.parse("1996-12-31"))
        );
        var a2 = mongoTemplate.find(query, OrdersR.class);

        return a2;
    }

    /**
     * ### A3) Indexed Columns
     *
     * This query selects all records from the customer table
     * ```sql
     * SELECT * FROM customer;
     * ```
     */
    public static List<CustomerR> A3(MongoTemplate mongoTemplate){
        List<CustomerR> a3 = mongoTemplate.findAll(CustomerR.class);

        return a3;
    }

    /**
     * ### A4) Indexed Columns — Range Query
     *
     * This query selects all records from the orders table where the order key is between 1000 and 50000
     * ```sql
     * SELECT * FROM orders
     * WHERE o_orderkey BETWEEN 1000 AND 50000;
     * ```
     */
    public static List<OrdersR> A4(MongoTemplate mongoTemplate) {
        Query query = new Query().addCriteria(
                Criteria.where("_id")
                        .gte(1000)
                        .lte(50000)
        );
        var a4 = mongoTemplate.find(query, OrdersR.class);

        return a4;
    }


    /**
     * ### B1) COUNT
     *
     * This query counts the number of orders grouped by order month
     * ```sql
     * SELECT COUNT(o.o_orderkey) AS order_count,
     *        DATE_FORMAT(o.o_orderdate, '%Y-%m') AS order_month
     * FROM orders o
     * GROUP BY order_month;
     * ```
     */
    public static List<Document> B1(MongoTemplate mongoTemplate) {

        GroupOperation groupOperation = group().and("_id",
                        DateOperators.dateOf("o_orderdate")
                        .toString("%Y-%m")
                )
                .count().as("order_count");

        ProjectionOperation projectionOperation = project()
                .and("_id").as("orderMonth")
                .and("order_count").as("orderCount")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(
                groupOperation,
                projectionOperation
        );

        return mongoTemplate.aggregate(
                aggregation,
                OrdersR.class,
                Document.class
        ).getMappedResults();
    }

    /**
     * ### B2) MAX
     *
     * This query finds the maximum extended price from the lineitem table grouped by ship month
     * ```sql
     * SELECT DATE_FORMAT(l.l_shipdate, '%Y-%m') AS ship_month,
     *        MAX(l.l_extendedprice) AS max_price
     * FROM lineitem l
     * GROUP BY ship_month;
     * ```
     */
    public static List<Document> B2(MongoTemplate mongoTemplate) {

        GroupOperation groupOperation = group().and("_id",
                        DateOperators.dateOf("l_shipdate")
                                .toString("%Y-%m")
                )
                .max("l_extendedprice").as("max_price");

        ProjectionOperation projectionOperation = project()
                .and("_id").as("shipMonth")
                .and("max_price").as("maxPrice")
                .andExclude("_id");

        Aggregation aggregation = newAggregation(
                groupOperation,
                projectionOperation
        );

        return mongoTemplate.aggregate(
                aggregation,
                LineitemR.class,
                Document.class
        ).getMappedResults();
    }

    /**
     * ## C) Joins
     *
     * ### C1) Non-Indexed Columns
     *
     * This query gives customer names, order dates, and total prices for customers
     * ```sql
     * SELECT c.c_name, o.o_orderdate, o.o_totalprice
     * FROM customer c, orders o;
     * ```
     */
    public static List<Document> C1(MongoTemplate mongoTemplate){
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("ordersR")
                //.localField("_id")
                //.foreignField("o_custkey")
                .pipeline()
                .as("ordersR");
        UnwindOperation unwindOperation = Aggregation.unwind("ordersR");

        ProjectionOperation projectionOperation = project()
                //.andExclude("_id")
                .and("c_name").as("c_name")
                .and("ordersR.o_orderdate").as("o_orderdate")
                .and("ordersR.o_totalprice").as("o_totalprice");

        LimitOperation limitOperation = Aggregation.limit(1_500_000);

        Aggregation aggregation = newAggregation(
                lookupOperation,
                unwindOperation,
                projectionOperation,
                limitOperation
        );

        AggregationResults<Document> results =
                mongoTemplate.aggregate(
                        aggregation,
                        CustomerR.class,
                        Document.class);

        return results.getMappedResults();
    }


    /**
     * ### C2) Indexed Columns
     *
     * This query gives customer names, order dates, and total prices for all customers
     * ```sql
     * SELECT c.c_name, o.o_orderdate, o.o_totalprice
     * FROM customer c
     * JOIN orders o ON c.c_custkey = o.o_custkey;
     * ```
     */
    public static List<Document> C2(MongoTemplate mongoTemplate) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("ordersR")
                .localField("_id")
                .foreignField("o_custkey")
                .as("ordersR");

        UnwindOperation unwindOperation = Aggregation.unwind("ordersR");

        ProjectionOperation projectionOperation = project()
                //.andExclude("_id")
                .and("c_name").as("c_name")
                .and("ordersR.o_orderdate").as("o_orderdate")
                .and("ordersR.o_totalprice").as("o_totalprice");

        Aggregation aggregation = newAggregation(
                lookupOperation,
                unwindOperation,
                projectionOperation
        );

        AggregationResults<Document> results =
                mongoTemplate.aggregate(
                        aggregation,
                        CustomerR.class,
                        Document.class);

        return results.getMappedResults();
    }


    /**
     * ### C3) Complex Join 1
     *
     * This query gives customer names, nation names, order dates, and total prices for customers
     * ```sql
     * SELECT c.c_name, n.n_name, o.o_orderdate, o.o_totalprice
     * FROM customer c
     * JOIN nation n ON c.c_nationkey = n.n_nationkey
     * JOIN orders o ON c.c_custkey = o.o_custkey;
     * ```
     * @param mongoTemplate
     * @return
     */
    public static List<Document> C3(MongoTemplate mongoTemplate) {

        // Join customer -> orders
        LookupOperation lookupOrders = LookupOperation.newLookup()
                .from("ordersR")
                .localField("_id")              // c_custkey
                .foreignField("o_custkey")
                .as("ordersR");

        UnwindOperation unwindOrders = Aggregation.unwind("ordersR");

        // Join customer -> nation
        LookupOperation lookupNation = LookupOperation.newLookup()
                .from("nationR")
                .localField("c_nationkey")
                .foreignField("_id")            // n_nationkey
                .as("nationR");

        UnwindOperation unwindNation = Aggregation.unwind("nationR");

        // Final projection
        ProjectionOperation projection = project()
                .and("c_name").as("c_name")
                .and("nationR.n_name").as("n_name")
                .and("ordersR.o_orderdate").as("o_orderdate")
                .and("ordersR.o_totalprice").as("o_totalprice");

        Aggregation aggregation = newAggregation(
                lookupOrders,
                unwindOrders,
                lookupNation,
                unwindNation,
                projection
        );

        AggregationResults<Document> results =
                mongoTemplate.aggregate(
                        aggregation,
                        CustomerR.class,
                        Document.class
                );

        return results.getMappedResults();
    }


    /**
     * ### D1) UNION
     *
     * This query combines customer and supplier nation keys
     * ```sql
     * (SELECT c_nationkey FROM customer)
     * UNION
     * (SELECT s_nationkey FROM supplier);
     * ```
     */
    public static List<Document> D1(MongoTemplate mongoTemplate) {
        // Stage 1: project from customerR
        ProjectionOperation projectCustomer =
                Aggregation.project()
                        .and("c_nationkey").as("nationkey");

        // Stage 2: unionWith supplierR
        UnionWithOperation unionWithSupplier =
                UnionWithOperation.unionWith("supplierR")
                        .pipeline(
                                project("s_nationkey")
                                        .and("s_nationkey").as("nationkey")
                                        .andExclude("_id")
                        );

        // Stage 3: remove duplicates via group
        GroupOperation groupByNationKey =
                group("nationkey");

        // Stage 4: reshape output
        ProjectionOperation finalProjection =
                project()
                        .and("_id").as("nationkey")
                        .andExclude("_id");

        Aggregation aggregation = newAggregation(
                projectCustomer,
                unionWithSupplier,
                groupByNationKey,
                finalProjection
        );

        return mongoTemplate.aggregate(
                aggregation,
                CustomerR.class,
                Document.class
        ).getMappedResults();
    }
}
