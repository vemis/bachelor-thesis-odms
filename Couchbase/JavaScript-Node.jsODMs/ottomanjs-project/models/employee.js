import { Schema, model } from 'ottoman';
import AddressSchema from './address.js';

const EmployeeSchema = new Schema({
    name: String,
    age: Number,
    manager: { type: Schema.Types.Reference, ref: 'Employee' },
    emails: [String],
    salary: Number,
    address: AddressSchema,
});

export const Employee = model('Employee', EmployeeSchema);

