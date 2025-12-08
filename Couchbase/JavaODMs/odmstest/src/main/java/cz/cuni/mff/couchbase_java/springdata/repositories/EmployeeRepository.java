package cz.cuni.mff.couchbase_java.springdata.repositories;

import cz.cuni.mff.couchbase_java.springdata.models.Employee;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CouchbaseRepository<Employee, String> {
    List<Employee> findByAddress_City(String city);

    List<Employee> findByNameIn(List<String> names);

    Employee findTopByOrderByAgeAsc();
}
