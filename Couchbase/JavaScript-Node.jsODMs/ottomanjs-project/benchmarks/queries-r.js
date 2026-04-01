import {LineitemR} from "../models/tpc_h_r/lineitem-r.js";
import {OrdersR} from "../models/tpc_h_r/orders-r.js";
import {RegionR} from "../models/tpc_h_r/region-r.js";
import ottoman, {getDefaultInstance, Query} from "ottoman";
import {CustomerR} from "../models/tpc_h_r/customer-r.js";


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
 *
 ### A3) Indexed Columns

 This query selects all records from the customer table
 ```sql
 SELECT * FROM customer;
 ```
 */
async function A3(){
    const a3 = await CustomerR.find({} );

    return a3.rows;
}

/**
 * ### A4) Indexed Columns — Range Query
 *
 * This query selects all records from the orders table where the order key is between 1000 and 50000
 * ```sql
 * SELECT * FROM orders
 * WHERE o_orderkey BETWEEN 1000 AND 50000;
 * ```
 * @returns {Promise<TRow[]|any[]|TRow[]|TRow[]|ViewRow<TKey, TValue>[]|string[]|CppSearchResponseSearchRow[]|CppDocumentViewResponseRow[]|string|HTMLCollectionOf<HTMLTableRowElement>|number|SQLResultSetRowList>}
 * @constructor
 */
async function A4(){

    const a4 = await OrdersR.find({
        $or: [{
            o_orderkey_field: { $btw: [1000, 50000] }
        }]
    });

    return a4.rows;
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

async function B2() {

    const b2 = await ottoman.getDefaultInstance()
        .query(
            `SELECT DATE_FORMAT_STR(l.l_shipdate, "%Y-%m") AS ship_month,
                    MAX(l.l_extendedprice)                 AS max_price
             FROM \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`LineitemR\` AS l
             GROUP BY DATE_FORMAT_STR(l.l_shipdate, "%Y-%m");`
        );

    return b2.rows;
}

/**
 * ### C1) Non-Indexed Columns
 *
 * This query gives customer names, order dates, and total prices for customers
 * ```sql
 * SELECT c.c_name, o.o_orderdate, o.o_totalprice
 * FROM customer c, orders o;
 * ```
 * @returns {Promise<any[]>}
 * @constructor
 */
async function C1(){
    throw new Error("Not possible to implement in Couchbase")
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
 * ### C3) Complex Join 1
 *
 * This query gives customer names, nation names, order dates, and total prices for customers
 * ```sql
 * SELECT c.c_name, n.n_name, o.o_orderdate, o.o_totalprice
 * FROM customer c
 * JOIN nation n ON c.c_nationkey = n.n_nationkey
 * JOIN orders o ON c.c_custkey = o.o_custkey;
 * ```
 * @returns {Promise<any[]>}
 * @constructor
 */
async function C3(){

    const c3 = await ottoman.getDefaultInstance()
        .query(
            `SELECT c.c_name, n.n_name, o.o_orderdate, o.o_totalprice
             FROM \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`CustomerR\` AS c
                      JOIN \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`NationR\` AS n
                           ON c.c_nationkey = META(n).id
                      JOIN \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`OrdersR\` AS o
                           ON META(c).id = o.o_custkey;`

        );

    return c3.rows;
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
 * @returns {Promise<any[]>}
 * @constructor
 */
async function C4(){

    const c4 = await ottoman.getDefaultInstance()
        .query(
            `SELECT c.c_name, n.n_name, r.r_name, o.o_orderdate, o.o_totalprice
             FROM \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`CustomerR\` AS c
                      JOIN \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`NationR\` AS n
                           ON c.c_nationkey = META(n).id
                      JOIN \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`RegionR\` AS r
                           ON n.n_regionkey = META(r).id
                      JOIN \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`OrdersR\` AS o
                           ON META(c).id = o.o_custkey;`

        );

    return c4.rows;
}

async function C5(){

    const c5 = await ottoman.getDefaultInstance()
        .query(
            `SELECT META(c).id AS c_custkey, c.c_name, META(o).id AS o_orderkey, o.o_orderdate
             FROM \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`CustomerR\` AS c
                      LEFT JOIN \`ottoman_bucket_r\`.\`ottoman_scope_r\`.\`OrdersR\` AS o
                                ON META(c).id = o.o_custkey;`

        );

    return c5.rows;
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
    A3,
    A4,
    B1,
    B2,
    C1,
    C2,
    C3,
    C4,
    C5,
    D1
}