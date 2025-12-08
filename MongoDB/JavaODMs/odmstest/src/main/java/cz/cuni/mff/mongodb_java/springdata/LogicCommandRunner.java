package cz.cuni.mff.mongodb_java.springdata;

import cz.cuni.mff.mongodb_java.springdata.models.Address;
import cz.cuni.mff.mongodb_java.springdata.models.Employee;
import cz.cuni.mff.mongodb_java.springdata.repositories.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class LogicCommandRunner {
    @Bean
    CommandLineRunner run(EmployeeRepository repo) {
        return args -> {
            // clean start
            repo.deleteAll();

            // Insert
            Employee joeDoe = new Employee(
                    "Joe Doe",
                    45,
                    null,
                    Arrays.asList("asd@email.com", "asd@email.cz"),
                    45000.0,
                    new Address("Heatrow 1", "NYC")
            );
            repo.save(joeDoe);
            System.out.println("Joe Doe saved!");


            // Insert Jane Doe
            Employee janeDoe = new Employee(
                    "Jane Doe",
                    31,
                    joeDoe,
                    Arrays.asList("jane@email.com", "jane2@email.cz"),
                    41000.0,
                    new Address("Dlouha 25", "Praha")
            );
            repo.save(janeDoe);
            System.out.println("Jane Doe saved!");

            // Query: find youngest
            Employee youngest = repo.findTopByOrderByAgeAsc();
            System.out.println("Youngest employee: " + youngest.getName());

            // Find all living in NYC
            List<Employee> inNYC = repo.findByAddress_City("NYC");
            for (Employee emp : inNYC) {
                System.out.println("Employee living in NYC: " + emp.getName());
            };

            // Delete Joe + Jane
            repo.deleteAll(repo.findByNameIn(Arrays.asList("Joe Doe", "Jane Doe")));
            System.out.println("Joe Doe and Jane Doe deleted!");
        };
    }
}
