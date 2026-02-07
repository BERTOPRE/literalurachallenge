package com.literalura.literalura.service;

import com.literalura.literalura.dto.GutendexAuthor;
import com.literalura.literalura.dto.GutendexBook;
import com.literalura.literalura.dto.GutendexResponse;
import com.literalura.literalura.model.Author;
import com.literalura.literalura.model.Book;
import com.literalura.literalura.repository.AuthorRepository;
import com.literalura.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final GutendexService gutendexService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(GutendexService gutendexService,
                       BookRepository bookRepository,
                       AuthorRepository authorRepository) {
        this.gutendexService = gutendexService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void searchAndSaveBook(String title) {
        GutendexResponse response = gutendexService.searchBooks(title);

        if (response == null || response.results().isEmpty()) {
            System.out.println("No se encontraron libros con ese título.");
            return;
        }

        GutendexBook data = response.results().get(0);

        GutendexAuthor gutAuthor = data.authors().get(0);

        Author author = authorRepository
                .findByName(gutAuthor.name())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(gutAuthor.name());
                    newAuthor.setBirthYear(gutAuthor.birth_year());
                    newAuthor.setDeathYear(gutAuthor.death_year());
                    return authorRepository.save(newAuthor);
                });

        Optional<Book> existingBook = bookRepository.findByTitleIgnoreCase(data.title());

        if (existingBook.isPresent()) {
            System.out.println(" El libro ya está registrado.");
            return;
        }

        Book book = new Book();
        book.setTitle(data.title());
        book.setLanguages(data.languages());
        book.setDownloadCount(data.download_count());
        book.setAuthor(author);

        bookRepository.save(book);
        System.out.println(" Libro guardado correctamente.");
    }


    @Transactional(readOnly = true)
    public void listSavedBooks() {
        List<Book> books = bookRepository.findAll();

        books.forEach(book -> {
            System.out.println("""
                Título: %s
                Idiomas: %s
                """.formatted(
                    book.getTitle(),
                    book.getLanguages()
            ));
        });
    }


    public void listAuthors() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            System.out.println(" No hay autores registrados.");
            return;
        }

        authors.forEach(author -> {
            System.out.println("""
                -------------------------
                 Autor: %s
                 Nacimiento: %s
                ️ Fallecimiento: %s
                -------------------------
                """.formatted(
                    author.getName(),
                    author.getBirthYear(),
                    author.getDeathYear()
            ));
        });
    }


    public void listLivingAuthorsInYear(int year) {
        List<Author> authors = authorRepository.findAll();

        List<Author> livingAuthors = authors.stream()
                .filter(a ->
                        (a.getBirthYear() != null && a.getBirthYear() <= year) &&
                                (a.getDeathYear() == null || a.getDeathYear() > year)
                )
                .toList();

        if (livingAuthors.isEmpty()) {
            System.out.println(" No se encontraron autores vivos en ese año.");
            return;
        }

        livingAuthors.forEach(author ->
                System.out.println("👤 " + author.getName())
        );
    }


    public void listBooksByLanguage(String language) {
        List<Book> books = bookRepository.findByLanguagesContaining(language);

        if (books.isEmpty()) {
            System.out.println(" No hay libros en ese idioma.");
            return;
        }

        books.forEach(book ->
                System.out.println(book.getTitle())
        );
    }
}
