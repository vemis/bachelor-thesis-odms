import mongoose from "mongoose";
import AddressSchema from "./address.js";

const EmployeeSchema = new mongoose.Schema({
    name: String,
    age: Number,//mongoose.Schema.Types.Mixed
    manager: { type: mongoose.Schema.Types.ObjectId, ref: "Employee" },
    emails: [String],
    salary: Number,
    address: AddressSchema,
});

export default mongoose.model("Employee", EmployeeSchema);