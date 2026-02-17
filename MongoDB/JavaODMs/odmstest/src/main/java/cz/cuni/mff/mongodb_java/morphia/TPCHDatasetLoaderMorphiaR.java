package cz.cuni.mff.mongodb_java.morphia;

import cz.cuni.mff.mongodb_java.TPCHDatasetLoader;
import cz.cuni.mff.mongodb_java.morphia.models.tpc_h_relational.RegionR;
import dev.morphia.Datastore;

import java.util.List;

public class TPCHDatasetLoaderMorphiaR extends TPCHDatasetLoader {

    public static void loadRegions(String filePath, Datastore datastore) {

        List<String[]> regions = readDataFromCustomSeparator(filePath);

        for (String[] row : regions) {
            RegionR region = new RegionR(
                    Integer.parseInt(row[0]),
                    row[1],
                    row[2]
            );

            datastore.save(region);
        }
    }
}
