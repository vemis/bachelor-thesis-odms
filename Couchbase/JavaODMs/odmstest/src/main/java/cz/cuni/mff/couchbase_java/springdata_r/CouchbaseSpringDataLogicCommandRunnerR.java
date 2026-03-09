package cz.cuni.mff.couchbase_java.springdata_r;

import cz.cuni.mff.couchbase_java.springdata.models.Address;
import cz.cuni.mff.couchbase_java.springdata.models.Employee;
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
    CommandLineRunner run(LogicServiceR service, CouchbaseTemplate couchbaseTemplate) {
        return args -> {
            // Create Joe
            Employee joeDoe = new Employee(
                    UUID.randomUUID().toString(),
                    "Joe Doe",
                    45,
                    null,
                    Arrays.asList("asd@email.com", "asd@email.cz"),
                    45000.0,
                    new Address("Heatrow 1", "NYC")
            );

            couchbaseTemplate.save(joeDoe);
            System.out.println("Joe saved!");
        };
    }
}
