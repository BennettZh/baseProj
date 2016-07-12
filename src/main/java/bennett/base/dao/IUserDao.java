package bennett.base.dao;

import java.util.Set;

import bennett.base.domain.User;

public interface IUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	public Set<String> findRoles(String username);
	public Set<String> findPermissions(String username);

	public User findByUsername(String username);
	
}