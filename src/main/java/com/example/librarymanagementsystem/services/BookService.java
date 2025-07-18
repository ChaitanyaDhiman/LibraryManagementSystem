package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    //Get all books.
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //Get book by its ID.
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    //Add a new book.
    public Book addBook(Book book) {
        if(bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Book with this ISBN: " + book.getIsbn() + " already exists");
        }
        if(bookRepository.findByTitle(book.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Book with this title: " + book.getTitle() + " already exists");
        }
        return bookRepository.save(book);
    }

    //Update an existing book.
    public Book updateBook(Long Id, Book book) {
        Optional<Book> optionalBook = bookRepository.findById(Id);
        if(optionalBook.isPresent()) {
            Book bookToUpdate = optionalBook.get();
            bookToUpdate.setIsbn(book.getIsbn());
            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setPublisher(book.getPublisher());
            bookToUpdate.setBorrowed(book.isBorrowed());
            return bookRepository.save(bookToUpdate);
        } else {
            throw new IllegalArgumentException("Book with this ID: " + book.getId() + " does not exist");
        }
    }

    //Delete a book by its ID.
    public void deleteBook(Long Id) {
        if(bookRepository.findById(Id).isPresent()) {
            bookRepository.deleteById(Id);
        } else {
            throw new IllegalArgumentException("Book with this ID: " + Id + " does not exist");
        }
    }

    //BusinessLogic for Borrowing/Returning.
    public Book borrowBook(Long Id) {
        Optional<Book> optionalBook = bookRepository.findById(Id);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if(!book.isBorrowed()) {
                book.setBorrowed(true);
                return bookRepository.save(book);
            } else {
                throw new IllegalArgumentException("Book with this ID: " + Id + " is already borrowed");
            }
        } else {
            throw new IllegalArgumentException("Book not found with this ID: " + Id);
        }
    }

    public Book returnBook(Long Id) {
        Optional<Book> optionalBook = bookRepository.findById(Id);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if(book.isBorrowed()) {
                book.setBorrowed(false);
                return bookRepository.save(book);
            } else {
                throw new IllegalArgumentException("Book with this ID: " + Id + " is not borrowed");
            }
        } else {
            throw new IllegalArgumentException("Book not found with this ID: " + Id);
        }
    }
}
