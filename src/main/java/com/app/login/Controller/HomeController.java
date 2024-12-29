package com.app.login.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.login.Model.User;
import com.app.login.Services.UserServices;

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
	public String addUser() {
		return "userfrom";
	}

	@PostMapping("/adduser")
	public String addUser1(Model model, User user, @AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes) {
		User user1 = new User(user.getName(), user.getEmail(), userDetails.getUsername());
		userServices.adduser(user1);
		redirectAttributes.addFlashAttribute("message", "User Created Successfully.");
		return "redirect:/users";
	}
}
