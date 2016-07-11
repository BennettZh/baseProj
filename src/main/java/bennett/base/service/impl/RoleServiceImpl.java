package bennett.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bennett.base.dao.IRoleDao;
import bennett.base.domain.Role;
import bennett.base.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

	@Resource
	private IRoleDao roleDao;
	@Override
	public void addRole(Role role) {
		roleDao.insert(role);
	}

	@Override
	public void correlationPermissionList(long roleId, List<Long> permissionId) {
		roleDao.correlationPermissionList(roleId, permissionId);
	}

	@Override
	public void uncorrelationPermission(long roleId, long permissionId) {
		roleDao.uncorrelationPermission(roleId, permissionId);
	}

	@Override
	public void uncorrelationAllPermission(long roleId) {
		roleDao.uncorrelationAllPermission(roleId);
	}

	@Override
	public void uncorrelationPermissionList(long roleId, List<Long> permissionId) {
		roleDao.uncorrelationPermissionList(roleId, permissionId);
	}

	@Override
	public void addUserToRole(Long roleId, Long userId) {
		roleDao.addUserToRole(roleId, userId);;
	}

	@Override
	public void addUserListToRole(Long roleId, List<Long> userId) {
		roleDao.addUserListToRole(roleId, userId);
	}

	@Override
	public void removeUserFromRole(Long roleId, Long userId) {
		roleDao.removeUserFromRole(roleId, userId);
	}

	@Override
	public void removeUserListFromeRole(Long roleId, List<Long> userId) {
		roleDao.removeUserListFromeRole(roleId, userId);
	}

	@Override
	public void removeAllUserFromRole(Long roleId) {
		roleDao.removeAllUserFromRole(roleId);
	}

	@Override
	public Set<String> findRoles(Long[] roleIds) {
		return null;
	}

	@Override
	public Set<String> findPermissions(Long[] roleIds) {
		return null;
	}

}
