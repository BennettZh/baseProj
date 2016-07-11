package bennett.base.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bennett.base.dao.IOrganizationDao;
import bennett.base.domain.Organization;
import bennett.base.service.IOrganizationService;

@Service
public class OrganizationServiceImpl implements IOrganizationService {
	
	@Resource
	IOrganizationDao orgDao;

	@Override
	public Organization findOrgById(long id) {
		return orgDao.selectByPrimaryKey(id);
	}

	@Override
	public void addOrg(Organization org) {
		orgDao.insert(org);
	}

	@Override
	public void deleteOrg(Organization org) {
		orgDao.deleteByPrimaryKey(org.getId());
	}

	@Override
	public void deleteOrgById(long id) {
		orgDao.deleteByPrimaryKey(id);
	}

}
