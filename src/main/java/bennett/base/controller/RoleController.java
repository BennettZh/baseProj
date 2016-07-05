package bennett.base.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bennett.base.domain.Role;
import bennett.base.service.IRoleService;

@Controller
@RequestMapping(value = "/role")
public class RoleController {

	@Autowired
	private IRoleService roleService;
	
	@RequestMapping(value = "/getUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUser(Model model, HttpSession session,
			@RequestParam int userId) {
		return "";
	}
	
	/**
	 * 添加角色
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value="addRole")
	public String addRole(Model model, HttpSession session){
		Role role = new Role();
		role.setRole("userRole");
		role.setDescription("用户角色");
		role.setAvailable(true);
		roleService.addRole(role);
		return "user/addResult";
	}
	
	/**
	 * 关联角色和权限
	 * @param roleId
	 * @param permissionId
	 * @return
	 */
	@RequestMapping(value="correlationPermission")
	public String correlationPermission(Model model, HttpSession session, @RequestParam(value="roleId")Long roleId,  @RequestParam(value="permissionId")Long permissionId){
		List<Long> list = new ArrayList<Long>();
		list.add(6L);
		list.add(7L);
		list.add(8L);
		roleService.correlationPermissionList(roleId, list);
		return "";
	}
	
	/**
	 * 取消关联角色和权限
	 * @param roleId
	 * @param permissionId
	 * @return
	 */
	@RequestMapping(value="uncorrelationPermission")
	public String uncorrelationPermission(Model model, HttpSession session,  @RequestParam(value="roleId")Long roleId,  @RequestParam(value="permissionId")Long permissionId){
		List<Long> list = new ArrayList<Long>();
		list.add(2L);
		list.add(3L);
		list.add(4L);
		roleService.uncorrelationPermissionList(roleId, list);
		return "";
	}
	
	/**
	 * 取消关联角色的所有权限
	 * @param roleId
	 * @param permissionId
	 * @return
	 */
	@RequestMapping(value="uncorrelationAllPermission")
	public String uncorrelationAllPermission(Model model, HttpSession session,  @RequestParam(value="roleId")Long roleId,  @RequestParam(value="permissionId")Long permissionId){
		List<Long> list = new ArrayList<Long>();
		list.add(2L);
		list.add(3L);
		list.add(4L);
		roleService.uncorrelationAllPermission(roleId);
		return "";
	}
	
	/**
	 * 向角色中添加用户
	 * @param userId
	 * @param roleId
	 */
	@RequestMapping(value="addUserToRole")
	public String addUserToRole(Model model, HttpSession session, @RequestParam(value="roleId") Long roleId, @RequestParam(value="userId") Long userId){
		roleService.addUserToRole(roleId, userId);
		return "";
	}
	
	/**
	 * 向角色中添加多个用户
	 * @param userId
	 * @param roleId
	 */
	@RequestMapping(value="addUserListToRole")
	public String addUserListToRole(Model model, HttpSession session,  @RequestParam(value="roleId")  Long roleId, @RequestParam(value="userId") List<Long> userId){
		roleService.addUserListToRole(roleId, userId);
		return "";
	}
	
	/**
	 * 从角色中移除用户
	 * @param userId
	 * @param roleId
	 */
	@RequestMapping(value="removeUserFromRole")
	public String removeUserFromRole(Model model, HttpSession session, @RequestParam(value="roleId") Long roleId, @RequestParam(value="userId") Long userId){
		roleService.removeUserFromRole(roleId, userId);
		return "";
	}
	
	/**
	 * 从角色中移除多个用户
	 * @param userId
	 * @param roleId
	 */
	@RequestMapping(value="removeUserListFromRole")
	public String removeUserListFromRole(Model model, HttpSession session, @RequestParam(value="roleId") Long roleId, @RequestParam(value="userId") List<Long> userId){
		roleService.removeUserListFromeRole(roleId, userId);
		return "";
	}
	
	/**
	 * 删除角色中的所有用户
	 * @param roleId
	 */
	@RequestMapping(value="removeAllUserFromRole")
	public String removeAllUserFromRole(Model model, HttpSession session, @RequestParam(value="roleId") Long roleId){
		roleService.removeAllUserFromRole(roleId);
		return "";
	}
}
