package edu.spirinigor.blogengine.model;

import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_active")
    private Short isActive;

    @Column(name = "moderation_status")
    @Enumerated(value = EnumType.STRING)
    private ModerationStatus moderationStatus;

    @ManyToOne
    @JoinColumn(name ="moderator_id")
    private User moderator;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private LocalDateTime time;

    @Column
    private String text;

    @Column(name = "view_count")
    private Integer viewCount;

    @OneToMany(mappedBy = "post",
                cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @ManyToMany
    @JoinTable(name = "tag2post"
            ,joinColumns = {@JoinColumn(name = "post_id")}
            ,inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;
}
