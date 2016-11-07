package au.com.example.persistence.dao.customer;

import au.com.example.exception.UpdateDeleteException;
import au.com.example.api.model.customer.Customer;
import au.com.example.persistence.dao.base.BaseDao;
import au.com.example.persistence.dao.customer.entity.CustomerEntity;
import au.com.example.persistence.dao.customer.query.SelectCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CustomerDaoImpl extends BaseDao implements CustomerDao {

    private static Logger log = LoggerFactory.getLogger(CustomerDaoImpl.class);

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<Customer>();

        Collection<CustomerEntity> customerEntities = loadData(CustomerEntity.class, new SelectCustomer());

        if (customerEntities == null || customerEntities.isEmpty()) {
            log.info("No customers found");
        }
        else
        {
            for(CustomerEntity customerEntity : customerEntities) {
                customers.add(toCustomer(customerEntity));
            }
        }

        return customers;
    }

    @Transactional
    @Override
    public boolean deleteCustomer(Long id) {
        boolean success = false;

        try {
            success = deleteSingleData(CustomerEntity.class, id);
        }
        catch(UpdateDeleteException e) {
            log.error("Error deleting customer with id: " + id);
        }

        return success;
    }

    @Transactional
    @Override
    public boolean saveCustomer(Customer customer) {
        boolean success = false;

        try {
            success = updateSingleData(toCustomerEntity(customer));
        }
        catch(UpdateDeleteException e) {
            log.error("Error saving customer with id: " + customer.getId());
        }

        return success;
    }

    // ======== Helpers =========

    private Customer toCustomer(CustomerEntity customerEntity) {
        Customer customer = null;

        if(customerEntity != null) {
            customer = new Customer(customerEntity.getId(), customerEntity.getFirstName(), customerEntity.getLastName());
        }

        return customer;
    }

    private CustomerEntity toCustomerEntity(Customer customer) {
        CustomerEntity entity = null;

        if(customer != null) {
            entity = new CustomerEntity(customer.getId(), customer.getFirstName(), customer.getLastName());
        }

        return entity;
    }
}
