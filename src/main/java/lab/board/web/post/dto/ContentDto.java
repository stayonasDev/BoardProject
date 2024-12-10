package lab.board.web.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContentDto {
    @NotBlank(message="내용을 적어주세요")
    String content;
}
