package com.Assignment.demo.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Assignment.demo.Config.BasicAuthUtils;
import com.Assignment.demo.DAO.RoleRepository;
import com.Assignment.demo.DAO.UserRepository;
import com.Assignment.demo.Entity.ERole;
import com.Assignment.demo.Entity.Role;
import com.Assignment.demo.Entity.User;
import com.Assignment.demo.Entity.request.SignupRequest;
import com.Assignment.demo.Entity.resposne.MessageResponse;



@Service
public class UserService {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BasicAuthUtils basicUtils;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;

	public ResponseEntity<MessageResponse> updateUser(String id, SignupRequest user) {
		try {
			Optional<User> oldUser =  userRepository.findById(id);
			if(oldUser.isPresent()) {
				User newUser = oldUser.get();
				Set<String> strRoles = user.getUpdateroles();
				Set<Role> roles = new HashSet<>();
				if (strRoles == null) {
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				} else {
					strRoles.forEach(role -> {
						switch (role) {
						case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
	
						break;
						case "user":
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
	
						break;
						default:
						Role userRoles = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRoles);
						}
					});
				}
				if(user.getEmail().equals(newUser.getEmail())) {
					newUser.setAddress(user.getAddress());
					newUser.setRoles(roles);
					userRepository.save(newUser);
					return ResponseEntity.ok(new MessageResponse("User Update Sucessfully!"));
					
				}else if (userRepository.existsByEmail(user.getEmail())) {
					return ResponseEntity
							.badRequest()
							.body(new MessageResponse("Error: Email is already in use!"));
				}
				newUser.setEmail(user.getEmail());
				newUser.setAddress(user.getAddress());

				
				newUser.setRoles(roles);
				userRepository.save(newUser);
				return ResponseEntity.ok(new MessageResponse("User Update Sucessfully!"));
			}
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User Not Found"));
		}catch(Exception e) {
			return ResponseEntity.ok(new MessageResponse("Error: User Update Unscessfully!"));
		}
	}

	public ResponseEntity<User> updatePassword(String id, String oldPassword, String newPassword) {
		
		Optional<User> newUser = userRepository.findById(id);
		if(newUser.isPresent()) {
			User oldUser = newUser.get();
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(oldUser.getUsername(), oldPassword));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			oldUser.setPassword(encoder.encode(newPassword));
			oldUser.setPasswordChangeToken(basicUtils.generateBasicToken(oldUser.getUsername(),newPassword));
			userRepository.save(oldUser);
			return new ResponseEntity<>(oldUser,HttpStatus.OK);
			//String basicToken = basicUtils.generateBasicToken(oldUser.getUsername(), oldPassword);
		}
		return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Map<String, Object>> getAllusers(int pageNo, int pageSize) {
		try {
            Map<String, Object> response = new HashMap<>();
            Sort sort = Sort.by(Sort.Direction.DESC,"id");
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            Page<User> page = userRepository.findAll(pageable);
            response.put("data", page.getContent());
            response.put("Total_No_Of_Pages", page.getTotalPages());
            response.put("Total_No_Of_Elements", page.getTotalElements());
            response.put("Current page no", page.getNumber());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

	public ResponseEntity<User> deleteUserById(String id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if(user.isPresent()) {
				userRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

	public ResponseEntity<MessageResponse> updateUserByUser(String id, SignupRequest user) {
		try {
			Optional<User> oldUser =  userRepository.findById(id);
			if(oldUser.isPresent()) {
				User newUser = oldUser.get();
				System.out.println(user.getEmail());
				System.out.println(newUser.getEmail());
				if(user.getEmail().equals(newUser.getEmail())) {
					System.out.println("aaaaaaaaaaaaaaaaaaaa");
					newUser.setEmail(user.getEmail());
					newUser.setAddress(user.getAddress());
					userRepository.save(newUser);
					return ResponseEntity.ok(new MessageResponse("User Update Sucessfully!"));
					
				}else if (userRepository.existsByEmail(user.getEmail())) {
					return ResponseEntity
							.badRequest()
							.body(new MessageResponse("Error: Email is already in use!!!"));
				}
				newUser.setEmail(user.getEmail());
				newUser.setAddress(user.getAddress());
				userRepository.save(newUser);
				return ResponseEntity.ok(new MessageResponse("User Update Sucessfully!"));
			}
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User Not Found"));
		}catch(Exception e) {
			return ResponseEntity.ok(new MessageResponse("Error: User Update Unscessful!"));
		}
	}

	public ResponseEntity<User> getUserById(String id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if(user.isPresent()) {
				return new ResponseEntity<>(user.get(),HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

	public ResponseEntity<Map<String, Object>> getSearchUsers(String searched, int pageNo, int pageSize) {
		
		try {
			List<User> searchedUser = userRepository.findBySearchContaining(searched);
			//List<User> searchedUser = userRepository.findByRolesName(searched);
			Map<String, Object> response = new HashMap<>();
			PagedListHolder<User> page = new PagedListHolder<User>(searchedUser);
			page.setPageSize(pageSize); // number of items per page
			page.setPage(pageNo); 
			
			response.put("data", page.getPageList());
			response.put("Total_No_Of_Elements", page.getNrOfElements());
			response.put("Total_No_Of_Pages", page.getPage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

//	public ResponseEntity<User> forgotPasswordUpdate(String username, String newPassword) {
//		Optional<User> user = userRepository.findByUsername(username);
//		User _user = new User();
//    	if(user.isPresent()) {
//    		_user = user.get();
//    	}
//    	if(userRepository.existsByEmail(username)) {
//    		user = userRepository.findByEmail(username);
//    		if(user.isPresent()) {
//        		_user = user.get();
//        	}
//    	}
//    	if(_user != null) {
//    		_user.setPassword(encoder.encode(newPassword));
//    		userRepository.save(_user);
//    		return new ResponseEntity<>(_user,HttpStatus.OK);
//    	}else {
//    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    	}
//	}
	
	
}
