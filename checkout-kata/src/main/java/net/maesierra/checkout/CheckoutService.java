package net.maesierra.checkout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.maesierra.checkout.pricing.PriceRule;
import net.maesierra.checkout.pricing.UnitPriceRule;

/**
 * Main checkout class
 *
 */
public class CheckoutService 
{
	
	
    private Map<PriceRule, Long> rules;
    


    /**
     * @param rules Map PriceRule => price in pence
     */
	public CheckoutService(final Map<PriceRule, Long> rules) {
		this.rules = rules;
	}

	/**
	 * Computes the price for the given sku and quantity
	 * @param sku
	 * @param quantity
	 * @return optional price 
	 */
	private Optional<Long> price(final String sku, final int quantity) {
		
		int remaining = quantity;
		long price = 0;
		while (remaining > 0) {
			int current = remaining;
			final Optional<Entry<PriceRule, Long>> entry = this.rules.entrySet().stream()
					.filter(e -> e.getKey().accepts(sku, current))
					//order in compare is inverted to do descending order
					.sorted((e1, e2) -> Integer.compare(e2.getKey().getPrecedence(), e1.getKey().getPrecedence()))
					.findFirst();					
			if (!entry.isPresent()) {
				return Optional.empty();
			}
			final PriceRule rule = entry.get().getKey();
			price += rule.getPrice(entry.get().getValue(), remaining);
			remaining -= rule.isAppliedTo(remaining);
		}
		return Optional.of(price);
	}
	

    /**
     * Calculates the total price for the given items 
     * @param items
     * @return 
     * @throws CheckoutException 
     */
	public Long getPrice(List<String> items) throws CheckoutException {
		//Group all the items by sku and then price them		
		final Map<String, Optional<Long>> pricedItems = items.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> price(e.getKey(), e.getValue().intValue())));
		//Locate all the failed items
		final String failedItems = pricedItems.entrySet()
											  .stream()
											  .filter(e -> !e.getValue().isPresent())
											  .map(e -> e.getKey())
											  .collect(Collectors.joining(","));
									  
		if (failedItems.length() != 0) {
			throw new CheckoutException(failedItems + " cannot be priced.");
		}		
		return pricedItems.values().stream().mapToLong(p -> p.get()).sum(); 
		
	}

	public static void main(String[] args) throws Exception {
		final Map<PriceRule, Long> priceRules = new HashMap<PriceRule, Long>() {{
			put(new PriceRule("A", 3, 1), 130L);
			put(new UnitPriceRule("A"),   50L);		
			put(new PriceRule("B", 2, 1), 45L);
			put(new UnitPriceRule("B"),   30L);
			put(new UnitPriceRule("C"),   20L);
			put(new UnitPriceRule("D"),   15L);					
		}};
		final CheckoutService service = new CheckoutService(priceRules);
		final List<String> items = new ArrayList<>();
		items.add("A");
		items.add("B");
		items.add("A");
		items.add("C");
		items.add("A");
		items.add("D");
		items.add("B");
		System.out.println(service.getPrice(items));
	}

}
