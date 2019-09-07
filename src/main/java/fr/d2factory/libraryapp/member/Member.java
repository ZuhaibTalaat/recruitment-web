package fr.d2factory.libraryapp.member;

import java.util.Set;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */
public abstract class Member {
	/**
	 * An initial sum of money the member has
	 */
	private float wallet;

	private Set<Book> borrowedBooks;

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays the number of days they kept the book
	 */
	public abstract void payBook(int numberOfDays);
	
	public abstract boolean isTheMemberLate(BookRepository bookRepository);

	public void updateWallet(int numberOfDays, float price) {
		wallet = wallet - numberOfDays * price;
	}
	
	public void addBorrowedBook(Book book) {
		borrowedBooks.add(book);
	}
	
	public void removeBorrowedBook(Book book) {
		borrowedBooks.remove(book);
	}

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}

	public Set<Book> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(Set<Book> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}
	

}
