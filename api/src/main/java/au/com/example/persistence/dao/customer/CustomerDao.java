package au.com.example.persistence.dao.customer;

import au.com.example.api.model.customer.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getCustomers();

    boolean deleteCustomer(Long id);

    boolean saveCustomer(Customer customer);
}
