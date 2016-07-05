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

import bennett.base.domain.Permission;
import bennett.base.domain.User;
import bennett.base.service.IPermissionService;
import bennett.base.service.IUserService;

@Controller
@RequestMapping(value = "/permission")
public class PermissionController {

	@Autowired
	private IPermissionService permissionService;
	
	@RequestMapping(value = "/getUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUser(Model model, HttpSession session) {
		return "";
	}
	
	@RequestMapping(value="addPermission")
	public String addUser(Model model, HttpSession session){
		Permission permission = new Permission();
		permission.setPermission("user:view");
		permission.setDescription("用户查看权限");
		permission.setAvailable(true);
		permissionService.addPermission(permission);
		model.addAttribute("result","1");
		return "user/addResult";
	}
}
