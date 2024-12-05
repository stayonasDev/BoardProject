package lab.board.web.post.controller;

import lab.board.domain.Post;
import lab.board.domain.User;
import lab.board.web.post.dto.PostDto;
import lab.board.web.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/post/register")
    public String register(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "post/register";
    }


    @PostMapping("/post/register")
    public String registerPost(@ModelAttribute PostDto postDto, @AuthenticationPrincipal User author) {
        Post post = new Post(postDto.getTitle(), postDto.getContent(), author);
        postService.save(post);
        return "redirect:/";
    }
}
