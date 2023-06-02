package functional;

import java.util.Objects;

public class Book implements Comparable<Book> {

    private final String ISBN;
    private final String title;
    private final int yearOfPublication;
    private final Genre genre;

    public Book(String ISBN, String title, int yearOfPublication, Genre genre) {
        Objects.requireNonNull(ISBN);
        Objects.requireNonNull(title);
        Objects.requireNonNull(genre);
        this.ISBN = ISBN;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.genre = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return '{' +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", genre=" + genre +
                '}';
    }

    /**
     * Ahora al ser dos objetos diferentes pero con los mismos datos, el conjuto (set) los compara como dos elementos
     * diferentes.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return yearOfPublication == book.yearOfPublication && Objects.equals(ISBN, book.ISBN) && Objects.equals(title, book.title) && genre == book.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN, title, yearOfPublication, genre);
    }

    /**
     * Compara dos libros por su titulo.
     */
    @Override
    public int compareTo(Book o) {
        return title.compareTo(o.title);
    }
}
