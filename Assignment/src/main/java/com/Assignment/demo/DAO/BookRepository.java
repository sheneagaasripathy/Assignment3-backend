package com.Assignment.demo.DAO;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.Assignment.demo.Entity.Books;


@Repository
public interface BookRepository extends MongoRepository<Books, Long>{
	
	
	@Query("{$or: [ { 'name': { $regex: ?0 , $options: 'i' } }, { 'authorName':{ $regex: ?0, $options: 'i' } },{ 'description': { $regex: ?0 , $options: 'i' } },{ 'category': { $regex: ?0 , $options: 'i' } },{ 'usage': { $regex: ?0 , $options: 'i' } } ]}")
	List<Books> findBySearchContaining(String searchString);

//	@Query("{$and: [{'usage' : { $regex: ?0}},{'category':{$regex:?1}},{'price':{$gt:?2,$lt:?3}}]}")
//	List<Books> findByFilter(String usage ,String category,int minPrice, int maxPrice);
}
