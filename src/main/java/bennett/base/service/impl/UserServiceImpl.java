package bennett.base.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bennett.base.dao.IUserDao;
import bennett.base.domain.BaseResource;
import bennett.base.domain.User;
import bennett.base.service.IUserService;


@Service
public class UserServiceImpl implements IUserService{

	@Resource
	private IUserDao userDao;
	
	@Override
	public User getUserById(long userId) {
		return userDao.selectByPrimaryKey(userId);
	}

	@Transactional
	@Override
	public void insertUser(List<User> userList) {
		for(int i = 0; i < userList.size(); i++){
			if(i<2){
				userDao.insertSelective(userList.get(i));
			} else {
				throw new RuntimeException();
			}
		}
	}
	
	public void createUser(User user){
		userDao.insertSelective(user);
	}

	@Override
	public void modifyUser(User user) {
		userDao.updateByPrimaryKey(user);
	}

}
