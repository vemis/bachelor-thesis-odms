using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Entities;
using MongoDBEntities.Models.TPC_H;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;

namespace MongoDBEntities
{
    class Book : IEntity
    {
        [BsonId]
        public string Title { get; set; }

        public object GenerateNewID()
        {
            throw new NotImplementedException();
        }

        public bool HasDefaultID()
        {
            return false;
        }

    }

    class ProgramR
    {
        // MainTesting Method
        public static async Task Main(String[] args)
        {


            CultureInfo.DefaultThreadCurrentCulture = CultureInfo.InvariantCulture;
            CultureInfo.DefaultThreadCurrentUICulture = CultureInfo.InvariantCulture;

            
            // Connect to MongoDB
            Console.WriteLine("Connecting to the database:");

            await DB.InitAsync("mongodbentities_database_r", "localhost", 27017);

            Console.WriteLine("MongoDB.Entities initialized!");

            var lineitems = new[]
            {
                new LineitemR{ l_id = "1" },
                new LineitemR{ l_id = "2" },
                new LineitemR{ l_id = "3" }
            };

            await DB.SaveAsync(lineitems);


            /*await DB.Index<TestR>()
                .Key(b => b.r_indexed, KeyType.Ascending)
                .CreateAsync();
            */
            /*
                        // LineitemsR indexes
                        await DB.Index<LineitemR>()
                            .Key(c => c.l_orderkey, KeyType.Ascending)
                            .Key(c => c.l_partkey, KeyType.Ascending)
                            .Key(c => c.l_suppkey, KeyType.Ascending)
                            .Key(c => c.l_ps_id, KeyType.Ascending)
                            .CreateAsync();


                        TPCHDatasetLoader.LoadDatasetAsync("..\\..\\..\\..\\..\\..\\..\\dataset\\TPC-H\\tpch-data\\lineitem.tbl");
            */

            /*var reg = TPCHDatasetLoader.ReadDataFromCustomSeparator("..\\..\\..\\..\\..\\..\\..\\dataset\\TPC-H\\tpch-data\\region.tbl");
            foreach (var arr in reg) { 
                Console.WriteLine($"[{string.Join(',', arr)}]");
            }*/


        }
    }
}
