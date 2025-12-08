package cz.cuni.mff.mongodb_java.morphia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import cz.cuni.mff.mongodb_java.morphia.models.Address;
import cz.cuni.mff.mongodb_java.morphia.models.Employee;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.morphia.query.filters.Filters.*;
import dev.morphia.query.Sort.*;
import dev.morphia.query.FindOptions;

import static dev.morphia.query.Sort.ascending;
import static dev.morphia.query.filters.Filters.*;
import static dev.morphia.query.Sort.*;
import static java.nio.file.Files.delete;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        // 1. Create a MongoClient (connects to local MongoDB by default)
        MongoClient client = MongoClients.create("mongodb://localhost:27017");

        // 2. Configure Morphia
        //MapperOptions options = MapperOptions.builder()
        //        .mapSubPackages(true)
        //        .build();

        // 3. Create a Datastore instance
        Datastore datastore = Morphia.createDatastore(client, "morphia_database");

        // 4. Tell Morphia to discover your entity classes
        datastore.getMapper().mapPackage("cz.cuni.mff.mongodb_java.morphia.models");

        datastore.ensureIndexes();

        System.out.println("Morphia initialized!");

        // Insert
        final Employee joeDoe = new Employee(    "Joe Doe",
                                                45,
                                                null,
                                                new ArrayList<String>( Arrays.asList("asd@email.com", "asd@email.cz")), 45000.0,
                                                new Address("Heatrow 1", "NYC"));
        datastore.save(joeDoe);

        System.out.println("Joe Doe saved!");

        final Employee janeDoe = new Employee(    "Jane Doe",
                31,
                joeDoe,
                new ArrayList<String>( Arrays.asList("jane@email.com", "jane2@email.cz")), 41000.0,
                new Address("Dlouha 25", "Praha"));
        datastore.save(janeDoe);

        System.out.println("Jane Doe saved!");

        // Query
        // Find the youngest employee
        Employee youngestEmployee = datastore.find(Employee.class, new FindOptions()
                        .sort(ascending("age")))
                        .first();
        System.out.println("Youngest employee:" + youngestEmployee.getName());

        // Find all employees living in NYC
        List<Employee> employeesInPrague = datastore.find(Employee.class)
                .filter(eq("address.city", "NYC"))
                .iterator()
                .toList();

        for (Employee employee : employeesInPrague) {
            System.out.println( "Employees living in NYC:" + employee.getName());
        }

        // Delete
        datastore.find(Employee.class)
                .filter(in("name", Arrays.asList("Joe Doe","Jane Doe")))
                .delete(new DeleteOptions()
                        .multi(true));

        System.out.println("Joe Doe and Jane Doe deleted!");
    }
}