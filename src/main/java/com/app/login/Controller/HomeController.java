package com.app.login.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.login.Model.ChatMessage;
import com.app.login.Model.User;
import com.app.login.Services.UserServices;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;

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
//	For Single Role
//	@PreAuthorize("hasRole('ADMIN')")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
		userServices.adduser(user, userDetails.getUsername());
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

	// Chat Application Routings

	@MessageMapping("/sendMessage")
	@SendTo("/topic/messages")
	public ChatMessage sendMessage(ChatMessage message) {
		return message;
	}

	@GetMapping("/chat")
	public String Chat() {
		return "chat";
	}

	// Shopping Application Route

	@GetMapping("/shopping")
	public String Shopping() {
		return "shopping";
	}

	@GetMapping("/cart")
	public String Cart() {
		return "cart";
	}

	@PostMapping("/create-payment")
	@ResponseBody
	public Map<String, String> createPaymentIntent(@RequestBody Map<String, Object> paymentData) {
		Map<String, String> response = new HashMap<>();
		try {
			String customerName = (String) paymentData.get("customerName");
			String customerEmail = (String) paymentData.get("customerEmail");
			String customerAddress = (String) paymentData.get("customerAddress");
			long amount = Long.parseLong(paymentData.get("amount").toString()) * 100;

			CustomerCreateParams customerParams = CustomerCreateParams.builder()
					.setName(customerName)
					.setEmail(customerEmail)
					.setAddress(CustomerCreateParams.Address.builder()
							.setLine1(customerAddress)
							.setCity("New Delhi") // city
							.setState("Delhi")
							.setCountry("IN")
							.setPostalCode("110001")
							.build())
					.build();

			Customer customer = Customer.create(customerParams);
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(amount)
					.setDescription("Software Developer")
					.setCustomer(customer.getId())
					.setCurrency("inr").build();
			PaymentIntent intent = PaymentIntent.create(params);
			response.put("clientSecret", intent.getClientSecret());
			response.put("customerId", customer.getId());
		} catch (StripeException e) {
			response.put("error", e.getMessage());
		}
		return response;
	}

}
