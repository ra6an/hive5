package com.hive5.hive5.model;

import com.hive5.hive5.model.enums.CommentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    @Transient
    private String oldContent;

    @Column(nullable = false)
    private Boolean edited;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.edited = false;
        this.status = CommentStatus.ACTIVE;
        this.replies = new ArrayList<>();
        this.likes = new ArrayList<>();
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
