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
    @Query("Select u from User u where u.email = :email ")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.code = :code")
    Optional<User> findByCode(String code);
}
