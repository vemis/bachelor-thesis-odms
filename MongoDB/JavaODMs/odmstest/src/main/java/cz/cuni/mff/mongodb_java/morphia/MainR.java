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

public class MainR {
    public static void main(String[] args){
        // 1. Create a MongoClient (connects to local MongoDB by default)
        MongoClient client = MongoClients.create("mongodb://localhost:27017");

        // 2. Configure Morphia
        MapperOptions options = MapperOptions.builder()
                .storeNulls(false)     // <-- THIS makes Morphia write null values
                .build();

        // 3. Create a Datastore instance
        Datastore datastore = Morphia.createDatastore(client, "morphia_database_tpch_relational", options);

        // 4. Tell Morphia to discover your entity classes
        datastore.getMapper().mapPackage("cz.cuni.mff.mongodb_java.morphia.models.tpc_h_relational");

        datastore.ensureIndexes();

        System.out.println("Morphia initialized!");

        // Insert Region
        TPCHDatasetLoaderMorphiaR.loadRegions("..\\..\\..\\dataset\\TPC-H\\tpch-data\\region.tbl", datastore);

        System.out.println("RegionRs saved!");
    }
}
