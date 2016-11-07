package au.com.example.persistence.dao.user;


import au.com.example.persistence.exceptions.ChangePasswordException;
import au.com.example.persistence.exceptions.CreateUserException;
import au.com.example.persistence.exceptions.DeleteUserException;
import au.com.example.persistence.exceptions.UpdateUserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDAO {
    public abstract UserDetails loadUser(String email)
            throws UsernameNotFoundException;

    public abstract void changePassword(String username, String password) throws ChangePasswordException;

    public abstract boolean userExists(String username);

    public abstract void createUser(UserDetails user) throws CreateUserException;

    public abstract void updateUser(UserDetails user) throws UpdateUserException;

    public abstract void deleteUser(String username) throws DeleteUserException;
}
