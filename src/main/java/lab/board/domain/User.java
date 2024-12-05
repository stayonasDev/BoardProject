package lab.board.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate birthday;

    private String nickname;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // USER / ADMIN

    //ALTER 추가 후 도메인 미 설정으로 엑세스 거부 됨 403 Forbidden
    @Column(nullable = false, unique = true)
    @Email
    String email;

    public User(String name, LocalDate birthday, String nickname, String username, String password, String email) {
        this.name = name;
        this.birthday = birthday;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
