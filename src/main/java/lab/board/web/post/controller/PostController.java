package lab.board.web.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lab.board.domain.Comment;
import lab.board.domain.Post;
import lab.board.domain.User;
import lab.board.web.post.dto.ContentDto;
import lab.board.web.post.dto.PostDto;
import lab.board.web.post.service.CommentService;
import lab.board.web.post.service.PostService;
import lab.board.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.time.LocalDateTime;
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
    private final CommentService commentService;

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
        } else {
            log.warn("User not found for username: {}", userDetails.getUsername());
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/register";
        }
    }

    @GetMapping("/detail/{id}")
    //@PathVariable 스프링부트 3 부터인가 파라미터 이름을 적어주지 않으면 에러
    //마찬가지로 @RequestParam 에러
    public String detail(@PathVariable("id") Long id, Model model) {
        Post findPost = postService.findById(id);
        model.addAttribute("post", findPost);
        model.addAttribute("comments", commentService.findByPostId(id));
        return "post/detail";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {

        Post findPost = postService.findById(id);

        PostDto postDto = new PostDto();
        postDto.setTitle(findPost.getTitle());
        postDto.setContent(findPost.getContent());

        model.addAttribute("postDto", postDto);
        return "/post/edit";
    }

    // 스프링 시큐리티로 User 회원 정보 id 빼오기?
    //게시판 id로??
    @PostMapping("/edit/{id}")
    public String commentEdit(@PathVariable("id") Long id, @Validated @ModelAttribute PostDto postDto
            , BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", id);

        if (bindingResult.hasErrors()) {
            return "redirect:/post/edit/{id}";
        }

        Post findPost = postService.findById(id);
        //중간 게시판이 삭제되었는지 검증 고민...
        findPost.setTitle(postDto.getTitle());
        findPost.setContent(postDto.getContent());
        postService.save(findPost);

        return "redirect:/post/detail/{id}";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(id);
        if (post.getAuthor().getUsername().equals(userDetails.getUsername())) {
            postService.delete(id);
        }
        return "redirect:/";
    }


    //댓글 답변 기능 낙관적 락 문제로 잠시 보류
    ///댓글 기능 Post 컨트롤러와 분리 고민
    @PostMapping("/detail/{id}/comment")
    public String addComment(@PathVariable("id") Long postId,
                             @ModelAttribute("comment") ContentDto content,
                             @AuthenticationPrincipal UserDetails userDetails
    ,RedirectAttributes redirectAttributes, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        log.info("접근 URL = {}", referer);
        log.info("접근자 ID = {}", userDetails.getUsername());
        log.info("Content = {}", content);

        Optional<User> findAuthor = userService.findByUsername(userDetails.getUsername());
        if (findAuthor.isEmpty()) {
            redirectAttributes.addAttribute("회원을 찾을 수 없습니다.");
            return "redirect:/" + referer;
        }

        Post post = postService.findById(postId);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(findAuthor.get());
        comment.setContent(content.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setParent(null);
        commentService.save(comment);

        return "redirect:" + referer;
    }


    //TODO
    //회원은 삭제할 수 있지만 아직 해당 게시물 작성자에 대한 검증X
    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long commentId
            ,HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        log.info("댓글 삭제 요청 URL ={}", referer);
        commentService.deleteById(commentId);
        return "redirect:" + referer;
    }
}
