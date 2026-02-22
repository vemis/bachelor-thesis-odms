package cz.cuni.mff.mongodb_java.springdata_r.service;

import cz.cuni.mff.mongodb_java.springdata_r.benchmarks.QueriesSpringDataR;
import cz.cuni.mff.mongodb_java.springdata_r.model.LineitemR;
import cz.cuni.mff.mongodb_java.springdata_r.model.OrdersR;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogicServiceR {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public LogicServiceR(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public List<LineitemR> A1(){
        return QueriesSpringDataR.A1(mongoTemplate);
    }

    public List<OrdersR> A2(){
        return QueriesSpringDataR.A2(mongoTemplate);
    }

    public List<Document> B1(){
        return QueriesSpringDataR.B1(mongoTemplate);
    }

    public  List<Document> C2(){
        return QueriesSpringDataR.C2(mongoTemplate);
    }

}
