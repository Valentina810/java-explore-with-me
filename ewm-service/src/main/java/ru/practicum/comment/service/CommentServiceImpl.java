package ru.practicum.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dao.CommentRepository;
import ru.practicum.comment.dao.CommentStateRepository;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentsUpdateState;
import ru.practicum.comment.mapper.MapperComment;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.StateComment;
import ru.practicum.comment.model.StateCommentValues;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.exception.BadDataException;
import ru.practicum.exception.ConditionsAreNotMetException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final CommentRepository commentRepository;
	private final CommentStateRepository commentStateRepository;

	private final HashMap<String, StateComment> dictionaryStatesComment = new HashMap<>();

	public CommentServiceImpl(UserRepository userRepository, EventRepository eventRepository, CommentRepository commentRepository, CommentStateRepository commentStateRepository) {
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.commentRepository = commentRepository;
		this.commentStateRepository = commentStateRepository;
		this.commentStateRepository.findAll().forEach(e -> dictionaryStatesComment.put(e.getName(), e));
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public CommentDto addComment(long userId, long eventId, CommentCreateDto commentCreateDto) {
		checkLengthComment(commentCreateDto);
		Comment comment = Comment.builder()
				.user(userRepository.findById(userId).orElseThrow(() ->
						new NotFoundException(String.format("Пользователь с id %d не найден!", userId))))
				.event(eventRepository.findById(eventId).orElseThrow(() ->
						new NotFoundException(String.format("Событие c id %d не найдено!", eventId))))
				.text(commentCreateDto.getText())
				.stateComment(dictionaryStatesComment.get(StateCommentValues.MODERATION.name()))
				.created(LocalDateTime.now())
				.build();
		CommentDto commentDto = MapperComment.toCommentDto(commentRepository.save(comment));
		log.info("Создан комментарий {}", commentDto);
		return commentDto;
	}

	private static void checkLengthComment(CommentCreateDto commentCreateDto) {
		if (commentCreateDto.getText().length() > 5000) {
			commentCreateDto.setText(commentCreateDto.getText().substring(0, 5000));
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public CommentDto updateComment(long userId, long commentId, CommentCreateDto commentCreateDto) {
		userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Пользователь с id %d не найден!", userId)));
		Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
				new NotFoundException(String.format("Комментарий с id %d не найден!", commentId)));
		if (comment.getUser().getId().equals(userId)) {
			checkLengthComment(commentCreateDto);
			comment.setText(commentCreateDto.getText());
			comment.setStateComment(dictionaryStatesComment.get(StateCommentValues.MODERATION.name()));
			CommentDto commentDto = MapperComment.toCommentDto(commentRepository.save(comment));
			log.info("Обновлен комментарий {}", commentDto);
			return commentDto;
		} else
			throw new BadDataException("Пользователь может редактировать только комментарий, автором которого он является");
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteComment(long userId, long commentId) {
		userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Пользователь с id %d не найден!", userId)));
		Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
				new NotFoundException(String.format("Комментарий с id %d не найден!", commentId)));
		if (comment.getUser().getId().equals(userId)) {
			if (comment.getStateComment().getName().equals(StateCommentValues.REMOVED.name())) {
				throw new NotFoundException("Запрошенный комментарий не найден");
			} else {
				comment.setStateComment(dictionaryStatesComment.get(StateCommentValues.REMOVED.name()));
				Comment commentSave = commentRepository.save(comment);
				log.info("Удален комментарий {}", commentSave);
			}
		} else
			throw new BadDataException("Пользователь может удалить только комментарий, автором которого он является");
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentDto> getComments(long userId, Integer from, Integer size) {
		List<CommentDto> comments = commentRepository.findByUserId(userId,
						dictionaryStatesComment.values().stream()
								.filter(a -> !a.getName().equals(StateCommentValues.REMOVED.name()))
								.map(e -> e.getId()).collect(Collectors.toList()),
						PageRequest.of(from / size, size))
				.stream().map(MapperComment::toCommentDto)
				.collect(Collectors.toList());
		log.info("Получен список комментариев {}", comments);
		return comments;
	}

	@Override
	public List<CommentDto> updateStateComments(CommentsUpdateState commentsUpdateState) {
		List<Comment> comments = commentRepository.getComments(commentsUpdateState.getCommentIds());
		if (commentsUpdateState.getCommentIds().size() != comments.size()) {
			throw new NotFoundException("Невозможно обновить статус комментариев: в списке идентификаторов передан идентификатор несуществующего комментария");
		} else {
			comments.forEach(
					e -> {
						if (commentsUpdateState.getState().equals(StateCommentValues.PUBLISHED.name())) {
							if (e.getStateComment().getName().equals(StateCommentValues.MODERATION.name())) {
								e.setStateComment(dictionaryStatesComment.get(StateCommentValues.PUBLISHED.name()));
								commentRepository.save(e);
							} else
								throw new ConditionsAreNotMetException("Невозможно обновить статус комментария с id " +
										e.getId() + ": можно изменить статус комментария на PUBLISHED только если текущий статус комментария MODERATION");
						} else if (commentsUpdateState.getState().equals(StateCommentValues.REJECTED.name())) {
							if (e.getStateComment().getName().equals(StateCommentValues.MODERATION.name())) {
								e.setStateComment(dictionaryStatesComment.get(StateCommentValues.REJECTED.name()));
								commentRepository.save(e);
							} else
								throw new ConditionsAreNotMetException("Невозможно обновить статус комментария с id " +
										e.getId() + ": в статус REJECTED можно перевести комментарий из статуса MODERATION");
						} else if (commentsUpdateState.getState().equals(StateCommentValues.REMOVED.name())) {
							if (!e.getStateComment().getName().equals(StateCommentValues.REMOVED.name())) {
								e.setStateComment(dictionaryStatesComment.get(StateCommentValues.REMOVED.name()));
								commentRepository.save(e);
							} else
								throw new ConditionsAreNotMetException("Невозможно обновить статус комментария с id " +
										e.getId() + ": если комментарий в статусе REMOVED то его статус изменять нельзя");
						} else throw new ConditionsAreNotMetException("Невозможно обновить статус комментария с id " +
								e.getId() + ": не предусмотрено изменение статуса комментария на MODERATION или передан некорректный статус");
					}
			);
			return commentRepository.getComments(commentsUpdateState.getCommentIds()).stream().map(MapperComment::toCommentDto).collect(Collectors.toList());
		}
	}
}