package bennett.base.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import bennett.base.domain.BaseResource;
import bennett.base.service.IResourceService;

@Controller
@RequestMapping(value = "/resource")
public class ResourceController {

	@Autowired
	private IResourceService resourceService;
	
	@RequestMapping(value = "/getResource", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getOrg(Model model, HttpSession session) {
		List<Long> resourceIds = new ArrayList<Long>();
		resourceIds.add(3L);
		Set<String> permissions = resourceService.findPermissions(resourceIds);
		List<BaseResource> menus = resourceService.findMenus(permissions);
		Gson gson = new Gson();
		return gson.toJson(permissions)+"----"+gson.toJson(menus);
//		return gson.toJson(permissions);
	}
	
	@RequestMapping(value="addResource")
	public String addUser(Model model, HttpSession session){
		BaseResource resource = new BaseResource();
		resource.setAvailable(true);
		resource.setName("User Management");
		resource.setParentId(1L);
		resource.setParentIds("0/2/");
		resource.setPermission("user:manage");
		resource.setType(BaseResource.ResourceType.menu);
		resource.setUrl("/user/manage");
		resourceService.addResource(resource);
		model.addAttribute("result","1");
		return "user/addResult";
	}
}
