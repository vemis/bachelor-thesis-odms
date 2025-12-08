package cz.cuni.mff.mongodb_java.morphia.models;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Entity("employees")
public class Employee {
    @Id
    private ObjectId id;
    private String name;
    private int age;
    @Reference
    private Employee manager;
    private ArrayList<String> emails;
    private Double salary;
    private Address address;

    // Morphia needs this no-arg constructor
    public Employee() {}

    public Employee(String name, int age, Employee manager, ArrayList<String> emails, Double salary,  Address address) {
        this.name = name;
        this.age = age;
        this.manager = manager;
        this.emails = emails;
        this.salary = salary;
        this.address = address;
    }

    public String getName() { return name; }
}
