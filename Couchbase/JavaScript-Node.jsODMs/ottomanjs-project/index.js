// this stupid import is the only way how I was able to use the Ottoman.js
import {} from './models/db.js';

import { Employee } from './models/employee.js';


async function run() {
    // INSERT Joe Doe
    console.log(Employee);
    const joeDoe = new Employee({
        name: "Joe Doe",
        age: 45,
        manager: null,
        emails: ["asd@email.com", "asd@email.cz"],
        salary: 45000,
        address: { street: "Heatrow 1", city: "NYC" },
    });

    await joeDoe.save();
    console.log("Joe Doe saved!");
  
    // INSERT Jane Doe
    const janeDoe = new Employee({
        name: "Jane Doe",
        age: 31,
        manager: joeDoe,
        emails: ["jane@email.com", "jane2@email.cz"],
        salary: 41000,
        address: { street: "Dlouha 25", city: "Praha" },
    });

    await janeDoe.save();
    console.log("Jane Doe saved!");

    // QUERY: Youngest employee
    const youngest = await Employee.find({}, { sort: { age: "ASC" }, limit: 1 });
    console.log("Youngest employee:", youngest.rows[0].name);

    // QUERY: Employees living in NYC
    const employeesInNYC = await Employee.find({
    'address.city': 'NYC'
    });

    for (const row of employeesInNYC.rows) {
        console.log("Employees living in NYC:", row.name);
    }

    // DELETE Joe & Jane
        await Employee.removeMany({
            name: { $in: ["Joe Doe", "Jane Doe"] }
        });
    console.log("Joe Doe and Jane Doe deleted!");
}

run().catch(err => console.error(err));