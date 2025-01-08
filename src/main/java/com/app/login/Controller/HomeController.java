package com.app.login.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.login.Model.User;
import com.app.login.Services.UserServices;

import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	UserServices userServices;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", "Username or email *");
		model.addAttribute("pass", "Password *");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/";
		}

		return "login";
	}

	@GetMapping("/users")
	public String Alluser(Model model) {
		model.addAttribute("user", userServices.getAllUsers());
		return "users";
	}

	@GetMapping("/adduser")
	public String addUser(Model model) {
		model.addAttribute("userform", new User());
		return "userfrom";
	}

	@PostMapping("/adduser")
	public String addUser1(@Valid @ModelAttribute("userform") User user, BindingResult result,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "userfrom";
		}
		User user1 = new User(user.getName(), user.getEmail(), userDetails.getUsername(), null);
		userServices.adduser(user1);
		redirectAttributes.addFlashAttribute("message", "User Created Successfully.");
		return "redirect:/users";
	}

	@GetMapping("/deleteUser/{id}")
	public String delteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		userServices.delteUserById(id);
		redirectAttributes.addFlashAttribute("message", "User Deleted Successfully.");
		return "redirect:/users";
	}

	@GetMapping("/editUser/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		User user = userServices.getUserById(id);
		model.addAttribute("userform", user);
		return "userfrom";
	}

	@PostMapping("/updateuser")
	public String updateUser(@Valid @ModelAttribute("userform") User user, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("userform", user);
			return "userfrom";
		}
		userServices.updateUser(user);
		redirectAttributes.addFlashAttribute("message", "User Updated Successfully.");
		return "redirect:/users";

	}
}
