package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.HashSet;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.enumeration.BookPrice;
import fr.d2factory.libraryapp.enumeration.DateLimit;

public class Student extends Member {

	private int year;

	public Student(int year, float wallet) {
		this.year = year;
		setWallet(wallet);
		setBorrowedBooks(new HashSet<>());
	}

	public void payBook(int numberOfDays) {
		if (year == 1) {
			if (numberOfDays > DateLimit.STUDENTFIRSTYEARDATELIMIT.getLimit()
					&& numberOfDays <= DateLimit.STUDENTDATELIMIT.getLimit()) {
				updateWallet(numberOfDays, BookPrice.STUDENTNORMALPRICE.getPrice());
			} else if (numberOfDays > DateLimit.STUDENTDATELIMIT.getLimit()) {
				updateWallet(DateLimit.STUDENTDATELIMIT.getLimit(), 0.10f);
				updateWallet(numberOfDays - DateLimit.STUDENTDATELIMIT.getLimit(),
						BookPrice.STUDENTOVERDATEPRICE.getPrice());
			}
		} else {
			updateWallet(numberOfDays, BookPrice.STUDENTNORMALPRICE.getPrice());
			if (numberOfDays > DateLimit.STUDENTDATELIMIT.getLimit()) {
				updateWallet(numberOfDays - DateLimit.STUDENTDATELIMIT.getLimit(),
						BookPrice.STUDENTOVERDATEPRICE.getPrice() - BookPrice.STUDENTNORMALPRICE.getPrice());
			}
		}
	}

	@Override
	public boolean isTheMemberLate(BookRepository bookRepository) {
		LocalDate today = LocalDate.now();
		for (Book book : getBorrowedBooks()) {
			LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
			LocalDate oneMonthAfterBorrowedAt = borrowedAt.plusDays(30);
			if (today.compareTo(oneMonthAfterBorrowedAt) > 0) {
				return true;
			}
		}
		return false;
	}

}
