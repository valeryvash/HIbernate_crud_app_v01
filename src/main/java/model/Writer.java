package model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "writers")
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "writer_seq",  initialValue = 1, allocationSize = 1)
    long id;

    @Column(nullable = false)
    String name;

    @OneToMany(fetch = FetchType.EAGER)
    List<Post> posts;

    public Writer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return List.copyOf(this.posts);
    }

    public void setPosts(List<Post> posts) {
        this.posts = List.copyOf(posts);
    }
}
