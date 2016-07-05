package bennett.base.dao;

import java.util.List;

import bennett.base.domain.Role;

public interface IRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
    
    /** 
     * 批量关联角色、权限
     * @param roleId
     * @param permissionId
     */
    public void correlationPermissionList(Long roleId, List<Long> permissionIdList);
    
    /**
     * 关联角色、权限
     * @param roleId
     * @param permissionId
     */
    public void correlationPermission(Long roleId, Long permissionId);
    
    /**
     * 取消关联角色、权限
     * @param roleId
     * @param permissionId
     */
    public void uncorrelationPermission(Long roleId, Long permissionId);

    /**
     * 批量取消关联角色、权限
     * @param roleId
     * @param permissionId
     */
    public void uncorrelationPermissionList(Long roleId, List<Long> permissionId);
    
    /**
     * 取消角色关联的所有权限
     * @param roleId
     */
	public void uncorrelationAllPermission(long roleId);
	
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