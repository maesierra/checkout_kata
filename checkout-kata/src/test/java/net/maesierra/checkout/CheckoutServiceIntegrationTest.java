package net.maesierra.checkout;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import net.maesierra.checkout.pricing.PriceRule;
import net.maesierra.checkout.pricing.UnitPriceRule;

/**
 * Unit test for Checkout application
 */
public class CheckoutServiceIntegrationTest {

	private CheckoutService service;
	private static final String SKU_A = "A";
	private static final String SKU_B = "B";
	private static final String SKU_C = "C";
	private static final String SKU_D = "D";
	private static final long PRICE_A = 50;
	private static final long PRICE_Ax3 = 130;
	private static final long PRICE_B = 30;
	private static final long PRICE_Bx2 = 45;
	private static final long PRICE_C = 20;
	private static final long PRICE_D = 15;
	
	
	private Map<PriceRule, Long> priceRules = new HashMap<PriceRule, Long>() {{
		put(new PriceRule(SKU_A, 3, 1), PRICE_Ax3);
		put(new UnitPriceRule(SKU_A),   PRICE_A);		
		put(new PriceRule(SKU_B, 2, 1), PRICE_Bx2);
		put(new UnitPriceRule(SKU_B),   PRICE_B);
		put(new UnitPriceRule(SKU_C),   PRICE_C);
		put(new UnitPriceRule(SKU_D),   PRICE_D);
				
	}};


	private static List<String> itemList(String... items) {
		return Arrays.stream(items).collect(Collectors.toList());
	}
	
	@Before
	public void setUp() {
		this.service = new CheckoutService(priceRules);
	}
	
	@Test
	public void testSimplePrice() throws Exception {
		assertThat(this.service.getPrice(itemList(SKU_A, SKU_B, SKU_C, SKU_D)), is(115L));
	}
	
	@Test
	public void testWithRules() throws Exception {
		assertThat(this.service.getPrice(itemList(SKU_A, SKU_B, SKU_A, SKU_C, SKU_A, SKU_D, SKU_B)), is(210L));
	}
	
	@Test
	public void test_rulesMatchingTwice() throws Exception {
		//Ax6 =>    2x130
		//Bx3 =>    2x45 + 1x50
		//Cx1 =>    1x20
		//Dx2 =>    1x15
		//Expected: 370
		assertThat(this.service.getPrice(itemList(SKU_A, SKU_A, SKU_A, SKU_A, SKU_A, SKU_B, SKU_B, SKU_C, SKU_D, SKU_A, SKU_B)), is(370L));
	}
	
	
}
