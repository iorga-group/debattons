package com.iorga.debattons.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Reaction.
 */
@Entity
@Table(name = "reaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reaction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 280)
    @Column(name = "title", length = 280, nullable = false)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "parentReaction")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reaction> childrenReactions = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("reactions")
    private User creator;

    @ManyToOne
    @JsonIgnoreProperties("childrenReactions")
    private Reaction parentReaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Reaction title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Reaction content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Reaction> getChildrenReactions() {
        return childrenReactions;
    }

    public Reaction childrenReactions(Set<Reaction> reactions) {
        this.childrenReactions = reactions;
        return this;
    }

    public Reaction addChildrenReactions(Reaction reaction) {
        this.childrenReactions.add(reaction);
        reaction.setParentReaction(this);
        return this;
    }

    public Reaction removeChildrenReactions(Reaction reaction) {
        this.childrenReactions.remove(reaction);
        reaction.setParentReaction(null);
        return this;
    }

    public void setChildrenReactions(Set<Reaction> reactions) {
        this.childrenReactions = reactions;
    }

    public User getCreator() {
        return creator;
    }

    public Reaction creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Reaction getParentReaction() {
        return parentReaction;
    }

    public Reaction parentReaction(Reaction reaction) {
        this.parentReaction = reaction;
        return this;
    }

    public void setParentReaction(Reaction reaction) {
        this.parentReaction = reaction;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reaction reaction = (Reaction) o;
        if (reaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reaction{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
