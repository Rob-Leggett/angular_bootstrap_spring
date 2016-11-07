package au.com.example.persistence.dao.customer.query;

import au.com.example.persistence.dao.base.query.QueryParameter;
import au.com.example.persistence.dao.base.query.QueryString;

import java.util.ArrayList;
import java.util.List;

public class SelectCustomer implements QueryString
{
    private static final String QUERY = "SELECT c FROM " + "CustomerEntity" + " c WHERE (1=1) ";

    // === QueryString implementation

    public String getStatement()
    {
        StringBuffer statement = new StringBuffer(QUERY);

        return statement.toString();
    }

    public List<QueryParameter> getParameters()
    {
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();

        return parameters;
    }
}