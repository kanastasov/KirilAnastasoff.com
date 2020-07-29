package com.personal.page.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.personal.page.exception.UserAccountNotFoundException;
import com.personal.page.model.UserAccount;
import com.personal.page.service.UserAccountService;

@Controller
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@GetMapping("/")
	public String showAllAcounts(Model model) {
		model.addAttribute("listUserAccounts", userAccountService.getAllUserAccounts());
		return "index";

	}

	// create model attribute to bind form data
	@GetMapping("/showUserAccountForm")
	public String showNewAccountForm(Model model) {
		UserAccount userAccount = new UserAccount();
		model.addAttribute("userAccount", userAccount);
		return "newUserAccount";
	}

	@PostMapping("/saveUserAccount")
	public String saveAccount(@ModelAttribute("userAccount") @Valid UserAccount account, BindingResult bindingResult) {

		if (userAccountService.findUserAccountByEmail(account.getEmail()) != null) {
			bindingResult.rejectValue("email", "There is already an account with this email");
		}

		if (bindingResult.hasErrors()) {
			return "newUserAccount";
		}

		userAccountService.saveUserAccount(account);
		return "redirect:/";
	}
	
	

	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate2(@PathVariable(value = "id") long id, Model model) {
		try {
			UserAccount userAccount = userAccountService.getUserAccountById(id);
			model.addAttribute("userAccount", userAccount);

		} catch (UserAccountNotFoundException e) {
			e.printStackTrace();
		}
		return "updateUserAccount";
	}

	@GetMapping("/deleteAccount/{id}")
	public String deleteAccount(@PathVariable(value = "id") long id) {
		userAccountService.deleteUserAccountById(id);
		return "redirect:/";
	}
}
