package com.Assignment.demo.Entity;

//import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Books")
public class Books {
	@Id
	long id;
	String name;
	String authorName;
	String description;
	int isbNumber;
	int price;
	String sellerId; //user id
	
	
	public Books (long id, String name, String authorName, String description, int isbNumber, int price,  String sellerId) {
		this.id = id;
		this.name = name;
		this.authorName = authorName;
		this.description = description;
		this.isbNumber = isbNumber;
		this.price = price;
		this.sellerId = sellerId;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAuthorName() {
		return authorName;
	}


	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getIsbNumber() {
		return isbNumber;
	}


	public void setIsbNumber(int isbNumber) {
		this.isbNumber = isbNumber;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public String getSellerId() {
		return sellerId;
	}


	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	@Override
	public String toString() {
		return "Books [id=" + id + ", name=" + name + ", authorName=" + authorName + ", description=" + description
				+ ", isbNumber=" + isbNumber + ", price=" + price + ", sellerId=" + sellerId + "]";
	}

}
