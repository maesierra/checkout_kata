package net.maesierra.checkout.pricing;

/**
 * Subclass of RequiredQuantityPriceRule that only requires one item, so it will be always applied.
 * It will also have the lower precedence 
 *
 */
public class UnitPriceRule extends PriceRule {

	public UnitPriceRule(final String sku) {
		super(sku, 1, 0);
	}

}
