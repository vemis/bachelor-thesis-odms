package cz.cuni.mff.mongodb_java.morphia.benchmarks;

import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_embedded.CustomerEWithOrders;
import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_embedded.OrdersEWithLineitems;
import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_relational.CustomerR;
import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_relational.OrdersR;
import dev.morphia.Datastore;
import dev.morphia.aggregation.expressions.Expressions;
import dev.morphia.aggregation.stages.Lookup;
import dev.morphia.aggregation.stages.Match;
import dev.morphia.aggregation.stages.Projection;
import dev.morphia.aggregation.stages.Unwind;
import dev.morphia.query.filters.Filters;
import org.bson.Document;

import java.util.List;

public class QueriesMorphiaE {
    /**
     * ### C2)
     *
     * Retrieve fields from an embedded document inside an array.
     * ```MongoDB
     * db.customerEWithOrders.aggregate([
     *   {
     *     $unwind: "$orders"
     *   },
     *   {
     *     $project: {
     *       c_name: 1,
     *       o_orderdate: "$orders.o_orderdate",
     *       o_totalprice: "$orders.o_totalprice"
     *     }
     *   }
     * ])
     * ```
     */
    public static List<Document> C2(Datastore datastore) {
        List<Document> results = datastore.aggregate(CustomerEWithOrders.class)
                .unwind(Unwind.unwind("orders") )
                .project(Projection.project()
                        .include("c_name")
                        .include("o_orderdate", Expressions.field("orders.o_orderdate"))
                        .include("o_totalprice", Expressions.field("orders.o_totalprice"))
                )
                .execute(Document.class)
                .toList();

        return results;
    }

    /**
     * ### R1) Embedded Customer with Orders Query
     *
     * Test performance of fetching nested documents (1:N relationship embedded).
     * ```MongoDB
     * db.customerEWithOrders.aggregate([
     *   { $match: { "orders.o_totalprice": { $gt: 259276 } } },
     *   { $project: { c_name: 1, "orders.o_totalprice": 1 } }
     * ])
     * ```
     */
    public static List<Document> R1_Customer_Deprecated(Datastore datastore) {
        List<Document> results = datastore.aggregate(CustomerEWithOrders.class)
                .match(Filters.gt("orders.o_totalprice", 259276))
                .project(Projection.project()
                        .include("c_name")
                        .include("orders.o_totalprice")
                )
                .execute(Document.class)
                .toList();

        return results;
    }

    /**
     * ### R1) Embedded Orders with Lineitems Query
     *
     * Test performance of fetching nested documents (1:N relationship embedded).
     * ```MongoDB
     * db.ordersEWithLineitems.aggregate([
     *   { $match: { "o_lineitems.l_quantity": { $gt: 5 } } },
     *   { $project: { o_orderdate: 1, "o_lineitems.l_partkey": 1 } }
     * ])
     * ```
     */
    public static List<Document> R1(Datastore datastore) {
        List<Document> results = datastore.aggregate(OrdersEWithLineitems.class)
                .match(Filters.gt("o_lineitems.l_quantity", 5))
                .project(Projection.project()
                        .include("o_orderdate")
                        .include("o_lineitems.l_partkey")
                )
                .execute(Document.class)
                .toList();

        return results;
    }

    /**
     * ### R2) Embedded Orders with Lineitems Query — Indexed Field
     *
     * Test performance of fetching nested documents (1:N relationship embedded) on indexed field.
     * ```MongoDB
     * db.ordersEWithLineitems.aggregate([
     *   { $match: { "o_lineitems.l_partkey": { $gt: 20000 } } },
     *   { $project: { o_orderdate: 1, "o_lineitems.l_partkey": 1 } }
     * ])
     * ```
     */
    public static List<Document> R2(Datastore datastore) {
        List<Document> results = datastore.aggregate(OrdersEWithLineitems.class)
                .match(Filters.gt("o_lineitems.l_partkey", 20000))
                .project(Projection.project()
                        .include("o_orderdate")
                        .include("o_lineitems.l_partkey")
                )
                .execute(Document.class)
                .toList();

        return results;
    }

    /**
     * ### R7) Unwind Embedded Lineitems
     *
     * Test unwind of embedded objects (array flattening cost).
     * ```MongoDB
     * db.ordersEWithLineitems.aggregate([
     *   { $unwind: "$o_lineitems" },
     *   { $project: { _id: 1, "o_lineitems.l_partkey": 1 } }
     * ])
     * ```
     */
    public static List<Document> R7(Datastore datastore) {
        List<Document> results = datastore.aggregate(OrdersEWithLineitems.class)
                .unwind(Unwind.unwind("o_lineitems"))
                .project(Projection.project()
                        .include("o_lineitems.l_partkey")
                )
                .execute(Document.class)
                .toList();

        return results;
    }
}
