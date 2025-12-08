import mongoose from "mongoose";
import Employee from "./models/employee.js";

async function run() {
    // Connect to MongoDB
    await mongoose.connect("mongodb://localhost:27017/mongoose_database");
    console.log("Connected to MongoDB.");

    // Clean existing data
    await Employee.deleteMany({});

    // Insert Joe Doe
    const joeDoe = await Employee.create({
      name: "Joe Doe",
      age: 45,
      manager: null,
      emails: ["asd@email.com", "asd@email.cz"],
      salary: 45000,
      address: {
        street: "Heatrow 1",
        city: "NYC",
      },
    });

    console.log("Joe Doe saved!");
  
    // Insert Jane Doe
    const janeDoe = await Employee.create({
      name: "Jane Doe",
      age: 31,
      manager: joeDoe._id,
      emails: ["jane@email.com", "jane2@email.cz"],
      salary: 41000,
      address: {
        street: "Dlouha 25",
        city: "Praha",
      },
    });

    console.log("Jane Doe saved!");
  
    // Query youngest employee
    const youngest = await Employee.findOne().sort({ age: 1 });
    console.log("Youngest employee:", youngest.name);

    // Query employees living in NYC
    const employeesInNYC = await Employee.find({ "address.city": "NYC" });

    employeesInNYC.forEach((e) => {
      console.log("Employees living in NYC:", e.name);
    });

  
    // Delete Joe & Jane
    await Employee.deleteMany({
      name: { $in: ["Joe Doe", "Jane Doe"] },
    });

    console.log("Joe Doe and Jane Doe deleted!");

    

    // Close connection
    await mongoose.disconnect();
}

run().catch((err) => console.error(err));
