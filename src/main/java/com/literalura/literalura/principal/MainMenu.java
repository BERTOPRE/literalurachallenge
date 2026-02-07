package com.literalura.literalura.principal;

import com.literalura.literalura.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import java.util.InputMismatchException;


@Component
public class MainMenu implements CommandLineRunner {

    private final BookService bookService;
    private final ApplicationContext context;
    private final Scanner scanner = new Scanner(System.in);

    public MainMenu(BookService bookService, ApplicationContext context) {
        this.bookService = bookService;
        this.context = context;
    }

    @Override
    public void run(String... args) {
        int option = -1;

        while (option != 0) {
            showMenu();
            option = readOption();

            switch (option) {
                case 1 -> searchBookByTitle();
                case 2 -> bookService.listSavedBooks();
                case 3 -> bookService.listAuthors();
                case 4 -> filterAuthorsByYear();
                case 5 -> filterBooksByLanguage();
                case 0 -> exitApplication();
                default -> System.out.println(" Opción no válida");
            }
        }
    }

    private int readOption() {
        try {
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            return option;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private void showMenu() {
        System.out.println("""
                -----------------------------
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores
                4 - Autores vivos en un año
                5 - Libros por idioma
                0 - Salir
                -----------------------------
                """);
    }

    private void searchBookByTitle() {
        System.out.print("Ingrese el título del libro: ");
        String title = scanner.nextLine();
        bookService.searchAndSaveBook(title);
    }

    private void filterAuthorsByYear() {
        System.out.print("Ingrese el año: ");
        try {
            int year = scanner.nextInt();
            scanner.nextLine();
            bookService.listLivingAuthorsInYear(year);
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println(" Año inválido");
        }
    }

    private void filterBooksByLanguage() {
        System.out.print("Ingrese idioma (en, es, fr): ");
        String language = scanner.nextLine();
        bookService.listBooksByLanguage(language);
    }

    private void exitApplication() {
        System.out.println(" Aplicación finalizada");
        SpringApplication.exit(context, () -> 0);
    }
}
