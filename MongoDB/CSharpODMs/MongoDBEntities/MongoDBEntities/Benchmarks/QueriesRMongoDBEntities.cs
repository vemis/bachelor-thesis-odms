using MongoDB.Bson;
using MongoDB.Driver;
using MongoDB.Entities;
using MongoDBEntities.Models.TPC_H;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Reflection.Metadata;
using System.Text;
using System.Threading.Tasks;

namespace MongoDBEntities.Benchmarks
{
    public class QueriesRMongoDBEntities
    {
        /*
        A1) Non-Indexed Columns          
        This query selects all records from the lineitem table
        ```sql
            SELECT * FROM lineitem;
        ```
         */
        public static async Task<List<LineitemR>> A1Async()
        {
            var a1 = await DB.Find<LineitemR>()
                .ExecuteAsync();

            return a1;
        }

        /*
        A2) Non-Indexed Columns — Range Query
         
        This query selects all records from the orders table where the order date is between '1996-01-01' and '1996-12-31'
        ```sql
        SELECT * FROM orders
        WHERE o_orderdate
            BETWEEN '1996-01-01' AND '1996-12-31';
        ```
        */
        public static async Task<List<OrdersR>> A2()
        {


            var a2 = await DB.Find<OrdersR>()
                .Match(
                    o => o.o_orderdate.DateTime > DateTime.Parse("1996-01-01")
                    && o.o_orderdate.DateTime < DateTime.Parse("1996-12-31") 
                )
                .ExecuteAsync();

            return a2;        
        }

        /**
        ### B1) COUNT
        
        This query counts the number of orders grouped by order month
        ```sql
        SELECT COUNT(o.o_orderkey) AS order_count,
               DATE_FORMAT(o.o_orderdate, '%Y-%m') AS order_month
        FROM orders o
        GROUP BY order_month;
        ```
        */
        public static async Task<List<BsonDocument>> B1()
        {
            var b1 = await DB.Fluent<OrdersR>()
                .Group(
                    x => x.o_orderdate.DateTime.Year.ToString() + "-" + x.o_orderdate.DateTime.Month.ToString(),//.ToString("yyyy-MM")
                    g => new
                    {
                        OrderMonth = g.Key,
                        OrderCount = g.Count()
                    }
                )
                .Project(x => new BsonDocument
                {
                    { "OrderMonth", x.OrderMonth },
                    { "OrderCount", x.OrderCount }
                })
                .ToListAsync();

            return b1;
        }

        public static async Task<List<BsonDocument>> B1()
        {
            

            return null;
        }

    }
}
