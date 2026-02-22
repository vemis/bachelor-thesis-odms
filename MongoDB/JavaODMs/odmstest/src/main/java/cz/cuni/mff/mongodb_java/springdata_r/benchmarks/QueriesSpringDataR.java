package cz.cuni.mff.mongodb_java.springdata_r.benchmarks;


import cz.cuni.mff.mongodb_java.springdata_r.model.*;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

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

        GroupOperation groupOperation = Aggregation
                .group().and("_id",
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
     * ### D1) UNION
     *
     * This query combines customer and supplier nation keys
     * ```sql
     * (SELECT c_nationkey FROM customer)
     * UNION
     * (SELECT s_nationkey FROM supplier);
     * ```
     * @param datastore
     */
    /*public static List<Document> D1(Datastore datastore) {
        List<Document> result = datastore.aggregate(CustomerR.class)
                // SELECT c_nationkey AS nationkey
                .project(
                        Projection.project()
                                .include("nationkey", Expressions.field("c_nationkey"))
                                .exclude("_id")
                ) //size() 30_000

                // UNION supplier
                .unionWith(SupplierR.class,
                        Projection.project()
                                .include("nationkey", Expressions.field("s_nationkey"))
                                .exclude("_id")
                ) //size() 32_000

                // Remove duplicates (SQL UNION behavior)
                .group(
                        Group.group(
                                Group.id(
                                        Expressions.field("nationkey")
                                )

                        )
                ) //25

                // Final reshape
                .project(
                        Projection.project()
                                .include("nationkey", Expressions.field("_id"))
                                .exclude("_id")
                )

                .execute(Document.class)
                .toList();

                return result;
    }*/
}
