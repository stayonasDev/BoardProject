package lab.board.web.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lab.board.domain.User;
import lab.board.web.user.dto.LoginDto;
import lab.board.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller()
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("loginDto", new LoginDto());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto loginDto, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }

        Optional<User> userOptional = userService.findByUsername(loginDto.getUsername());
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(loginDto.getPassword())) {
            return "redirect:/";
        }else{
            redirectAttributes.addFlashAttribute("loginError", "사용자 이름 또는 비밀번호가 잘못되었습니다.");
            return "redirect:/login"; // 실패 시 로그인 페이지로
        }
    }

    //    TODO
    @GetMapping("/logout")
    public String logout() {
        return "home";
    }

    @GetMapping("/join")
    public String register(Model model) {
        log.info("Get /join 접근");
        model.addAttribute("user", new User());
        return "user/join"; // 회원가입 페이지로
    }

    @PostMapping("/join")
    public String registerUser(@Validated @ModelAttribute User user, BindingResult bindingResult
    , HttpServletRequest request) {

        log.info("회원가입 시도 = {}", request.getServerName());
        if (bindingResult.hasErrors()) {
            return "user/join"; // 유효성 검사 실패 시 회원가입 페이지로
        }

        //아이디 중복확인은 다른 기능에서
        if (user == null) {
            return "redirect:/join";
        }

        userService.save(user);
        //리다이렉트로 로그인 성공 알람? 생각

        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }
}
