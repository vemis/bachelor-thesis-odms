package cz.cuni.mff.couchbase_java.springdata;

import cz.cuni.mff.couchbase_java.springdata.models.Address;
import cz.cuni.mff.couchbase_java.springdata.models.Employee;
import cz.cuni.mff.couchbase_java.springdata.repositories.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Configuration
public class CouchbaseSpringDataLogicCommandRunner {
    @Bean
    CommandLineRunner run(EmployeeRepository repo) {
        return args -> {

            repo.deleteAll();
            System.out.println("Delete all employees, starting with clean repo");

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

            repo.save(joeDoe);
            System.out.println("Joe saved!");

            // Create Jane
            Employee janeDoe = new Employee(
                    UUID.randomUUID().toString(),
                    "Jane Doe",
                    31,
                    joeDoe.getId(),
                    Arrays.asList("jane@email.com", "jane2@email.cz"),
                    41000.0,
                    new Address("Dlouha 25", "Praha")
            );

            repo.save(janeDoe);
            System.out.println("Jane saved!");

            // Youngest employee
            Employee youngest = repo.findTopByOrderByAgeAsc();
            System.out.println("Youngest employee: " + youngest.getName());

            // Employees in NYC
            List<Employee> inNY = repo.findByAddress_City("NYC");
            inNY.forEach(e ->
                    System.out.println("Employees living in NYC: " + e.getName())
            );


            // Delete
            repo.deleteAll(
                    repo.findByNameIn(Arrays.asList("Joe Doe", "Jane Doe"))
            );

            System.out.println("All Doe employees deleted!");
        };
    }

}