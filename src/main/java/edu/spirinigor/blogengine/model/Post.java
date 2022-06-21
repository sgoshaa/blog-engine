package edu.spirinigor.blogengine.model;

import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_active",nullable = false)
    private Short isActive;

    @Column(name = "moderation_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ModerationStatus moderationStatus;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name ="moderator_id")
    private User moderator;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @ToString.Exclude
    @OneToMany(mappedBy = "post",
                cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "tag2post"
            ,joinColumns = {@JoinColumn(name = "post_id")}
            ,inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    @ToString.Exclude
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<PostVotes> postVotes;
}
