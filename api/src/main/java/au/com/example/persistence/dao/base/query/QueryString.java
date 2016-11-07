package au.com.example.persistence.dao.base.query;

import java.util.List;

/**
 * QueryString
 *
 * @author Robert Leggett, O-I
 * @version $Id$
 */
public interface QueryString {
    public String getStatement();

    public List<QueryParameter> getParameters();
}
