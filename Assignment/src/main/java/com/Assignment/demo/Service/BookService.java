package com.Assignment.demo.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Assignment.demo.DAO.BookRepository;
import com.Assignment.demo.DAO.BookRepositoryCustom;
import com.Assignment.demo.Entity.Books;


@Service
public class BookService {
 
	@Autowired
	BookRepositoryCustom bookRepoCustom;
	@Autowired
	BookRepository bookRepo;

	public ResponseEntity<Books> createBook(Books book) {
		try {
			long id = bookRepoCustom.getMaxBookID() + 1;
			Books newBook = bookRepo.save(new Books(id,book.getName(),book.getAuthorName(),book.getDescription(),book.getIsbNumber(),book.getPrice(),book.getSellerId()));
			return new ResponseEntity<>(newBook,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	public ResponseEntity<Books> getBookById(long id) {
		try {
			Optional<Books> book = bookRepo.findById(id);
			if(book.isPresent()) {
				return new ResponseEntity<>(book.get(),HttpStatus.OK);
			}
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	public ResponseEntity<Map<String, Object>> getAllBooks(int pageNo ,int pageSize) {
		try {
            Map<String, Object> response = new HashMap<>();
            Sort sort = Sort.by(Sort.Direction.DESC,"id");
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            Page<Books> page = bookRepo.findAll(pageable);
            response.put("data", page.getContent());
            response.put("Total_No_Of_Pages", page.getTotalPages());
            response.put("Total_No_Of_Elements", page.getTotalElements());
            response.put("Current page no", page.getNumber());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

	public ResponseEntity<Books> deleteBook(long id) {
		try {
			Optional<Books> bookData = bookRepo.findById(id);
			if(bookData.isPresent()) {
				bookRepo.deleteById(id);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

	public ResponseEntity<Books> updateBook(long id, Books book) {
		try {
			Optional<Books> bookData = bookRepo.findById(id);
			if(bookData.isPresent()) {
				Books _book = bookData.get();
				_book.setName(book.getName());
				_book.setAuthorName(book.getAuthorName());
				_book.setDescription(book.getDescription());
//				_book.setCategory(book.getCategory());
				_book.setIsbNumber(book.getIsbNumber());
				_book.setPrice(book.getPrice());
			
				_book.setSellerId(book.getSellerId());
				return new ResponseEntity<>(bookRepo.save(_book),HttpStatus.OK);
			}
			return new ResponseEntity<> (HttpStatus.NOT_FOUND);
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}

	public ResponseEntity<Map<String, Object>> searchedBooks(String searched, int pageNo, int pageSize) {
		
		List<Books> searchedBooks = bookRepo.findBySearchContaining(searched);
		Map<String, Object> response = new HashMap<>();
		PagedListHolder<Books> page = new PagedListHolder<Books>(searchedBooks);
		page.setPageSize(pageSize); // number of items per page
		page.setPage(pageNo); 
		
		response.put("data", page.getPageList());
		response.put("Total_No_Of_Elements", page.getNrOfElements());
		response.put("Total_No_Of_Pages", page.getPage());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
