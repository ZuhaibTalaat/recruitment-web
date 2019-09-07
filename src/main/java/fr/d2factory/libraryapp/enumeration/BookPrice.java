package fr.d2factory.libraryapp.enumeration;

public enum BookPrice {
	
	RESIDENTNORMALPRICE(0.1f),
	RESIDENTOVERDATEPRICE(0.2f),
	STUDENTNORMALPRICE(0.1f),
	STUDENTOVERDATEPRICE(0.15f);
	
	
	private final float price;
	
	private BookPrice(float price) {
		this.price = price;
	}
	
	public float getPrice() {
		return price;
	}

}
