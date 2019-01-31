package com.iorga.debattons.web.rest;
import com.iorga.debattons.domain.Reaction;
import com.iorga.debattons.domain.User;
import com.iorga.debattons.repository.ReactionRepository;
import com.iorga.debattons.service.ReactionService;
import com.iorga.debattons.service.UserService;
import com.iorga.debattons.web.rest.errors.BadRequestAlertException;
import com.iorga.debattons.web.rest.util.HeaderUtil;
import com.iorga.debattons.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Reaction.
 */
@RestController
@RequestMapping("/api")
public class ReactionResource {

    private final Logger log = LoggerFactory.getLogger(ReactionResource.class);

    private static final String ENTITY_NAME = "reaction";

    private final ReactionRepository reactionRepository;
    private final ReactionService reactionService;
    private final UserService userService;

    public ReactionResource(ReactionRepository reactionRepository, ReactionService reactionService, UserService userService) {
        this.reactionRepository = reactionRepository;
        this.reactionService = reactionService;
        this.userService = userService;
    }

    /**
     * POST  /reactions : Create a new reaction.
     *
     * @param reaction the reaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reaction, or with status 400 (Bad Request) if the reaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reactions")
    public ResponseEntity<Reaction> createReaction(@Valid @RequestBody Reaction reaction) throws URISyntaxException {
        log.debug("REST request to save Reaction : {}", reaction);
        if (reaction.getId() != null) {
            throw new BadRequestAlertException("A new reaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reaction result = reactionService.saveByUser(reaction, userService.getCurrentUser().get()); // TODO throw 401 if the current user is not found or there is no current user
        return ResponseEntity.created(new URI("/api/reactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reactions : Updates an existing reaction.
     *
     * @param reaction the reaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reaction,
     * or with status 400 (Bad Request) if the reaction is not valid,
     * or with status 500 (Internal Server Error) if the reaction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reactions")
    public ResponseEntity<Reaction> updateReaction(@Valid @RequestBody Reaction reaction) throws URISyntaxException {
        log.debug("REST request to update Reaction : {}", reaction);
        if (reaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Reaction result = reactionRepository.save(reaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reactions : get all the reactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reactions in body
     */
    @GetMapping("/reactions")
    public ResponseEntity<List<Reaction>> getAllReactions(Pageable pageable) {
        log.debug("REST request to get a page of Reactions");
        Page<Reaction> page = reactionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reactions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /reactions/:id : get the "id" reaction.
     *
     * @param id the id of the reaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reaction, or with status 404 (Not Found)
     */
    @GetMapping("/reactions/{id}")
    public ResponseEntity<Reaction> getReaction(@PathVariable Long id) {
        log.debug("REST request to get Reaction : {}", id);
        Optional<Reaction> reaction = reactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reaction);
    }

    /**
     * DELETE  /reactions/:id : delete the "id" reaction.
     *
     * @param id the id of the reaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reactions/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        log.debug("REST request to delete Reaction : {}", id);
        reactionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /reactions/by-parent-reaction-id/:parentReactionId : get all the reactions which have the given "parentReactionId" as parent id
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reactions in body
     */
    @GetMapping("/reactions/by-parent-reaction-id/{parentReactionId}")
    public ResponseEntity<List<Reaction>> getAllReactions(Pageable pageable, @PathVariable Long parentReactionId) {
        log.debug("REST request to get a page of Reactions for parentReactionId: {}", parentReactionId);
        Page<Reaction> page = reactionRepository.findByParentReactionId(parentReactionId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/reactions/by-parent-reaction-id/%s", parentReactionId));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
