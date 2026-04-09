package cz.cuni.mff.mongodb_java.springdata_e;

import cz.cuni.mff.mongodb_java.springdata_e.benchmarks.QueriesSpringDataE;
import cz.cuni.mff.mongodb_java.springdata_e.service.LogicServiceE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class LogicCommandRunnerE {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    CommandLineRunner run(LogicServiceE service) {
        return args -> {


            var res = QueriesSpringDataE.R1(mongoTemplate);
            System.out.println(res.get(0));
            System.out.println(res.size());

/*
            var lineitemsE = TPCHDatasetLoaderSpringDataE.createLineitemsE("..\\..\\..\\dataset\\TPC-H\\tpch-data\\lineitem.tbl");
            System.out.println(lineitemsE.size());

            TPCHDatasetLoaderSpringDataE.loadOrdersEWithLineitems("..\\..\\..\\dataset\\TPC-H\\tpch-data\\orders.tbl", lineitemsE, mongoTemplate );
            System.out.println("OrdersEWithLineitems loaded");

            // Create RegionsE
            var ordersE = TPCHDatasetLoaderSpringDataE.loadOrders("..\\..\\..\\dataset\\TPC-H\\tpch-data\\orders.tbl");
            System.out.println("OrdersE created");

            // Create Customers
            TPCHDatasetLoaderSpringDataE.loadCustomers("..\\..\\..\\dataset\\TPC-H\\tpch-data\\customer.tbl", ordersE ,mongoTemplate);
            System.out.println("CustomersE inserted");
*/



        };
    }
}
