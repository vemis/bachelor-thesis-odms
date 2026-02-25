using MongoDB.Entities;
using MongoDBEntities.Models.TPC_H;
using System;
using System.Collections.Generic;
using System.Text;

namespace MongoDBEntities
{
    class ProgramR
    {
        // MainTesting Method
        public static async Task Main(String[] args)
        {
            
            // Connect to MongoDB
            Console.WriteLine("Connecting to the database:");

            await DB.InitAsync("mongodbentities_database_r", "localhost", 27017);

            Console.WriteLine("MongoDB.Entities initialized!");

            /*await DB.Index<TestR>()
                .Key(b => b.r_indexed, KeyType.Ascending)
                .CreateAsync();
            */

            /*var reg = TPCHDatasetLoader.readDataFromCustomSeparator("..\\..\\..\\..\\..\\..\\..\\dataset\\TPC-H\\tpch-data\\region.tbl");
            foreach (var arr in reg) { 
                Console.WriteLine($"[{string.Join(',', arr)}]");
            }*/


        }
    }
}
