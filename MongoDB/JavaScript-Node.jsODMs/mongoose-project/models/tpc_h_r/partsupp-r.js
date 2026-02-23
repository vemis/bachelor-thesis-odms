import mongoose from "mongoose";
mongoose.pluralize(null)


const PartsuppRSchema = new mongoose.Schema({
        
    //@Id private String ps_id;
    _id: String,
    
    ps_partKey:{
        type:Number,
        index:true
    },
    ps_suppKey:{
        type:Number,
        index:true
    },

    ps_availqty: Number,
    ps_supplycost: Number,
    ps_comment: String
});

export default mongoose.model("PartsuppR", PartsuppRSchema);
