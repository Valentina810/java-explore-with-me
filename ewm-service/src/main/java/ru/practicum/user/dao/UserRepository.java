package ru.practicum.user.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "select new ru.practicum.user.model.User(uu.id,uu.name,uu.email) " +
			"from User as uu " +
			"where uu.id in (:ids) ")
	List<User> getUsers(Set<Long> ids, Pageable pageable);
}