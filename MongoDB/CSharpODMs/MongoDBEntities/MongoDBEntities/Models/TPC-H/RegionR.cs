using MongoDB.Entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace MongoDBEntities.Models.TPC_H
{
    public class RegionR : Entity
    {
       
        public int r_regionkey;
        
        public string r_name;
        public string r_comment;

        public RegionR(int r_regionkey, string r_name, string r_comment)
        {
            this.r_regionkey = r_regionkey;
            this.r_name = r_name;
            this.r_comment = r_comment;
        }
    }
}
