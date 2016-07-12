package bennett.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import bennett.base.domain.BaseResource;
import bennett.base.domain.User;
import bennett.base.service.IResourceService;
import bennett.base.service.IUserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IResourceService resourceService;
	
	@RequestMapping(value = "/getUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUser(Model model, HttpSession session,
			@RequestParam int userId) {
		User user = userService.getUserById(userId);
		Gson gson = new Gson();
		Set<String> roles = userService.findRoles("admin");
		Set<String> permissions = userService.findPermissions("admin");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("permissions", permissions);
		map.put("user", user);
		map.put("roles", roles);
		List<BaseResource> userMenus = resourceService.findMenus(permissions);
		map.put("userMenus", userMenus);
		return gson.toJson(map);
	}
	
	@RequestMapping(value="addUser")
	public String addUser(Model model, HttpSession session, @RequestParam(value="name") String name, @RequestParam(value="password") String password){
		User user = new User();
		user.setUsername(name);
		user.setPassword(password);
		userService.createUser(user);
		model.addAttribute("result",1);
		return "user/addResult";
	}
}
