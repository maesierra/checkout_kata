package net.maesierra.checkout.pricing;

/**
 * Basic implementation of a simple rule that will allow to apply simple rules like 3 items for 1.5   
 * If the item matches the SKU and  quantity is divisible by the required quantity the rule will be applied
 */
public class PriceRule  {
	
	private final String sku;
	private final Integer requiredQuantiyFactor;
	private final Integer precedence;
	
	public PriceRule(final String sku, final Integer requiredQuantiyFactor, final Integer precedence) {
		this.sku = sku;
		this.requiredQuantiyFactor = requiredQuantiyFactor;
		this.precedence = precedence;
	}
	

	public boolean accepts(final String sku, final int quantity) {
		return this.sku.equals(sku) && (quantity >= requiredQuantiyFactor);
	}
	
	public int isAppliedTo(final int quantity) {
		return quantity - (quantity % this.requiredQuantiyFactor);
	}
	
	public Integer getPrecedence() {
		return precedence;
	}
	
	public long getPrice(final Long price, final int quantity) {
		return (quantity / this.requiredQuantiyFactor) * price;
	}



}
