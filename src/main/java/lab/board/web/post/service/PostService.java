package lab.board.web.post.service;

import lab.board.domain.Post;
import lab.board.web.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;

    public Post save(Post post) {
        return repository.save(post);
    }

    public Post findById(Long id) {
        Optional<Post> findPost = repository.findById(id);
        if (findPost.isEmpty()) {
            //예외 처리 로직
        }
        return findPost.get();
    }

    public List<Post> findAll() {
        return repository.findAll();
    }

//업데이트

    public void delete(Long id) {
         Post findPost = findById(id);
        repository.delete(findPost);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
