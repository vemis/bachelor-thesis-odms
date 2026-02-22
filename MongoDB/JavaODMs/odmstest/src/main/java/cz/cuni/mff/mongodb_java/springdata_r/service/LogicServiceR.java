package cz.cuni.mff.mongodb_java.springdata_r.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogicServiceR {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public LogicServiceR(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
