package bennett.base.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Test;
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
            if(p1.implies(p2) ) {
                return true;
            }
        }
        return false;
    }
	@Test
	public void testPermisstion(){
		BaseResource resource1 = new BaseResource();
		BaseResource resource2 = new BaseResource();
		BaseResource resource3 = new BaseResource();
		BaseResource resource4 = new BaseResource();
		BaseResource resource5 = new BaseResource();
		BaseResource resource6 = new BaseResource();
		BaseResource resource7 = new BaseResource();
		BaseResource resource8 = new BaseResource();
		BaseResource resource9 = new BaseResource();
		BaseResource resource0 = new BaseResource();
		resource1.setPermission("user:manage");
		resource2.setPermission("user:update:3");
		resource3.setPermission("user:view");
		resource4.setPermission("user:insert");
		resource5.setPermission("user:delete");
		resource6.setPermission("resource:manage");
		resource7.setPermission("resource:update");
		resource8.setPermission("resource:view");
		resource9.setPermission("resource:insert");
		resource0.setPermission("resource:delete");
		Set<String> permissions = new HashSet<String>();
		permissions.add("user:update:3");
		System.out.println(hasPermission(permissions, resource1));
		System.out.println(hasPermission(permissions, resource2));
		System.out.println(hasPermission(permissions, resource3));
		System.out.println(hasPermission(permissions, resource4));
		System.out.println(hasPermission(permissions, resource5));
		System.out.println(hasPermission(permissions, resource6));
		System.out.println(hasPermission(permissions, resource7));
		System.out.println(hasPermission(permissions, resource8));
		System.out.println(hasPermission(permissions, resource9));
		System.out.println(hasPermission(permissions, resource0));
	}
}
