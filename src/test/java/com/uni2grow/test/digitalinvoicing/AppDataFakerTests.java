package com.uni2grow.test.digitalinvoicing;

import com.github.javafaker.Faker;
import com.uni2grow.test.digitalinvoicing.entity.*;
import com.uni2grow.test.digitalinvoicing.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
class AppDataFakerTests {
    private final Faker faker = new Faker();

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void fakeData() {
        truncate();

        var customers = fakeCustomers(30);
        var items = fakeItems(50);

        fakeInvoices(200, customers, items);
    }

    /**
     * Erases all existing data.
     */
    private void truncate() {
        invoiceRepository.deleteAll();
        customerRepository.deleteAll();
        addressRepository.deleteAll();
        itemRepository.deleteAll();
    }

    private List<Customer> fakeCustomers(int size) {
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            Customer customer = Customer.builder()
                .name(String.format("%s %s", faker.name().firstName(), faker.name().lastName()))
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .address(buildAddress())
                .build();

            customers.add(customer);
        }

        return customerRepository.saveAll(customers);
    }

    private List<Item> fakeItems(int size) {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            Item item = Item.builder()
                .name(faker.commerce().productName())
                .price(faker.number().randomDouble(2, 10, 100))
                .build();

            items.add(item);
        }

        return itemRepository.saveAll(items);
    }

    private List<Invoice> fakeInvoices(int size, List<Customer> customers, List<Item> items) {
        List<Invoice> invoices = new ArrayList<>();

        for (int i = 0; i < size; ++i) {
            // Pick a random customer

            Customer customer = customers.get(faker.random().nextInt(0, customers.size() - 1));

            // Find random items

            Set<Integer> itemIndices = new HashSet<>();
            int itemSize = faker.random().nextInt(1, items.size() / 3);
            for (int k = 0; k < itemSize; ++k) {
                itemIndices.add(faker.random().nextInt(0, items.size() - 1));
            }

            // Build invoice

            Invoice invoice = Invoice.builder()
                .customer(customer)
                .billingAddress(faker.random().nextDouble() < 0.7 ? customer.getAddress() : buildAddress())
                // From 90 to 5 days ago
                .issueDate(Instant.now().minusSeconds(faker.random().nextInt(5 * 24 * 3600, 90 * 24 * 3600)))
                .build();

            // Persist to retrieve an identifier

            addressRepository.save(invoice.getBillingAddress());
            invoiceRepository.save(invoice);

            // Prepare the inventory of items

            List<RelInvoiceItem> relInvoiceItems = new ArrayList<>();
            for (Integer itemIndex: itemIndices) {
                Item item = items.get(itemIndex);

                relInvoiceItems.add(
                    RelInvoiceItem.builder()
                        .invoice(invoice)
                        .item(item)
                        .quantity(faker.random().nextInt(1, 7))
                        .priceOfRecord(item.getPrice())
                        .build()
                );
            }

            // Attach inventory

            invoice.setRelInvoiceItems(relInvoiceItems);
            invoice.totalize();

            // Queue invoice for batch persisting

            invoices.add(invoice);
        }

        return invoiceRepository.saveAll(invoices);
    }

    private Address buildAddress() {
        return Address.builder()
            .street(faker.random().nextDouble() < 0.7 ? faker.address().streetAddress() : null)
            .city(faker.address().city())
            .state(faker.random().nextDouble() < 0.4 ? faker.address().state() : null)
            .country(faker.address().country())
            .zipCode(faker.random().nextDouble() < 0.4 ? faker.address().zipCode() : null)
            .build();
    }
}
