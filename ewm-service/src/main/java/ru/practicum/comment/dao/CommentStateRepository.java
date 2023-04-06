package ru.practicum.comment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.StateComment;

public interface CommentStateRepository extends JpaRepository<StateComment, Long> {
}