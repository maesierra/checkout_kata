package net.maesierra.checkout.pricing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class PriceRuleUnitTest {

	private static final String SKU = "AA";

	@Test
	public void testAccept_sameQuantity( ) {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.accepts(SKU, 3), is(true));
	}
	
	@Test
	public void testAccept_graterThanQuantity( ) {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.accepts(SKU, 4), is(true));
	}
	
	@Test
	public void testDenies_lessQuantity( ) {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.accepts(SKU, 2), is(false));
	}
	
	@Test
	public void testDenies_otherSKU( ) {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.accepts("dds", 3), is(false));
	}
	@Test
	public void testIsAppliedTo_exactMatch() {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.isAppliedTo(3), is(3));
	}
	@Test
	public void testIsApplied_withRemainder() {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.isAppliedTo(4), is(3));
	}
	@Test
	public void testGetPrice_exactMatch() {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.getPrice(10L, 3), is(10L));
	}
	@Test
	public void testGetPrice_8Times_exact() {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.getPrice(10L, 24), is(80L));
	}
	@Test
	public void testGetPrice_8Times_withRemainder() {
		PriceRule rule = new PriceRule(SKU, 3, 1);
		assertThat(rule.getPrice(10L, 25), is(80L));
	}
	

}
