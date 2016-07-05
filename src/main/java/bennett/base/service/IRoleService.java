package bennett.base.service;

import java.util.List;

import bennett.base.domain.Role;

public interface IRoleService {
	/**
	 * 添加角色
	 * @param role
	 */
	public void addRole(Role role);
	
	/**
	 * 批量关联角色、权限
	 * @param roleId
	 * @param permissionId
	 */
	public void correlationPermissionList(long roleId, List<Long> permissionId);
	
	/**
	 * 关联角色、权限
	 * @param roleId
	 * @param permissionId
	 */
	public void uncorrelationPermission(long roleId, long permissionId);
	
	/**
	 * 取消角色关联的所有权限
	 * @param roleId
	 */
	public void uncorrelationAllPermission(long roleId);
	
	/**
	 * 批量取消关联角色、权限
	 * @param roleId
	 */
	public void uncorrelationPermissionList(long roleId, List<Long> permissionId);
	
	/**
	 * 向角色中添加用户
	 * @param userId
	 * @param roleId
	 */
	public void addUserToRole(Long roleId, Long userId);
	
	/**
	 * 向角色中添加多个用户
	 * @param userId
	 * @param roleId
	 */
	public void addUserListToRole( Long roleId, List<Long> userId);
	
	/**
	 * 从角色中移除用户
	 * @param userId
	 * @param roleId
	 */
	public void removeUserFromRole(Long roleId, Long userId);
	
	/**
	 * 从角色中移除多个用户
	 * @param userId
	 * @param roleId
	 */
	public void removeUserListFromeRole(Long roleId, List<Long> userId);
	
	/**
	 * 删除角色中的所有用户
	 * @param roleId
	 */
	public void removeAllUserFromRole(Long roleId);
}
