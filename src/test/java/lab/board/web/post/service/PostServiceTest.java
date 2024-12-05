package lab.board.web.post.service;

import lab.board.domain.Post;
import lab.board.domain.User;
import lab.board.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class PostServiceTest {

    private final PostService postService;
    private final UserService userService;

    //스프링 부트에서는 @Autowired는 생략 가능하지만 Junit은 명시해줘야 함
    @Autowired
    public PostServiceTest(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @AfterEach
    void beforeDelete(){
        postService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("게시판 저장 테스트")
    void userEqualUser(){
        LocalDate birth = LocalDate.of(1800, 3, 1);
        User user = userService.save(new User("UserA", birth, "nickname","quest", "1234", "user@user"));
        Post postA = new Post("제목A", "내용A", user);
        Post postB = new Post("제목B", "내용B", user);

        log.info("postA = {}  {}   //  User: {}", postA.getTitle(), postA.getContent(), postA.getAuthor());
        log.info("postB = {}  {}   //  User: {}", postB.getTitle(), postB.getContent(), postB.getAuthor());

        assertThat(postA.getAuthor().getUsername()).isEqualTo(postB.getAuthor().getUsername());
        assertThat(postA.getTitle()).isNotEqualTo(postB.getTitle());
    }
}