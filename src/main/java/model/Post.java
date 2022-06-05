package model;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "post_seq", initialValue = 1, allocationSize = 3)
    long id;
    @Column(nullable = false)
    String content;

    @ManyToMany
    List<Tag> tags;

    @Column(nullable = false)
    PostStatus postStatus = PostStatus.ACTIVE;


    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public Post() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Tag> getTags() {
        return List.copyOf(tags);
    }

    public void setTags(List<Tag> tags) {
        this.tags = List.copyOf(tags);
    }
}
