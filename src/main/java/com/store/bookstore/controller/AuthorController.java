package com.store.bookstore.controller;

import com.store.bookstore.dto.author.response.AuthorFullResponseDto;
import com.store.bookstore.dto.author.request.AuthorCreateRequestDto;
import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    @Operation(summary = "Get author by ID", description = "Get author data only")
    public ResponseEntity<AuthorResponseDto> getAuthor(@PathVariable String id) {

        AuthorResponseDto authorResponseDto =  authorService.getAuthorOnly(id);

        return ResponseEntity.ok(authorResponseDto);
    }

    @GetMapping("/author/full/{id}")
    @Operation(summary = "Get author by ID with books", description = "Get author data with books")
    public ResponseEntity<AuthorFullResponseDto> getAuthorFullInfo(@PathVariable String id) {

        AuthorFullResponseDto authorFullResponseDto = authorService.getFullAuthor(id);

        return ResponseEntity.ok(authorFullResponseDto);
    }

    @GetMapping("/authors")
    @Operation(summary = "Get all authors", description = "Get all authors only.")
    public ResponseEntity<List<AuthorResponseDto>> getAuthors() {

        List<AuthorResponseDto> authorResponseDtos = authorService.getAuthorsOnly();

        return ResponseEntity.ok(authorResponseDtos);
    }

    @GetMapping("/authors/full")
    @Operation(summary = "Get authors with books", description = "Get all authors with books.")
    public ResponseEntity<List<AuthorFullResponseDto>> getFullAuthors() {

        List<AuthorFullResponseDto> authorFullResponseDtos = authorService.getFullAuthors();

        return ResponseEntity.ok(authorFullResponseDtos);
    }

    @PostMapping("/author")
    @Operation(summary = "Create author", description = "Create author with books.")
    public ResponseEntity<AuthorFullResponseDto> createAuthor(@RequestBody AuthorCreateRequestDto author) {

        AuthorFullResponseDto authorFullResponseDto = authorService.createAuthor(author);

        return ResponseEntity.status(201).body(authorFullResponseDto);
    }


    @PutMapping("/author/{id}")
    @Operation(summary = "Update author", description = "Update data about author by ID.")
    public ResponseEntity<AuthorFullResponseDto> updateAuthor(@PathVariable String id, @RequestBody AuthorCreateRequestDto author) {


        AuthorFullResponseDto savedAuthor = authorService.updateAuthor(id, author);

        return ResponseEntity.ok(savedAuthor);
    }

    @DeleteMapping("/author/{id}")
    @Operation(summary = "Delete author", description = "Delete author by ID.")
    public ResponseEntity<String> deleteAuthor(@PathVariable String id) {

        authorService.deleteAuthor(id);

        return ResponseEntity.ok("Author deleted");
    }
}
