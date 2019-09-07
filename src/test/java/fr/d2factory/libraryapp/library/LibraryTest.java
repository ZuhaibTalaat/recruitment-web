package fr.d2factory.libraryapp.library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

public class LibraryTest {
	private Library library;
	private BookRepository bookRepository;

	@Before
	public void setup() {
		bookRepository = new BookRepository();
		try {
			String booksFile = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("books.json"),
					"UTF-8");
			ObjectMapper objectMapper = new ObjectMapper();
			List<Book> books = objectMapper.readValue(booksFile, new TypeReference<List<Book>>() {
			});
			bookRepository.addBooks(books);
			library = new LibraryImpl(bookRepository);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void member_can_borrow_a_book_if_book_is_available() throws HasLateBooksException {
		Student student = new Student(1, 35f);
		Book book = library.borrowBook(968787565445l, student, LocalDate.now());
		assertEquals("Joseph Heller", book.getAuthor());
		assertEquals(968787565445l, book.getIsbn().getIsbnCode());
		assertEquals("Catch 22", book.getTitle());
	}

	@Test
	public void borrowed_book_is_no_longer_available() throws HasLateBooksException {
		Student student = new Student(1, 35f);
		Resident resident = new Resident(100f);
		library.borrowBook(968787565445l, student, LocalDate.now());
		Book book = library.borrowBook(968787565445l, resident, LocalDate.now());
		assertNull(book);
	}

	@Test
	public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() throws HasLateBooksException {
		Resident resident = new Resident(100f);
		Book book = library.borrowBook(968787565445l, resident, LocalDate.now().minusDays(15));
		library.returnBook(book, resident);
		assertEquals(98.5f, resident.getWallet(), 0.001);
	}

	@Test
	public void students_pay_10_cents_the_first_30days() throws HasLateBooksException {
		Student student = new Student(2, 50f);
		Book book = library.borrowBook(968787565445l, student, LocalDate.now().minusDays(30));
		library.returnBook(book, student);
		assertEquals(47f, student.getWallet(), 0.001);
	}

	@Test
	public void students_in_1st_year_are_not_taxed_for_the_first_15days() throws HasLateBooksException {
		Student student = new Student(1, 50f);
		Book book = library.borrowBook(968787565445l, student, LocalDate.now().minusDays(15));
		library.returnBook(book, student);
		assertEquals(50f, student.getWallet(), 0.001);
	}

	@Test
	public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days()
			throws HasLateBooksException {
		Student student = new Student(2, 50f);
		Book book = library.borrowBook(968787565445l, student, LocalDate.now().minusDays(45));
		library.returnBook(book, student);
		assertEquals(44.75f, student.getWallet(), 0.001);
	}

	@Test
	public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() throws HasLateBooksException {
		Resident resident = new Resident(100f);
		Book book = library.borrowBook(968787565445l, resident, LocalDate.now().minusDays(75));
		library.returnBook(book, resident);
		assertEquals(91f, resident.getWallet(), 0.001);
	}

	@Test()
	public void members_cannot_borrow_book_if_they_have_late_books() {
		Student student = new Student(2, 50f);
		try {
			library.borrowBook(968787565445l, student, LocalDate.now().minusDays(45));
		} catch (HasLateBooksException e) {

		}
		try {
			library.borrowBook(3326456467846l, student, LocalDate.now());
			fail("Should throw HasLateException");
		} catch (HasLateBooksException e) {
			assertEquals(e.getMessage(), "The member is late !");
		}
	}
}
