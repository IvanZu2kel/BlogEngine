package com.example.blogengine.repository;

import com.example.blogengine.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Integer> {
    @Query(nativeQuery = true, value = "select s.* from global_settings s where s.code = :code")
    GlobalSettings findAllGlobalSettings(@Param("code") String code);

    @Query("select gs from GlobalSettings gs where gs.code = :code ")
    Optional<GlobalSettings> findByCode(String code);
}
