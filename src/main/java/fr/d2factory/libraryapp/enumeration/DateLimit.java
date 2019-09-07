package fr.d2factory.libraryapp.enumeration;

public enum DateLimit {

	RESIDENTDATELIMIT(60), 
	STUDENTDATELIMIT(30),
	STUDENTFIRSTYEARDATELIMIT(15);

	private final int limit;

	private DateLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}
}
