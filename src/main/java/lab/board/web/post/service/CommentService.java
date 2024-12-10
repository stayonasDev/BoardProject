package lab.board.web.post.service;

import lab.board.domain.Comment;
import lab.board.web.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;

    @Transactional
    public void save(Comment comment) {
        repository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByPostId(Long postId) {
        return repository.findByPostId(postId);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Comment not found for id: " + id); // 예외 처리
        }
    }

    @Transactional(readOnly = true)
    public Long findPostIdByCommentId(Long commentId) {
        return repository.findById(commentId)
                .map(comment -> comment.getPost().getId())
                .orElseThrow(() -> new RuntimeException("Comment not found")); // 예외 처리
    }
}
