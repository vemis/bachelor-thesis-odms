package cz.cuni.mff.couchbase_java.springdata.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.List;

@Document
public class Employee {
    @Id
    private String id;

    private String name;
    private int age;

    // Couchbase does not use @DBRef — CRDT references must be manual
    private String managerId;

    private List<String> emails;
    private Double salary;

    @Field
    private Address address;

    public Employee(String id, String name, int age, String managerId, List<String> emails, Double salary, Address address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.managerId = managerId;
        this.emails = emails;
        this.salary = salary;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
