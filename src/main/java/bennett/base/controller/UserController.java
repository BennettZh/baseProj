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

import bennett.base.domain.User;
import bennett.base.service.IUserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping(value = "/getUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUser(Model model, HttpSession session,
			@RequestParam int userId) {
		User user = userService.getUserById(userId);
		Gson gson = new Gson();
		return gson.toJson(user);
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
