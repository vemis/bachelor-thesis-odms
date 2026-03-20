package cz.cuni.mff.couchbase_java.springdata_e.benchmarks;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;

import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.QueryCriteria;

import java.time.LocalDate;
import java.util.List;

public class QueriesSpringDataE {
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
    public static List<JsonObject> C2(Cluster cluster){
        String query =
         "SELECT" +
                " c.c_name," +
                " o.o_orderdate," +
                " o.o_totalprice" +
        " FROM ottoman_bucket_e.ottoman_scope_e.CustomerEWithOrders AS c" +
        " UNNEST c.c_orders AS o;";

        return cluster
                .query(query)
                .rowsAsObject();
    }
}
