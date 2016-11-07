package au.com.example.service;

import au.com.example.api.model.customer.Customer;
import au.com.example.persistence.dao.customer.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "CustomerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

	@Override
	public List<Customer> getCustomers() {
		return customerDao.getCustomers();
	}

    @Override
    public boolean deleteCustomer(Long id) {
        return customerDao.deleteCustomer(id);
    }

    @Override
    public boolean saveCustomer(Customer customer) { return customerDao.saveCustomer(customer); }
}
