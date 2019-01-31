package com.iorga.debattons.repository;

import com.iorga.debattons.domain.Reaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Reaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("select reaction from Reaction reaction where reaction.creator.login = ?#{principal.username}")
    List<Reaction> findByCreatorIsCurrentUser();

}
