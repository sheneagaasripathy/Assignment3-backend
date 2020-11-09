package com.Assignment.demo.DAO;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Assignment.demo.Entity.ERole;
import com.Assignment.demo.Entity.Role;



@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
