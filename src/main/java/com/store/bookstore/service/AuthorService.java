package com.store.bookstore.service;

import com.store.bookstore.dto.author.response.AuthorFullResponseDto;
import com.store.bookstore.dto.author.request.AuthorCreateRequestDto;
import com.store.bookstore.dto.author.response.AuthorResponseDto;
import com.store.bookstore.exception.EntityAlreadyExistsException;
import com.store.bookstore.exception.EntityNotFoundException;
import com.store.bookstore.model.Author;
import com.store.bookstore.model.Book;
import com.store.bookstore.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;
    private final ModelMapper mapper;

    public List<AuthorResponseDto> getAuthorsOnly() {
        List<Author> authors = repository.findAll();

        if (authors.isEmpty()) {
            throw new EntityNotFoundException("No authors found");
        }

        return authors.stream()
                .map(author -> mapper.map(author, AuthorResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AuthorFullResponseDto> getFullAuthors() {
        List<Author> authors = repository.findAll();

        if (authors.isEmpty()) {
            throw new EntityNotFoundException("No authors found");
        }

        return authors.stream()
                .map(author -> mapper.map(author, AuthorFullResponseDto.class))
                .collect(Collectors.toList());
    }

    public AuthorResponseDto getAuthorOnly(String id) {
        Author author = repository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("No author found with ID: " + id));

        return mapper.map(author, AuthorResponseDto.class);
    }

    @Transactional
    public AuthorFullResponseDto getFullAuthor(String id) {
        Author author = repository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("No author found with ID: " + id));

        return mapper.map(author, AuthorFullResponseDto.class);
    }

    @Transactional
    public AuthorFullResponseDto createAuthor(AuthorCreateRequestDto authorDto) {
        Author authorEntity = mapper.map(authorDto, Author.class);

        if (repository.existsByNameAndSurname(authorEntity.getName(), authorEntity.getSurname())) {
            throw new EntityAlreadyExistsException("Author already exists: " + authorEntity.getName() + " " + authorEntity.getSurname());
        }

        if (authorDto.getBooks() != null) {
            List<Book> books = authorDto.getBooks().stream()
                    .map(bookDto -> {
                        Book book = mapper.map(bookDto, Book.class);
                        book.setAuthor(authorEntity);
                        return book;
                    }).toList();
            authorEntity.setBooks(books);
        }

        Author savedAuthor = repository.save(authorEntity);
        return mapper.map(savedAuthor, AuthorFullResponseDto.class);
    }

    @Transactional
    public AuthorFullResponseDto updateAuthor(String id, AuthorCreateRequestDto authorDto) {
        Author author = repository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("No author found with ID: " + id));

        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());

        if (authorDto.getBooks() != null) {
            List<Book> books = authorDto.getBooks().stream()
                    .map(bookDto -> {
                        Book book = mapper.map(bookDto, Book.class);
                        book.setAuthor(author);
                        return book;
                    }).collect(Collectors.toList());
            author.setBooks(books);
        }

        Author updatedAuthor = repository.save(author);
        return mapper.map(updatedAuthor, AuthorFullResponseDto.class);
    }

    public void deleteAuthor(String id) {
        Author author = repository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("No author found with ID: " + id));

        repository.delete(author);
    }
}
