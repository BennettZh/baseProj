package bennett.base.service;

import bennett.base.domain.Permission;

public interface IPermissionService {
	
	/**
	 * 添加权限
	 * @param permission
	 */
	public void addPermission(Permission permission);
}
