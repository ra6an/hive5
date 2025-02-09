package com.hive5.hive5.model;

import com.hive5.hive5.model.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @Transient
    private String oldContent;

    @Column
    private String image;

    @Column(nullable = false)
    private Boolean edited;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @PrePersist
    public void prePersist() {
        this.edited = false;
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void checkIfContentChanged() {
        if (this.oldContent == null || !this.oldContent.equals(this.content)) {
            this.edited = true;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setContent(String content) {
        this.oldContent = this.content;
        this.content = content;
    }
}
