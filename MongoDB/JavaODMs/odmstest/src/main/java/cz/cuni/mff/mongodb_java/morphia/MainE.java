package cz.cuni.mff.mongodb_java.morphia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import cz.cuni.mff.mongodb_java.morphia.models.Address;
import cz.cuni.mff.mongodb_java.morphia.models.Employee;
import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_embedded.NationE;
import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_embedded.RegionE;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

public class MainE {
    public static void main(String[] args){
        // 1. Create a MongoClient (connects to local MongoDB by default)
        MongoClient client = MongoClients.create("mongodb://localhost:27017");

        // 2. Configure Morphia
        MapperOptions options = MapperOptions.builder()
                .storeNulls(false)     // <-- THIS makes Morphia write null values
                .build();

        // 3. Create a Datastore instance
        Datastore datastore = Morphia.createDatastore(client, "morphia_database_tpch_embedded", options);

        // 4. Tell Morphia to discover your entity classes
        datastore.getMapper().mapPackage("cz.cuni.mff.mongodb_java.morphia.models.tpc_h_embedded");

        datastore.ensureIndexes();

        System.out.println("Morphia initialized!");

        // Insert Region
        /*final RegionE regionTest = new RegionE(
                1,
                "test name",
                "some comment",
                new ArrayList<>(Arrays.asList( new NationE(
                        1,
                        "nation name",
                        "nation comment"
                )))

        );
        datastore.save(regionTest);*/

        System.out.println("RegionTest saved!");
    }
}
