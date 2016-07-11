package bennett.base.service;

import bennett.base.domain.Organization;

public interface IOrganizationService {
	
	public Organization findOrgById(long id);
	
	public void addOrg(Organization org);
	
	public void deleteOrg(Organization org);
	
	public void deleteOrgById(long id);
	
}
