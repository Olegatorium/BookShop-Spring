package com.store.bookstore.controller;

import com.store.bookstore.dto.BookFullResponseDto;
import com.store.bookstore.dto.BookRequestDto;
import com.store.bookstore.dto.BookResponseDto;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import com.store.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController

@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable String id) {
        Book book = bookRepository.findById(UUID.fromString(id)).orElse(null);

        BookResponseDto bookResponseDto = modelMapper.map(book, BookResponseDto.class);

        return ResponseEntity.ok(bookResponseDto);
    }

    @GetMapping("/book/full/{id}")
    public ResponseEntity<BookFullResponseDto> getFullBook(@PathVariable String id) {
        Book book = bookRepository.findById(UUID.fromString(id)).orElse(null);

        BookFullResponseDto bookResponseDto = modelMapper.map(book, BookFullResponseDto.class);

        return ResponseEntity.ok(bookResponseDto);
    }

    @PostMapping("/book")
    public ResponseEntity<Book> addBook(@RequestBody BookRequestDto bookRequestDto) {
        Author author = authorRepository.findById(UUID.fromString(bookRequestDto.getAuthorId())).orElse(null);

        if (author == null) {
            return ResponseEntity.badRequest().build();
        }

        Book book = Book.builder()
                .title(bookRequestDto.getTitle())
                .description(bookRequestDto.getDescription())
                .pageCount(bookRequestDto.getPageCount())
                .author(author)
                .build();

        return ResponseEntity.ok(bookRepository.save(book));
    }
}
