package cz.cuni.mff.couchbase_java.springdata_r.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;
import org.springframework.stereotype.Indexed;


@Document
@Scope("spring_scope_r")
@Collection("NationR")
public class NationR {
    @Id
    private int n_nationkey;

    private String n_name;

    private int n_regionkey; //foreign key

    private String n_comment;

    public NationR() {}

    public NationR(int n_nationkey, String n_name, int n_regionkey, String n_comment) {
        this.n_nationkey = n_nationkey;
        this.n_name = n_name;
        this.n_regionkey = n_regionkey;
        this.n_comment = n_comment;
    }
}
