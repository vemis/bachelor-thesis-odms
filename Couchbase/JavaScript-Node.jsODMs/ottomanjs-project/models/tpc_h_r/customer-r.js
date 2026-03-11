import { Schema, model } from 'ottoman';


const CustomerRSchema = new Schema({
    //c_custkey
    _id: Number,
    c_name: String,
    c_address: String,

    c_nationkey: {
        type: Number,
        index: true
    },
    c_phone: String,
    c_acctbal: Number,
    c_mktsegment: String,
    c_commen: String
});

export const CustomerR =  model("CustomerR", CustomerRSchema,
    {collectionName: 'CustomerR', scopeName: 'ottoman_scope_r'});
