package com.nasif.jounalApp.repository;

import com.nasif.jounalApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }

    public boolean isAdmin(User user){
        Query query =  new Query();
        query.addCriteria(Criteria.where("userName").is(user.getUserName()).and("roles").in("ADMIN"));
        boolean exists = mongoTemplate.exists(query, User.class);
        if(exists){
            log.info("User is ADMIN");
            return true;
        }
        else{
            log.info("User is not ADMIN");
            return false;
        }
    }
}
