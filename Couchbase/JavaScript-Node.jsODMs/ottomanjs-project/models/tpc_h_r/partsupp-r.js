import { Schema, model } from 'ottoman';



const PartsuppRSchema = new Schema({
        
    //@Id private String ps_id;
    id: String,
    
    ps_partKey: String,
    ps_suppKey: String,

    ps_availqty: Number,
    ps_supplycost: Number,
    ps_comment: String
});

PartsuppRSchema.index.findBy_ps_partKey = {
    by: "ps_partKey",
    type: 'n1ql',
};

PartsuppRSchema.index.findBy_ps_suppKey = {
    by: "ps_suppKey",
    type: 'n1ql',
};

export const PartsuppR = model("PartsuppR", PartsuppRSchema,
    {
        idKey: "id",
        scopeName:'ottoman_scope_r',
        collectionName:'PartsuppR',
        keyGenerator: ({ metadata }) => "",
        keyGeneratorDelimiter: ""
    });
