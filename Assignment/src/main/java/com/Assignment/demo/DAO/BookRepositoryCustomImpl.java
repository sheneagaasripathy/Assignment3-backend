package com.Assignment.demo.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.Assignment.demo.Entity.Books;



@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

	@Autowired
    MongoTemplate mongoTemplate;
	
	@Override
	public long getMaxBookID() {
		Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.limit(1);
        
        Books maxObject = mongoTemplate.findOne(query, Books.class);
        if (maxObject == null) {
            return 0;
        }
        return maxObject.getId();
	}

	
}

