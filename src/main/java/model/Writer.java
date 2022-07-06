package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "writers")
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,
            name = "name")
    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            mappedBy = "writer"
    )
    private List<Post> posts;

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
        if (this.posts != null) {
            return List.copyOf(this.posts);
        } else {
            return new ArrayList<>();
        }
    }

    public void setPosts(List<Post> posts) {
        if (posts != null) {
            this.posts = List.copyOf(posts);
        }
    }
}
