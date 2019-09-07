package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.HashSet;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.enumeration.BookPrice;
import fr.d2factory.libraryapp.enumeration.DateLimit;

public class Resident extends Member {

	public Resident(float wallet) {
		setWallet(wallet);
		setBorrowedBooks(new HashSet<>());
	}

	@Override
	public void payBook(int numberOfDays) {
		updateWallet(numberOfDays, BookPrice.RESIDENTNORMALPRICE.getPrice());
		if (numberOfDays > DateLimit.RESIDENTDATELIMIT.getLimit()) {
			updateWallet(numberOfDays - DateLimit.RESIDENTDATELIMIT.getLimit(),
					BookPrice.RESIDENTOVERDATEPRICE.getPrice() - BookPrice.RESIDENTNORMALPRICE.getPrice());
		}
	}

	@Override
	public boolean isTheMemberLate(BookRepository bookRepository) {
		LocalDate today = LocalDate.now();
		for (Book book : getBorrowedBooks()) {
			LocalDate borrowedAt = bookRepository.findBorrowedBookDate(book);
			LocalDate oneMonthAfterBorrowedAt = borrowedAt.plusDays(60);
			if (today.compareTo(oneMonthAfterBorrowedAt) > 0) {
				return true;
			}
		}
		return false;
	}

}
