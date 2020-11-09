package com.Assignment.demo.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.Assignment.demo.Entity.User;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  
  
  List<User> findByRolesName(String name);
  
  @Query("{$or: [ { 'username': { $regex: ?0 , $options: 'i' } }, { 'email':{ $regex: ?0, $options: 'i' } },{ 'phoneNum': { $regex: ?0 , $options: 'i' } },{ 'address': { $regex: ?0 , $options: 'i' } } ]}")
  List<User> findBySearchContaining(String searched);
  
  @Query("{ 'email' : ?0 }")
  Optional<User>findByEmail(String email);
}