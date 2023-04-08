package ru.practicum.comment.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query(value = "from Comment as com " +
			"where com.user.id=:userId " +
			"and com.stateComment.id in (:stateIds) " +
			"order by com.created desc ")
	List<Comment> findByUserId(Long userId, List<Long> stateIds, Pageable pageable);

	@Query(value = "select new ru.practicum.comment.model.Comment(com.id,com.user,com.event,com.text,com.stateComment,com.created) " +
			"from Comment as com " +
			"where com.id in (:ids) " +
			"order by com.id")
	List<Comment> getComments(List<Long> ids);
}