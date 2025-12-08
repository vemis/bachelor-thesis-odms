using MongoDB.Entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace MongoDBEntities.Models
{
    internal class Employee : Entity
    {
        public string Name { get; set; }
        public int Age { get; set; }
        public One<Employee> Manager { get; set; }
        public List<string> Emails { get; set; }
        public double Salary { get; set; }
        public Address Address { get; set; }

        public string GetName => Name;
    }
}
