package cz.cuni.mff.couchbase_java.springdata_e;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import cz.cuni.mff.couchbase_java.SpringDataCouchbaseClusterManagement;
import cz.cuni.mff.couchbase_java.springdata_e.benchmarks.QueriesSpringDataE;
import cz.cuni.mff.couchbase_java.springdata_e.models.CustomerEWithOrders;
import cz.cuni.mff.couchbase_java.springdata_e.models.OrdersE;
import cz.cuni.mff.couchbase_java.springdata_e.service.LogicServiceE;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import cz.cuni.mff.couchbase_java.springdata_e.TPCHDatasetLoaderSpringDataE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class CouchbaseSpringDataLogicCommandRunnerE {
    //@Autowired
    //private CouchbaseTemplate couchbaseTemplate;

    @Bean
    CommandLineRunner run(LogicServiceE service,
                          CouchbaseTemplate couchbaseTemplate,
                          Cluster cluster,
                          ReactiveCouchbaseTemplate  reactiveCouchbaseTemplate) {
        return args -> {

            Bucket bucket = cluster.bucket("spring_bucket_e");

            // Create Scope
            SpringDataCouchbaseClusterManagement.createScope("spring_scope_e", bucket);

            // Create Collections
            SpringDataCouchbaseClusterManagement.createCollection("spring_scope_e", "CustomerEWithOrders", bucket);

            // Create Indexes
            //SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_e", "spring_scope_e", "CustomerEWithOrders", "s_nationkey");
            // Embedded Index
            //SpringDataCouchbaseClusterManagement.createIndex(cluster, "spring_bucket_e", "spring_scope_e", "CustomerEWithOrders",
            //        "c_orders.");

            CustomerEWithOrders.createIndexes(cluster);

            /*
            var orde1 = new OrdersE(1,
                    1,
                    "test",
                    "test",
                    LocalDate.now(),
                    "test",
                    "test",
                    "test",
                    "test"
            );

            var cuse1 = new CustomerEWithOrders(1,
                    "test",
                    "test",
                    1,
                    "test",
                    1.0,
                    "test",
                    "test",
                    Arrays.asList(orde1)
            );

            couchbaseTemplate.save(cuse1);
            */

            /*System.out.println("Creating ordersE");
            var orderse = TPCHDatasetLoaderSpringDataE.createOrders("..\\..\\..\\dataset\\TPC-H\\tpch-data\\orders.tbl", reactiveCouchbaseTemplate);
            System.out.println("OrdersE created");

            System.out.println("Creating CustomerEWithOrders");
            TPCHDatasetLoaderSpringDataE.loadCustomers("..\\..\\..\\dataset\\TPC-H\\tpch-data\\customer.tbl", orderse ,reactiveCouchbaseTemplate);
            System.out.println("CustomerEWithOrders created");*/

            var c2 = QueriesSpringDataE.C2(cluster);
            System.out.println(c2.get(0));
            System.out.println(c2.size());

        };
    }
}
