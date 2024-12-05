package lab.board.web.user.service;

import lab.board.domain.Role;
import lab.board.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceTest {

    private final UserService service;

    @Autowired
    public UserServiceTest(UserService service) {
        this.service = service;
    }

    @AfterEach
    public void delete(){
        service.deleteAll();
    }

    @Test
    @DisplayName("회원 저장 테스트")
    void save() {
        User user = new User();
        user.setUsername("a");
        user.setPassword("a");
        user.setBirthday(LocalDate.now());
        user.setNickname("a");
        user.setPassword("a");
        user.setName("a");
        user.setEmail("a@a");

        User userA = service.save(user);
        Optional<User> optionalUser = service.findByUsername("a");
        if(optionalUser.isPresent()){
            User userB = optionalUser.get();
            assertThat(user.getUsername()).isEqualTo(userB.getUsername());
        }
//        assertThatThrownBy(() -> service.save(user)).isInstanceOf(NullPointerException.class);
    }
}