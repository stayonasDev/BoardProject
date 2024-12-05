package lab.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // 긴 텍스트 저장하기 위함
    private String content;

    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author;

    private LocalDateTime createdAt;

    private int viewCount;

}
