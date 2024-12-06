package lab.board.web.post.controller;

import lab.board.domain.Post;
import lab.board.domain.User;
import lab.board.web.post.dto.PostDto;
import lab.board.web.post.service.PostService;
import lab.board.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "post/register";
    }


    @PostMapping("/register")
    public String registerPost(@ModelAttribute PostDto postDto, @AuthenticationPrincipal UserDetails userDetails
    , RedirectAttributes redirectAttributes) {
        Optional<User> author = userService.findByUsername(userDetails.getUsername()); //현재 로그인한 사용자
        if (author.isPresent()) {
            Post post = new Post(postDto.getTitle(), postDto.getContent(), author.get());
            postService.save(post);
            log.info("Post created by user: {}", author.get().getUsername());
            return "redirect:/";
        }else{
            log.warn("User not found for username: {}", userDetails.getUsername());
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/register";
        }
    }

    @GetMapping ("/detail")
    public String detail(Model model) {
        model("board", );
        return null;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(id);
        if (post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            postService.delete(id);
        }
        return "redirect:/";
    }
}
