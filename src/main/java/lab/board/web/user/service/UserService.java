package lab.board.web.user.service;

import lab.board.domain.User;
import lab.board.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService{

    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(repository.findByUsername(username));
    }

    public void delete(User user) {
        repository.delete(user);
    }
    public List<User> findAll(){
        return repository.findAll();
    }
    public void deleteAll(){repository.deleteAll();};
}
