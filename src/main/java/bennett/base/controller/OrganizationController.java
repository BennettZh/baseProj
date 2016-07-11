package bennett.base.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import bennett.base.domain.Organization;
import bennett.base.domain.Permission;
import bennett.base.domain.User;
import bennett.base.service.IOrganizationService;
import bennett.base.service.IPermissionService;
import bennett.base.service.IUserService;

@Controller
@RequestMapping(value = "/org")
public class OrganizationController {

	@Autowired
	private IOrganizationService orgService;
	
	@RequestMapping(value = "/getOrg", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getOrg(Model model, HttpSession session) {
		return "";
	}
	
	@RequestMapping(value="addOrg")
	public String addUser(Model model, HttpSession session){
		Organization org = new Organization();
		org.setAvailable(true);
		org.setName("总公司");
		org.setParentId(0L);
		org.setParentIds("0/");
		orgService.addOrg(org);
		model.addAttribute("result","1");
		return "user/addResult";
	}
}
