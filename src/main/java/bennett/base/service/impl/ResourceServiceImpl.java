package bennett.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import bennett.base.dao.IResourceDao;
import bennett.base.domain.BaseResource;
import bennett.base.service.IResourceService;

@Service
public class ResourceServiceImpl implements IResourceService {

	@Resource
	IResourceDao resourceDao;
	@Override
	public void addResource(BaseResource resource) {
		resourceDao.insert(resource);
	}
	@Override
	public Set<String> findPermissions(List<Long> resourceIds) {
		return resourceDao.findPermissions(resourceIds);
	}
	@Override
	public List<BaseResource> findMenus(Set<String> permissions) {
        List<BaseResource> allResources = resourceDao.findAll();
        List<BaseResource> menus = new ArrayList<BaseResource>();
        for(BaseResource resource : allResources) {
            if(resource.isRootNode()) {
                continue;
            }
            if(resource.getType() != BaseResource.ResourceType.menu) {
                continue;
            }
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
	}

	private boolean hasPermission(Set<String> permissions, BaseResource resource) {
        if(StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }
        for(String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
            if(p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }
}
