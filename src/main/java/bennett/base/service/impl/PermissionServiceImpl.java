package bennett.base.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bennett.base.dao.IPermissionDao;
import bennett.base.domain.Permission;
import bennett.base.service.IPermissionService;

@Service
public class PermissionServiceImpl implements IPermissionService {

	@Resource
	private IPermissionDao permissionDao;
	
	
	@Override
	public void addPermission(Permission permission) {
		permissionDao.insertSelective(permission);
	}

}
