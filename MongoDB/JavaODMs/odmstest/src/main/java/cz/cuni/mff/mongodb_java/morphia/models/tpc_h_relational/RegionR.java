package cz.cuni.mff.mongodb_java.morphia.models.tpc_h_relational;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;

@Entity
public class RegionR {
    @Id
    //private ObjectId r_regionkey;
    private int r_regionkey;

    private String r_name;
    private String r_comment;

    // Morphia needs this no-arg constructor
    public RegionR() {}

    public RegionR(int r_regionkey, String r_name, String r_comment) {
        this.r_regionkey = r_regionkey;
        this.r_name = r_name;
        this.r_comment = r_comment;
    }
}
