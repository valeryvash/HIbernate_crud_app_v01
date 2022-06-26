package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "post_seq")
    @SequenceGenerator(name = "post_seq", initialValue = 1, allocationSize = 1)
    long id;
    @Column(nullable = false,name = "content")
    String content;

    @ManyToMany(fetch = FetchType.LAZY)
    List<Tag> tags;

    @Column(nullable = false,name = "post_status")
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
        if (this.tags != null) {
            return List.copyOf(this.tags);
        } else {
            return new ArrayList<>();
        }
    }

    public void setTags(List<Tag> tags) {
        if (tags != null) {
            this.tags = List.copyOf(tags);
        }
    }
}
