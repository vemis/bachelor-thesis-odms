import {LineitemR} from "../models/tpc_h_r/lineitem-r.js";
import {OrdersR} from "../models/tpc_h_r/orders-r.js";
import {RegionR} from "../models/tpc_h_r/region-r.js";
import ottoman, {getDefaultInstance, Query} from "ottoman";


/**
 * A1) Non-Indexed Columns
 *
 * This query selects all records from the lineitem table
 * ```sql
 *         SELECT * FROM lineitem;
 * ```
 */
async function A1(){
    /*
    Ops/sec: 0.00
    Average time per op: 220113.647 ms
    */
    const a1 = await LineitemR.find({} );

    return a1.rows;
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
async function A2(){
    /*
    Ops/sec: 0.08
    Average time per op: 12577.724 ms
    */
    const a2 = await OrdersR.find({
        $or: [{
                o_orderdate: { $btw: [new Date('1996-01-01'), new Date('1996-12-31')] }
        }]
    });

    return a2.rows;
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
async function B1() {
    /*
    Ops/sec: 0.10
    Average time per op: 9653.446 ms
    */
    const b1 = await ottoman.getDefaultInstance()
        .query(
            "SELECT COUNT(META(o).id) AS order_count, DATE_FORMAT_STR(o.o_orderdate, \"YYYY-MM\") AS order_month\n" +
            "FROM ottoman_bucket_r.ottoman_scope_r.OrdersR AS o\n" +
            "GROUP BY DATE_FORMAT_STR(o.o_orderdate, \"YYYY-MM\")"
        );

    return b1.rows;
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
async function C2(){
    /*
    Ops/sec: 0.10
    Average time per op: 9905.980 ms
    */
    const c2 = await ottoman.getDefaultInstance()
        .query(
    "SELECT c.c_name AS cName, o.o_orderdate AS oOrderDate, o.o_totalprice AS oTotalPrice"+
    " FROM ottoman_bucket_r.ottoman_scope_r.CustomerR c"+
    " JOIN ottoman_bucket_r.ottoman_scope_r.OrdersR o ON META(c).id = o.o_custkey"
    );

    return c2.rows;
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
async function D1(){
    /*
    Ops/sec: 1.33
    Average time per op: 751.370 ms
    */
    const d1 = await ottoman.getDefaultInstance()
        .query(
            `SELECT c.c_nationkey AS nationkey
                    FROM ottoman_bucket_r.ottoman_scope_r.CustomerR AS c
                    UNION
                    SELECT s.s_nationkey AS nationkey
                    FROM ottoman_bucket_r.ottoman_scope_r.SupplierR AS s;`
        )
    return d1.rows;
}

export {
    A1,
    A2,
    B1,
    C2,
    D1
}