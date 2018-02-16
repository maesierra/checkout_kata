package net.maesierra.checkout;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import net.maesierra.checkout.pricing.PriceRule;

/**
 * Unit test for Checkout application
 */
public class CheckoutServiceUnitTest {

	private CheckoutService service;
	private static final String SKU_A = "A";
	private static final String SKU_B = "B";
	private static final String SKU_C = "C";
	private static final String SKU_D = "D";
	private static final String SKU_E = "E";
	private static final long PRICE_A = 50;
	private static final long PRICE_Ax3 = 130;
	private static final long PRICE_B = 30;
	private static final long PRICE_Bx2 = 45;
	private static final long PRICE_C = 20;
	private static final long PRICE_D = 15;
	
	
	private Map<PriceRule, Long> priceRules = new HashMap<PriceRule, Long>() {{
		put(priceRule(SKU_A, 1, 0), PRICE_A);		
		put(priceRule(SKU_A, 3, 1), PRICE_Ax3);
		put(priceRule(SKU_B, 1, 0), PRICE_B);
		put(priceRule(SKU_B, 2, 1), PRICE_Bx2);
		put(priceRule(SKU_C, 1, 0), PRICE_C);
		put(priceRule(SKU_D, 1, 0), PRICE_D);
				
	}};


	private static PriceRule priceRule(final String sku, final Integer quantity, final Integer precedence) {
		final PriceRule rule = mock(PriceRule.class);
		when(rule.accepts(sku, quantity)).thenReturn(true);
		when(rule.getPrecedence()).thenReturn(precedence);
		when(rule.getPrice(anyLong(), anyInt())).then(a -> a.getArgument(0));
		when(rule.isAppliedTo(quantity)).thenReturn(quantity);
		return rule;
	}
	
	private static List<String> itemList(String... items) {
		return Arrays.stream(items).collect(Collectors.toList());
	}
	
	@Before
	public void setUp() {
		this.service = new CheckoutService(priceRules);
	}
	
	@Test
	public void testNoItems() throws Exception {
		assertThat(this.service.getPrice(itemList()), is(0L));
	}
	
	@Test
	public void testSimplePrice() throws Exception {
		assertThat(this.service.getPrice(itemList(SKU_A, SKU_B, SKU_C, SKU_D)), is(115L));
	}
	
	@Test(expected = CheckoutException.class)
	public void testNoPrice() throws Exception {
		this.service.getPrice(itemList(SKU_A, SKU_E));
		
	}
	
	@Test
	public void testWithRules() throws Exception {
		assertThat(this.service.getPrice(itemList(SKU_A, SKU_B, SKU_A, SKU_C, SKU_A, SKU_D, SKU_B)), is(210L));
	}
	
}
