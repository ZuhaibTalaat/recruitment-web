package fr.d2factory.libraryapp.library;

import java.time.Duration;
import java.time.LocalDate;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

public class LibraryImpl implements Library {
	
	private BookRepository bookRepository;
	
	public LibraryImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		if(member.isTheMemberLate(bookRepository)) {
			throw new HasLateBooksException("The member is late !");
		}
		Book book = bookRepository.findBook(isbnCode);
		if(book == null) {
			return null;
		}
		bookRepository.saveBookBorrow(book, borrowedAt);
		member.addBorrowedBook(book);
		return book;
	}


	@Override
	public void returnBook(Book book, Member member) {
		LocalDate today = LocalDate.now();
		LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
		int numberOfDays = (int) Duration.between(borrowedAt.atStartOfDay(), today.atStartOfDay()).toDays();
		member.payBook(numberOfDays);
		member.removeBorrowedBook(book);
		bookRepository.returnBook(book);
	}

}
