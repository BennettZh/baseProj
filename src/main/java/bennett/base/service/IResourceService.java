package bennett.base.service;

import java.util.List;
import java.util.Set;

import bennett.base.domain.BaseResource;

public interface IResourceService {
	public void addResource(BaseResource resource);
    /**
     * 得到资源对应的权限字符串  
     * @param resourceIds
     * @return
     */
    public Set<String> findPermissions(List<Long> resourceIds); 
    
    /**
     * 根据用户权限得到菜单
     * @param permissions
     * @return
     */
    public List<BaseResource> findMenus(Set<String> permissions);
}
