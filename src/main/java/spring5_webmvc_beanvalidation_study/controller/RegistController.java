package spring5_webmvc_beanvalidation_study.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistController {
	@Autowired
	private MemberRegisterService memberRegisterService;

	@RequestMapping("/register/step1")
	public String handleStep1() {
		return "/register/step1";
	}
	
	@PostMapping("/register/step2")
	public String handleStep2(@RequestParam(value = "agree", defaultValue = "false") Boolean agree, /*Model model*/
			RegisterRequest registerRequest) {
		if (!agree) {
			return "/register/step1";
		}
//		model.addAttribute("registerRequest", new RegisterRequest());
		return "/register/step2";
	}
	
	@GetMapping("/register/step2")
	public String handleStep2Get() {
		return "redirect:/register/step1";
	}
	
	@PostMapping("/register/step3")
	public String handleStep3(@Valid RegisterRequest regReq, Errors errors) {
		if (errors.hasErrors()) 
			return "register/step2";
		
		if (!regReq.isPasswordEqualToConfirmPassword()) {
			errors.rejectValue("confirmPassword", "nomatch");
			return "register/step2";
		}
		
		try {
			memberRegisterService.regist(regReq);
			return "/register/step3";
		} catch (DuplicateMemberException ex) {
			errors.rejectValue("email", "duplicate");
			return "/register/step2";
		}
	}
}
