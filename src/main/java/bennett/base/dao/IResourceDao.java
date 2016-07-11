package bennett.base.dao;

import java.util.List;
import java.util.Set;

import bennett.base.domain.BaseResource;

public interface IResourceDao {
    int deleteByPrimaryKey(Long id);

    int insert(BaseResource record);

    int insertSelective(BaseResource record);

    BaseResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseResource record);

    int updateByPrimaryKey(BaseResource record);
    
    /**
     * 得到资源对应的权限字符串  
     * @param resourceIds
     * @return
     */
    public Set<String> findPermissions(List<Long> resourceIds); 
    
    /**
     * 查询全部
     * @return
     */
    public List<BaseResource> findAll();
    
}