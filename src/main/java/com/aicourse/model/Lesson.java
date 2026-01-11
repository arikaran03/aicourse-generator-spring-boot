package com.aicourse.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.time.OffsetDateTime;

@Entity
@Table(name = "lessons")
public class Lesson implements Persistable<Long> {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Flexible structured blocks
    @Column(columnDefinition = "JSONB", nullable = false)
    private String content;

    @Column(name = "is_enriched", nullable = false)
    private boolean isEnriched = false;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Transient
    private boolean isNew = true;

    @Override
    public Long getId() { return id; }

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    private void markNotNew() { this.isNew = false; }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Getters & Setters
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isEnriched() { return isEnriched; }
    public void setEnriched(boolean enriched) { isEnriched = enriched; }
    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }
}
