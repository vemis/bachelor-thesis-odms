using MongoDB.Driver;
using MongoDB.Entities;
using MongoDBEntities.Models;
using System;
using System.Threading.Tasks;


namespace MongoDBEntities
{


    class Program
    {

        // Main Method
        public static async Task Main(String[] args)
        {
            // Connect to MongoDB
            Console.WriteLine("Connecting to the database:");

            await DB.InitAsync("mongodbentities_database", "localhost", 27017);

            Console.WriteLine("MongoDB.Entities initialized!");

            // Insert
            Employee joeDoe = new Employee
            {
                Name = "Joe Doe",
                Age = 45,
                Emails = new List<string> { "asd@email.com", "asd@email.cz" },
                Salary = 45000,
                Address = new Address
                {
                    Street = "Heatrow 1",
                    City = "NYC"
                }
            };

            await joeDoe.SaveAsync();
            Console.WriteLine("Joe Doe saved!");

            Employee janeDoe = new Employee
            {
                Name = "Jane Doe",
                Age = 31,
                Manager = joeDoe.ToReference(),   // reference
                Emails = new List<string> { "jane@email.com", "jane2@email.cz" },
                Salary = 41000,
                Address = new Address
                {
                    Street = "Dlouha 25",
                    City = "Praha"
                }
            };

            await janeDoe.SaveAsync();
            Console.WriteLine("Jane Doe saved!");

            // Query

            // Youngest employee
            Employee youngest = await DB.Find<Employee>()
                                   .Sort(e => e.Ascending(x => x.Age))
                                   .ExecuteFirstAsync();

            Console.WriteLine("Youngest employee: " + youngest.GetName);

            // Employee living in the NYC
            var employeesInNYC = await DB.Find<Employee>()
                                         .Match(e => e.Address.City == "NYC")
                                         .ExecuteAsync();

            foreach (var emp in employeesInNYC)
            {
                Console.WriteLine("Employees living in NYC: " + emp.Name);
            }

            // Delete employees by name (multi-delete)


            string[] toDeleteEmployees = { "Joe Doe", "Jane Doe" };
            var filter = Builders<Employee>.Filter.In(e => e.Name, toDeleteEmployees);

            await DB.Collection<Employee>().DeleteManyAsync(filter);

            Console.WriteLine("Joe Doe and Jane Doe deleted!");
        }
    }
}
