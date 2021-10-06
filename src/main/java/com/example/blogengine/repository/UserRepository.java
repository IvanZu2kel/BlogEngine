package com.example.blogengine.repository;

import com.example.blogengine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u where u.email = ?1 ")
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (email, name, reg_time, password, is_moderator) VALUES (:email, :name, :time, :password, 0)",
            nativeQuery = true)
    void insertUser(@Param("email") String email, @Param("name") String name, @Param("time") Date time, @Param("password") String password);
}
