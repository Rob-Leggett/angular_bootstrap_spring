package au.com.example.persistence.dao.user.query;

import au.com.example.constant.Constants;
import au.com.example.persistence.dao.base.query.QueryParameter;
import au.com.example.persistence.dao.base.query.QueryString;

import java.util.ArrayList;
import java.util.List;

public class UpdatePassword implements QueryString {
    private static final String QUERY = "UPDATE " + Constants.ENTITY_USER + " ";

    private String username;
    private String password;

    public UpdatePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // === QueryString implementation

    @Override
    public String getStatement() {
        StringBuffer statement = new StringBuffer(QUERY);

        if (username != null && password != null) {
            statement.append("SET password = :password ");
            statement.append("WHERE username = :username ");
        }

        return statement.toString();
    }

    @Override
    public List<QueryParameter> getParameters() {
        List<QueryParameter> parameters = new ArrayList<QueryParameter>();

        if (username != null && password != null) {
            parameters.add(new QueryParameter("password", password));
            parameters.add(new QueryParameter("username", username));
        }

        return parameters;
    }
}
