package com.iorga.debattons.web.rest;

import com.iorga.debattons.DebattonsApp;
import com.iorga.debattons.domain.Reaction;
import com.iorga.debattons.domain.User;
import com.iorga.debattons.repository.ReactionRepository;
import com.iorga.debattons.service.ReactionService;
import com.iorga.debattons.service.UserService;
import com.iorga.debattons.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static com.iorga.debattons.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.iorga.debattons.domain.enumeration.ReactionType;
/**
 * Integration tests for the {@Link ReactionResource} REST controller.
 */
@SpringBootTest(classes = DebattonsApp.class)
public class ReactionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ReactionType DEFAULT_TYPE = ReactionType.ROOT;
    private static final ReactionType UPDATED_TYPE = ReactionType.AGREE;

    private static final Integer DEFAULT_TYPE_LEVEL = 1;
    private static final Integer UPDATED_TYPE_LEVEL = 2;

    private static final Double DEFAULT_SUPPORT_SCORE = 1D;
    private static final Double UPDATED_SUPPORT_SCORE = 2D;

    private static final Integer DEFAULT_TOTAL_CHILDREN_COUNT = 1;
    private static final Integer UPDATED_TOTAL_CHILDREN_COUNT = 2;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restReactionMockMvc;

    private Reaction reaction;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReactionResource reactionResource = new ReactionResource(reactionRepository, reactionService, userService);
        this.restReactionMockMvc = MockMvcBuilders.standaloneSetup(reactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createEntity(EntityManager em) {
        User user = new User();
        user.setLogin("user");
        user.setId(4L);

        Reaction reaction = new Reaction()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .type(DEFAULT_TYPE)
            .typeLevel(DEFAULT_TYPE_LEVEL)
            .supportScore(DEFAULT_SUPPORT_SCORE)
            .totalChildrenCount(DEFAULT_TOTAL_CHILDREN_COUNT)
            .creator(user);
        return reaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createUpdatedEntity(EntityManager em) {
        Reaction reaction = new Reaction()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .type(UPDATED_TYPE)
            .typeLevel(UPDATED_TYPE_LEVEL)
            .supportScore(UPDATED_SUPPORT_SCORE)
            .totalChildrenCount(UPDATED_TOTAL_CHILDREN_COUNT);
        return reaction;
    }

    @BeforeEach
    public void initTest() {
        reaction = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser
    public void createReaction() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();

        // Create the Reaction
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isCreated());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate + 1);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReaction.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testReaction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testReaction.getTypeLevel()).isEqualTo(DEFAULT_TYPE_LEVEL);
        assertThat(testReaction.getSupportScore()).isNull();
        assertThat(testReaction.getTotalChildrenCount()).isEqualTo(DEFAULT_TOTAL_CHILDREN_COUNT);
    }

    @Test
    @Transactional
    @WithMockUser
    public void createReactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();

        // Create the Reaction with an existing ID
        reaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = reactionRepository.findAll().size();
        // set the field null
        reaction.setType(null);

        // Create the Reaction, which fails.

        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalChildrenCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = reactionRepository.findAll().size();
        // set the field null
        reaction.setTotalChildrenCount(null);

        // Create the Reaction, which fails.

        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReactions() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList
        restReactionMockMvc.perform(get("/api/reactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].typeLevel").value(hasItem(DEFAULT_TYPE_LEVEL)))
            .andExpect(jsonPath("$.[*].supportScore").value(hasItem(DEFAULT_SUPPORT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalChildrenCount").value(hasItem(DEFAULT_TOTAL_CHILDREN_COUNT)));
    }

    @Test
    @Transactional
    public void getReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get the reaction
        restReactionMockMvc.perform(get("/api/reactions/{id}", reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reaction.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.typeLevel").value(DEFAULT_TYPE_LEVEL))
            .andExpect(jsonPath("$.supportScore").value(DEFAULT_SUPPORT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.totalChildrenCount").value(DEFAULT_TOTAL_CHILDREN_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingReaction() throws Exception {
        // Get the reaction
        restReactionMockMvc.perform(get("/api/reactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser
    public void updateReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Update the reaction
        Reaction updatedReaction = reactionRepository.findById(reaction.getId()).get();
        // Disconnect from session so that the updates on updatedReaction are not directly saved in db
        em.detach(updatedReaction);
        updatedReaction
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .type(UPDATED_TYPE)
            .typeLevel(UPDATED_TYPE_LEVEL)
            .supportScore(UPDATED_SUPPORT_SCORE)
            .totalChildrenCount(UPDATED_TOTAL_CHILDREN_COUNT);

        restReactionMockMvc.perform(put("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReaction)))
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
        assertThat(testReaction.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReaction.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testReaction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testReaction.getTypeLevel()).isEqualTo(UPDATED_TYPE_LEVEL);
        assertThat(testReaction.getSupportScore()).isNull();
        assertThat(testReaction.getTotalChildrenCount()).isEqualTo(UPDATED_TOTAL_CHILDREN_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Create the Reaction

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc.perform(put("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser
    public void deleteReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeDelete = reactionRepository.findAll().size();

        // Delete the reaction
        restReactionMockMvc.perform(delete("/api/reactions/{id}", reaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reaction.class);
        Reaction reaction1 = new Reaction();
        reaction1.setId(1L);
        Reaction reaction2 = new Reaction();
        reaction2.setId(reaction1.getId());
        assertThat(reaction1).isEqualTo(reaction2);
        reaction2.setId(2L);
        assertThat(reaction1).isNotEqualTo(reaction2);
        reaction1.setId(null);
        assertThat(reaction1).isNotEqualTo(reaction2);
    }

    @Test
    @WithMockUser
    @Transactional
    public void createARootReactionReplyingToAnotherOneTest() throws Exception {
        // Initialize the database
        Reaction savedReaction = reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeCreate = reactionRepository.findAll().size();

        Reaction replyReaction = new Reaction()
            .title("reply")
            .content("reply")
            .type(ReactionType.ROOT)
            .parentReaction(savedReaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(replyReaction)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate);
    }
}
