using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Entities;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace MongoDBEntities.Models.TPC_H
{
    public class TestR : Entity
    {
        
        public BsonValue r_indexed;
        public string r_comment;
    }
}
