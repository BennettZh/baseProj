package bennett.base.service;

import java.util.List;
import java.util.Set;

import bennett.base.domain.User;

public interface IUserService {
    public User getUserById(long userId);  
    public void insertUser(List<User> userList);
    public void createUser(User user);
    public void modifyUser(User user);
    public Set<String> findRoles(String username);
    public Set<String> findPermissions(String username);
	public User findByUsername(String username);
}
