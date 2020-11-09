package com.Assignment.demo.Controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.demo.Config.BasicAuthUtils;
import com.Assignment.demo.DAO.RoleRepository;
import com.Assignment.demo.DAO.UserRepository;
import com.Assignment.demo.Entity.ERole;
import com.Assignment.demo.Entity.Role;
import com.Assignment.demo.Entity.User;
import com.Assignment.demo.Entity.request.SignupRequest;
import com.Assignment.demo.Entity.resposne.MessageResponse;
import com.Assignment.demo.Service.UserService;




@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	BasicAuthUtils basicUtils;
	
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest){
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),
							 signUpRequest.getAddress());
							 

		Set<String> strRoles = signUpRequest.getRoles();
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
							.orElseThrow(() -> new RuntimeException("Error: Role is not found.."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found..."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageResponse> updateUser (@PathVariable String id, @RequestBody SignupRequest user ){
		return userService.updateUser(id,user);
	}
	@PutMapping(value = "user/{id}")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageResponse> updateUserByUser (@PathVariable String id, @RequestBody SignupRequest user ){
		return userService.updateUserByUser(id,user);
	}
	
	@PutMapping(value = "password/{id}")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<User>updatePassword(
			@PathVariable String id,
			@RequestParam(name = "oldPassword") String oldPassword,
			@RequestParam(name = "newPassword") String newPassword){
		return userService.updatePassword(id,oldPassword,newPassword);
	}
	
//	@PutMapping(value = "forgot/password")
//	public ResponseEntity<User>updatePassword(
//			@RequestParam(name = "username") String username,
//			@RequestParam(name = "newPassword") String newPassword){
//		return userService.forgotPasswordUpdate(username,newPassword);
//	}
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Map<String, Object>> getAllUsers(
			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
			@RequestParam(name = "pageSize",defaultValue = "5") int pageSize){
		return userService.getAllusers(pageNo,pageSize);
	}
	
	@GetMapping(value = "/Searchedpage")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Map<String, Object>> getSearchUsers(
			@RequestParam(name = "searched",defaultValue = "0") String searched,
			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
			@RequestParam(name = "pageSize",defaultValue = "5") int pageSize){
		return userService.getSearchUsers(searched,pageNo,pageSize);
	}
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> getUserById (@PathVariable String id){
		return userService.getUserById(id);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User>deleteUserById(@PathVariable String id){
		return userService.deleteUserById(id);
	}
	

}
