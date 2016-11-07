package au.com.example.persistence.dao.user.query;


import au.com.example.constant.Constants;
import au.com.example.persistence.dao.base.query.QueryParameter;
import au.com.example.persistence.dao.base.query.QueryString;

import java.util.ArrayList;
import java.util.List;

public class SelectUser implements QueryString {
    private static final String QUERY = "SELECT u FROM " + Constants.ENTITY_USER + " u WHERE (1=1) ";

    private String email;

    public SelectUser(String email) {
        this.email = email;
    }

    // === QueryString implementation

    @Override
    public String getStatement() {
        StringBuffer statement = new StringBuffer(QUERY);

        if (email != null) {
            statement.append("AND (u.email = :email) ");
        }

        return statement.toString();
    }

    @Override
    public List<QueryParameter> getParameters() {
        List<QueryParameter> parameters = new ArrayList<>();

        if (email != null) {
            parameters.add(new QueryParameter("email", email));
        }

        return parameters;
    }
}
