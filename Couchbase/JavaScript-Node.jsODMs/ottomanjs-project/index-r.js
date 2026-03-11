// this stupid import is the only way how I was able to use the Ottoman.js
import {} from './config/db-r.js';

import {RegionR} from "./models/tpc_h_r/region-r.js";

async function run() {
    const reg1 = new RegionR({
        id: "1",
        r_name: "test",
        r_comment: "test"
    });

    await reg1.save();
    console.log("reg1 saved");
}

run().catch(err => console.error(err));