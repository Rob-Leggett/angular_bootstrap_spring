package au.com.example.service;

import au.com.example.api.model.customer.Customer;

import java.util.List;

public interface CustomerService {
	List<Customer> getCustomers();

    boolean deleteCustomer(Long id);

    boolean saveCustomer(Customer customer);
}
