package cz.cuni.mff.mongodb_java.morphia.models.tpc_h_embedded;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Id;

import java.util.ArrayList;

@Embedded
public class NationE {
    @Id
    private int n_nationkey;

    private String n_name;
    private String n_comment;

    private ArrayList<Integer> customers;
    private ArrayList<Integer> suppliers;


    public NationE() {}

    public NationE(int n_nationkey, String n_name, String n_comment,
                   ArrayList<Integer> customers,
                   ArrayList<Integer> suppliers) {
        this.n_nationkey = n_nationkey;
        this.n_name = n_name;
        this.n_comment = n_comment;
        this.customers = customers;
        this.suppliers = suppliers;
    }
}
