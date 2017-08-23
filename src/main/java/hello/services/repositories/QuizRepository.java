package hello.services.repositories;

import org.springframework.data.repository.CrudRepository;

import hello.entities.Quiz;

public interface QuizRepository extends CrudRepository<Quiz, Long> {

}
