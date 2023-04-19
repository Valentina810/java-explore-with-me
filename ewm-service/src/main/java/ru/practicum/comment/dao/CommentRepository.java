package ru.practicum.comment.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query(value = "from Comment as com " +
			"join fetch com.user "+
			"join fetch com.event "+
			"join fetch com.stateComment "+
			"where com.user.id=:userId " +
			"and com.stateComment.id in (:stateIds) " +
			"order by com.created desc ")
	List<Comment> findByUserId(Long userId, List<Long> stateIds, Pageable pageable);

	@Query(value = "from Comment as com " +
			"join fetch com.user " +
			"join fetch com.event " +
			"join fetch com.stateComment " +
			"where com.id in (:ids) " +
			"order by com.id")
	List<Comment> getComments(List<Long> ids);

	@Query(value = "from Comment as com " +
			"join fetch com.user "+
			"join fetch com.event "+
			"join fetch com.stateComment "+
			"where com.event.id =  :eventId " +
			"and com.stateComment.id = :stateCommentsId " +
			"order by com.id desc")
	List<Comment> findByEventIdAndStateCommentIdOrderByCreatedDesc(long eventId, long stateCommentsId, Pageable pageable);

	@Query(value = "from Comment as com " +
			"join fetch com.user "+
			"join fetch com.event "+
			"join fetch com.stateComment "+
			"where com.id=:commentId " +
			"and com.stateComment.id = :stateCommentsId ")
	Optional<Comment> findByIdAndStateCommentId(long commentId,long stateCommentsId);
}