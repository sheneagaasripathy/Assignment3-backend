package com.Assignment.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.Assignment.demo.Entity.Books;
import com.Assignment.demo.Service.BookService;



@CrossOrigin ("*")
@RestController
@RequestMapping(value = "/books")
public class BookController {
 
	@Autowired
	private BookService bookService;
	
	@PostMapping()
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Books> createBook(@RequestBody Books book){
		return bookService.createBook(book);
	}
	
	@GetMapping(value= "/{id}")
	public ResponseEntity<Books> getBook(@PathVariable long id){
		return bookService.getBookById(id);
	}
	
	@GetMapping (value = "/page")
	public ResponseEntity <Map<String, Object>> getAllBooks(
			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
			@RequestParam(name = "pageSize",defaultValue = "5") int pageSize){
		return bookService.getAllBooks(pageNo,pageSize);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Books> deleteBook(@PathVariable long id){
		return bookService.deleteBook(id);
	}
	
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Books> updateBook(@PathVariable long id, @RequestBody Books book){
		return bookService.updateBook(id,book);
	}
	
	@GetMapping(value = "/page/serachedPages")
	public ResponseEntity<Map<String,Object>> getSerchedBook(
			@RequestParam(name = "serched",defaultValue = "null") String searched,
			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
			@RequestParam(name = "pageSize",defaultValue = "5") int pageSize
			){
		return bookService.searchedBooks(searched,pageNo,pageSize);
	}
	
//	@GetMapping(value = "/page/serachedPages")
//	public ResponseEntity<List<Books>> getSerchedBook(
//			@RequestParam(name = "serched",defaultValue = "") String searched,
//			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
//			@RequestParam(name = "pageSize",defaultValue = "15") int pageSize
//			){
//		return bookService.searchedBooks(searched,pageNo,pageSize);
//	}
//	
	
//	@GetMapping(value = "/page/filteredpage")
//	public ResponseEntity<Map<String,Object>> getFilteredBook(
//			@RequestParam(name = "usage",defaultValue = "null") String usage,
//			@RequestParam(name = "category",defaultValue = "null") String category,
//			@RequestParam(name = "minPrice",defaultValue = "0") int minPrice,
//			@RequestParam(name = "maxPrice",defaultValue = "10000") int maxPrice,
//			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
//			@RequestParam(name = "pageSize",defaultValue = "5") int pageSize
//			){
//		return bookService.getFilteredBook(usage,category,minPrice,maxPrice,pageNo,pageSize);
//	}
//	@GetMapping(value = "/page/filteredpage")
//	public ResponseEntity<List<Books>> getFilteredBook(
//			@RequestParam(name = "usage") String usage,
//			@RequestParam(name = "category") String category,
//			@RequestParam(name = "minPrice",defaultValue = "0") int minPrice,
//			@RequestParam(name = "maxPrice",defaultValue = "10000") int maxPrice,
//			@RequestParam(name = "pageNo",defaultValue = "0") int pageNo,
//			@RequestParam(name = "pageSize",defaultValue = "15") int pageSize
//			){
//		return bookService.getFilteredBook(usage,category,minPrice,maxPrice,pageNo,pageSize);
//	}
}
