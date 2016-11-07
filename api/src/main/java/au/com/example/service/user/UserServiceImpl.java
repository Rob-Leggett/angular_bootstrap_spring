package au.com.example.service.user;

import au.com.example.constant.Constants;
import au.com.example.persistence.dao.user.UserDAO;
import au.com.example.persistence.exceptions.ChangePasswordException;
import au.com.example.persistence.exceptions.CreateUserException;
import au.com.example.persistence.exceptions.DeleteUserException;
import au.com.example.persistence.exceptions.UpdateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(Constants.SERVICE_USER)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    // === UserDetailsService implementation ===

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException, DataAccessException {
        return userDAO.loadUser(email);
    }

    // === UserDetailsManager implementation ===

    @Override
    public void changePassword(String email, String password) throws ChangePasswordException {
        userDAO.changePassword(email, password);
    }

    @Override
    public boolean userExists(String email) {
        return userDAO.userExists(email);
    }

    @Override
    public void createUser(UserDetails user) throws CreateUserException {
        userDAO.createUser(user);
    }

    @Override
    public void updateUser(UserDetails user) throws UpdateUserException {
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(String email) throws DeleteUserException {
        userDAO.deleteUser(email);
    }

}
