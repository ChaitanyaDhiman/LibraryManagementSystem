package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    //Display a list of all books.
    @GetMapping// Handles GET requests to /books
    public String getBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    //Show form for adding a new book.
    @GetMapping("/new") // Handles GET requests to /books/new
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("pageTitle", "Add new book");
        return "add-edit-book";
    }

    //Handle form submission for adding/updating a book.
    @PostMapping("/save") // Handles POST requests to /books/save
    public String saveBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        try {
            if (book.getId() == null) {
                bookService.addBook(book);
                redirectAttributes.addFlashAttribute("message", "Book added successfully");
            } else {
                bookService.updateBook(book.getId(), book);
                redirectAttributes.addFlashAttribute("message", "Book updated successfully");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            if(book.getId() == null) {
                return "redirect:/books";
            } else {
                return "redirect:/edit/" + book.getId();
            }
        }
        return "redirect:/books";
    }

    //Show form for editing an existing book
    @GetMapping("/edit/{id}") // Handles Get request to /books/edit/{id}
    public String editBook(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(id);
            if (book == null) {
                throw new IllegalArgumentException("Book not found with id " + id);
            }
            model.addAttribute("book", book);
            model.addAttribute("pageTitle", "Edit Book (ID: " + id + ")");
            return "add-edit-book";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/books";
        }
    }

    //Delete a book.
    @GetMapping("/delete/{id}") // Handles Get request to /books/delete/{id}
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try{
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("message", "Book deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/books";
    }

    //Borrow a book.
    @GetMapping("/borrow/{id}") // Handles Get request to /books/borrow/{id}
    public String borrowBook(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            bookService.borrowBook(id);
            redirectAttributes.addFlashAttribute("message", "Book borrowed successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/books";
    }

    //Return a book.
    @GetMapping("/return/{id}") //Handles GET request to /books/return/{id}
    public String returnBook(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            bookService.returnBook(id);
            redirectAttributes.addFlashAttribute("message", "Book returned successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/books";
    }
}
