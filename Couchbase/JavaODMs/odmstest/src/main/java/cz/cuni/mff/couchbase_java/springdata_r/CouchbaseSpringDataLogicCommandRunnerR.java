package cz.cuni.mff.couchbase_java.springdata_r;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import cz.cuni.mff.couchbase_java.SpringDataCouchbaseClusterManagement;
import cz.cuni.mff.couchbase_java.springdata.models.Address;
import cz.cuni.mff.couchbase_java.springdata.models.Employee;
import cz.cuni.mff.couchbase_java.springdata_r.models.NationR;
import cz.cuni.mff.couchbase_java.springdata_r.models.RegionR;
import cz.cuni.mff.couchbase_java.springdata_r.service.LogicServiceR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.Arrays;
import java.util.UUID;

@Configuration
public class CouchbaseSpringDataLogicCommandRunnerR {
    //@Autowired
    //private CouchbaseTemplate couchbaseTemplate;

    @Bean
    CommandLineRunner run(LogicServiceR service, CouchbaseTemplate couchbaseTemplate, Cluster cluster) {
        return args -> {

            Bucket bucket = cluster.bucket("spring_bucket_r");

            // Create Scope
            SpringDataCouchbaseClusterManagement.createScope("spring_scope_r", bucket);

            // Create Collections
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "RegionR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "NationR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "CustomerR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "OrdersR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "LineitemR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "PartsuppR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "SupplierR", bucket);
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_r", "PartR", bucket);


            // Create Indexes
            SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_r", "spring_scope_r", "SupplierR", "s_nationkey");

            SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_r", "spring_scope_r", "PartsuppR", "ps_suppKey");
            SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_r", "spring_scope_r", "PartsuppR", "ps_partKey");

            SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_r", "spring_scope_r", "OrdersR", "o_custkey");

            SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_r", "spring_scope_r", "CustomerR", "c_nationkey");


            // Insert Regions
            TPCHDatasetLoaderSpringDataR.loadRegions("..\\..\\..\\dataset\\TPC-H\\tpch-data\\region.tbl", couchbaseTemplate);
            System.out.println("RegionRs saved!");

            /*
            // Insert Nations
            TPCHDatasetLoaderSpringDataR.loadNations("..\\..\\..\\dataset\\TPC-H\\tpch-data\\nation.tbl", mongoTemplate);
            System.out.println("NationRs saved!");

            // Insert Customers
            // Slow
            TPCHDatasetLoaderSpringDataR.loadCustomers("..\\..\\..\\dataset\\TPC-H\\tpch-data\\customer.tbl", mongoTemplate);
            System.out.println("CustomerRs saved!");



            // Insert Orders
            TPCHDatasetLoaderSpringDataR.loadOrders("..\\..\\..\\dataset\\TPC-H\\tpch-data\\orders.tbl", mongoTemplate);
            System.out.println("OrderRs saved!");


            // Insert Lineitems
            TPCHDatasetLoaderSpringDataR.loadLineitems("..\\..\\..\\dataset\\TPC-H\\tpch-data\\lineitem.tbl", mongoTemplate);
            System.out.println("LineitemRs saved!");


            // Insert Partsupp
            TPCHDatasetLoaderSpringDataR.loadPartsupps("..\\..\\..\\dataset\\TPC-H\\tpch-data\\partsupp.tbl", mongoTemplate);
            System.out.println("partsuppRs saved!");

            // Insert Part
            TPCHDatasetLoaderSpringDataR.loadParts("..\\..\\..\\dataset\\TPC-H\\tpch-data\\part.tbl", mongoTemplate);
            System.out.println("partRs saved!");

            // Insert Suppliers
            TPCHDatasetLoaderSpringDataR.loadSuppliers("..\\..\\..\\dataset\\TPC-H\\tpch-data\\supplier.tbl", mongoTemplate);
            System.out.println("supplierRs saved!");
*/

        };
    }
}
