package cz.cuni.mff.mongodb_java.morphia.benchmarks;

import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_relational.*;
import dev.morphia.Datastore;
import dev.morphia.aggregation.expressions.DateExpressions;
import dev.morphia.aggregation.expressions.Expressions;
import dev.morphia.aggregation.stages.*;
import dev.morphia.aggregation.expressions.AccumulatorExpressions;


import dev.morphia.query.FindOptions;
import org.bson.Document;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static dev.morphia.query.filters.Filters.*;

public class QueriesMorphiaR {
    /**
     * A1) Non-Indexed Columns
     *
     * This query selects all records from the lineitem table
     * ```sql
     *         SELECT * FROM lineitem;
     * ```
     */
    public static List<LineitemR> A1(Datastore datastore) {
        List<LineitemR> a1 = datastore
                .find(LineitemR.class)
                .iterator()
                .toList();

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
     * @param datastore
     * @return
     */
    public static List<OrdersR> A2(Datastore datastore) {
        List<OrdersR> a2 = datastore
                .find(OrdersR.class)
                .filter(gte("o_orderdate", LocalDate.parse("1996-01-01")), lte("o_orderdate", LocalDate.parse("1996-12-31")))
                .iterator()
                .toList();

        return a2;
    }

    /**
     * ### A3) Indexed Columns
     *
     * This query selects all records from the customer table
     * ```sql
     * SELECT * FROM customer;
     * ```
     * */
    public static List<CustomerR> A3(Datastore datastore) {
        List<CustomerR> a3 = datastore
                .find(CustomerR.class)
                .iterator()
                .toList();

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
     * */
    public static List<OrdersR> A4(Datastore datastore) {
        List<OrdersR> a4 = datastore
                .find(OrdersR.class)
                .filter(gte("o_orderkey", 1000), lte("o_orderkey",50000 ))
                .iterator()
                .toList();

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
    public static List<Document> B1(Datastore datastore) {

         List<Document> aggregation = datastore.aggregate(OrdersR.class)
                .group(
                        Group.group(Group.id(

                                        DateExpressions.dateToString()
                                                .format("%Y-%m")
                                                .date(Expressions.field("o_orderdate"))

                                )
                        ).field("order_count", AccumulatorExpressions.sum(Expressions.value(1)))
                )
                 .project(
                         Projection.project()
                                 .suppressId()
                                .include("order_count")
                                .include("order_month", Expressions.field("_id"))
                )
                .execute(Document.class)
                .toList();

         return aggregation;
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
     * */
    public static List<Document> B2(Datastore datastore){
        List<Document> aggregation = datastore.aggregate(LineitemR.class)
                .group(
                        Group.group(Group.id(

                                        DateExpressions.dateToString()
                                                .format("%Y-%m")
                                                .date(Expressions.field("l_shipdate"))

                                )
                        ).field("max_price", AccumulatorExpressions.max(Expressions.field("l_extendedprice")))
                )
                .project(
                        Projection.project()
                                .suppressId()
                                .include("max_price")
                                .include("ship_month", Expressions.field("_id"))
                )
                .execute(Document.class)
                .toList();

        return aggregation;
    }


    /**
     *
     ### C1) Non-Indexed Columns

     This query gives customer names, order dates, and total prices for customers
     ```sql
     SELECT c.c_name, o.o_orderdate, o.o_totalprice
     FROM customer c, orders o;
     ```
     */
    public static List<Document> C1(Datastore datastore) {
        List<Document> c1 = datastore.aggregate(CustomerR.class)
                .lookup(
                        Lookup.lookup(OrdersR.class)
                                //.localField("_id")
                                //.foreignField("o_custkey")
                                .pipeline()
                                .as("ordersR")

                )
                .unwind(Unwind.unwind("ordersR"))
                .project(Projection.project()
                        .suppressId()
                        .include("c_name")
                        .include("ordersR.o_orderdate")
                        .include("ordersR.o_totalprice")
                )
                .limit(300_500)
                .execute(Document.class)
                .toList();

        return c1;
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
    public static List<Document> C2(Datastore datastore) {
        List<Document> c2 = datastore.aggregate(CustomerR.class)
                .lookup(
                        Lookup.lookup(OrdersR.class)
                                .localField("_id")
                                .foreignField("o_custkey")
                                .as("ordersR")

                )
                .unwind(Unwind.unwind("ordersR"))
                .project(Projection.project()
                        .suppressId()
                        .include("c_name")
                        .include("ordersR.o_orderdate")
                        .include("ordersR.o_totalprice")
                )
                .execute(Document.class)
                .toList();

        return c2;
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
     */
    public static List<Document> C3(Datastore datastore) {
        List<Document> c3 = datastore.aggregate(CustomerR.class)
                .lookup(
                        Lookup.lookup(NationR.class)
                                .localField("c_nationkey")
                                .foreignField("_id")
                                .as("nationR")

                )
                .unwind(Unwind.unwind("nationR"))

                .lookup(
                        Lookup.lookup(OrdersR.class)
                                .localField("_id")
                                .foreignField("o_custkey")
                                .as("ordersR")

                )
                .unwind(Unwind.unwind("ordersR"))

                .project(Projection.project()
                        .suppressId()
                        .include("c_name")
                        .include("nationR.n_name")
                        .include("ordersR.o_orderdate")
                        .include("ordersR.o_totalprice")
                )
                .execute(Document.class)
                .toList();

        return c3;
    }


    /**
     * ### C4) Complex Join 2
     *
     * This query gives customer names, nation names, region names, order dates, and total prices for customers
     * ```sql
     * SELECT c.c_name, n.n_name, r.r_name, o.o_orderdate, o.o_totalprice
     * FROM customer c
     * JOIN nation n ON c.c_nationkey = n.n_nationkey
     * JOIN region r ON n.n_regionkey = r.r_regionkey
     * JOIN orders o ON c.c_custkey = o.o_custkey;
     * ```
     */
    public static List<Document> C4(Datastore datastore) {
        List<Document> c4 = datastore.aggregate(CustomerR.class)
                .lookup(
                        Lookup.lookup(NationR.class)
                                .localField("c_nationkey")
                                .foreignField("_id")
                                .as("nationR")

                )
                .unwind(Unwind.unwind("nationR"))

                .lookup(
                        Lookup.lookup(RegionR.class)
                                .localField("nationR.n_regionkey")
                                .foreignField("_id")
                                .as("regionR")

                )
                .unwind(Unwind.unwind("nationR"))

                .lookup(
                        Lookup.lookup(OrdersR.class)
                                .localField("_id")
                                .foreignField("o_custkey")
                                .as("ordersR")

                )
                .unwind(Unwind.unwind("ordersR"))

                .project(Projection.project()
                        .suppressId()
                        .include("c_name")
                        .include("nationR.n_name")
                        .include("regionR.r_name")
                        .include("ordersR.o_orderdate")
                        .include("ordersR.o_totalprice")
                )
                .execute(Document.class)
                .toList();

        return c4;
    }


    /**
     * ### C5) Left Outer Join
     *
     * This query gives customer names and order dates for all customers, including those without orders
     * ```sql
     * SELECT c.c_custkey, c.c_name, o.o_orderkey, o.o_orderdate
     * FROM customer c
     * LEFT OUTER JOIN orders o ON c.c_custkey = o.o_custkey;
     * ```
     */
    public static List<Document> C5(Datastore datastore) {
        List<Document> c5 = datastore.aggregate(CustomerR.class)
                .lookup(
                        Lookup.lookup(OrdersR.class)
                                .localField("_id")
                                .foreignField("o_custkey")
                                .as("ordersR")

                )
                .unwind(Unwind.unwind("ordersR")
                        .preserveNullAndEmptyArrays(true))

                .project(Projection.project()
                        .suppressId()
                        .include("c_name")
                        .include("ordersR.o_orderdate")
                        .include("ordersR.o_totalprice")
                )
                .execute(Document.class)
                .toList();

        return c5;
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
    public static List<Document> D1(Datastore datastore) {
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
    }

    /**
     * ### D2) INTERSECT
     *
     * This query finds common customer and supplier keys
     * MySQL doesn't directly support INTERSECT, so I used IN
     * ```sql
     * SELECT DISTINCT c.c_custkey
     * FROM customer c
     * WHERE c.c_custkey IN (
     *     SELECT s.s_suppkey
     *     FROM supplier s
     * );
     * ```
     */
    public static List<Document> D2(Datastore datastore) {
        List<Document> d2 = datastore.aggregate(CustomerR.class)

                .group(
                        Group.group(
                                Group.id(
                                        Expressions.field("_id")
                                )

                        )
                )
                .lookup(
                        Lookup.lookup(SupplierR.class)
                                .localField("_id")
                                .foreignField("_id")
                                .as("matchesR")
                )
                .match(
                        ne("matchesR", new Object[0])
                )
                .project(
                        Projection.project()
                                .suppressId()
                                .include("key", Expressions.field("_id"))
                )


                .execute(Document.class)
                .toList();

        return d2;
    }


    /**
     * ### D3) DIFFERENCE
     *
     * This query finds customer keys that are not in the supplier table
     * MySQL doesn't directly support EXCEPT/MINUS, so I used NOT IN
     * ```sql
     * SELECT DISTINCT c.c_custkey
     * FROM customer c
     * WHERE c.c_custkey NOT IN (
     *     SELECT DISTINCT s.s_suppkey
     *     FROM supplier s
     * );
     * ```
     */
    public static List<Document> D3(Datastore datastore) {
        List<Document> d3 = datastore.aggregate(CustomerR.class)

                .lookup(
                        Lookup.lookup(SupplierR.class)
                                .localField("_id")
                                .foreignField("_id")
                                .as("matchesR")
                )
                .match(
                        eq("matchesR", new Object[0]) // keep only those NOT found in supplier
                )
                .group(
                        Group.group(
                                Group.id("_id")
                        )
                )
                .project(
                        Projection.project()
                                .suppressId()
                                .include("key",  Expressions.field("_id"))
                )


                .execute(Document.class)
                .toList();

        return d3;
    }


    /**
     * ### E1) Non-Indexed Columns Sorting
     *
     * This query sorts customer names, addresses, and account balances in descending order of account balance
     * ```sql
     * SELECT c_name, c_address, c_acctbal
     * FROM customer
     * ORDER BY c_acctbal DESC
     * ```
     */
    public static List<CustomerR> E1(Datastore datastore) {
        List<CustomerR> e1 = datastore.find(CustomerR.class, new FindOptions()
                        .sort(dev.morphia.query.Sort.descending ("c_acctbal"))
                        .projection()
                        .include("c_name", "c_address", "c_acctbal")
                )
                .iterator()
                .toList();

        return e1;
    }

    /**
     * ### E2) Indexed Columns Sorting
     *
     * This query sorts order keys, customer keys, order dates, and total prices in ascending order of order key
     * ```sql
     * SELECT o_orderkey, o_custkey, o_orderdate, o_totalprice
     * FROM orders
     * ORDER BY o_orderkey
     * ```
     */
    public static List<OrdersR> E2(Datastore datastore) {
        List<OrdersR> e2 = datastore.find(OrdersR.class, new FindOptions()
                        .sort(dev.morphia.query.Sort.ascending ("_id"))
                        .projection()
                        .include("_id", "o_custkey", "o_orderdate", "o_totalprice")
                )
                .iterator()
                .toList();

        return e2;
    }

    /**
     * ### E3) Distinct
     *
     * This query selects distinct nation keys and market segments from the customer table
     * ```sql
     * SELECT DISTINCT c_nationkey, c_mktsegment
     * FROM customer;
     * ```
     */
    public static List<Document> E3(Datastore datastore) {
        var e3 = datastore.aggregate(CustomerR.class)
                .group(
                        Group.group(
                                Group.id(
                                            Expressions.document()
                                                    .field("c_nationkey", Expressions.field("c_nationkey"))
                                                    .field("c_mktsegment", Expressions.field("c_mktsegment"))
                                        )
                        )
                )
                .project(
                        Projection.project()

                                .include("c_nationkey", Expressions.field("_id.c_nationkey"))
                                .include("c_mktsegment", Expressions.field("_id.c_mktsegment"))
                                .suppressId()
                );

        return e3
                .execute(Document.class)
                .toList();

        //return e3;
    }


}


