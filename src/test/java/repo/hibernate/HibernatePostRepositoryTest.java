package repo.hibernate;

import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import model.Post;
import model.PostStatus;
import model.Tag;
import org.junit.jupiter.api.*;
import repo.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Education purpose only
// Test shall be run with create-drop hibernate property.
// Tests shall be run in the described order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernatePostRepositoryTest {
    static PostRepository postRepo = null;
    @BeforeAll
    static void set() {
        postRepo = new HibernatePostRepositoryImpl();
    }
    @Test
    @Order(10)
    void add() {
        Post post = new Post();
        String content = "new post content";
        post.setContent(content);
        assertEquals(0L, post.getId());
        assertNotNull(post.getTags());
        assertTrue(post.getTags() instanceof ArrayList<Tag>);

        List<Tag> tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName("one");
        tagList.add(tag);
        tag = new Tag();
        tag.setName("two");
        tagList.add(tag);
        post.setTags(tagList);

        final Post p = post;

        assertThrows(IllegalStateException.class, () -> postRepo.add(p));
        assertTrue(post.getId() > 0L);

        post = null;
        post = new Post();
        post.setContent("new one post content");

        assertEquals(0L, post.getId());
        postRepo.add(post);
        assertTrue(post.getId() > 0L);

        assertThrows(IllegalArgumentException.class, () -> postRepo.add(null));

        post = new Post();
        post.setContent("another one post content");

        List<Tag> listTag = new ArrayList<>();
        tag = new Tag();
        tag.setId(555L);
        tag.setName("one");
        listTag.add(tag);
        post.setTags(listTag);
        final Post  p2 = post;
        assertThrows(PersistenceException.class, () -> postRepo.add(p2));
    }

    @Test
    @Order(20)
    void get() {
        assertThrows(NoResultException.class, () -> postRepo.get(null));
        assertThrows(NoResultException.class, () -> postRepo.get(Long.MAX_VALUE));
        assertThrows(NoResultException.class, () -> postRepo.get(Long.MIN_VALUE));
        assertThrows(NoResultException.class, () -> postRepo.get(0L));

        Post post = new Post();
        String postContent = "new one post content";
        post.setContent(postContent);
        postRepo.add(post);
        long objectId = post.getId();

        assertTrue(objectId > 0L);
        post = null;
        post = postRepo.get(objectId);

        assertEquals(objectId, post.getId());
        assertEquals(postContent, post.getContent());

    }

    @Test
    @Order(30)
    void update() {
        String postContent = "new one post content";
        Post post = new Post();
        post.setContent("regular post content");

        postRepo.add(post);
        long objectId = post.getId();

        post.setContent(postContent);
        postRepo.update(post);

        post = null;
        post = postRepo.get(objectId);
        assertEquals(postContent,post.getContent());
    }

    @Test
    @Order(40)
    void remove() {
        Post p = new Post();
        p.setContent("post content");
        postRepo.add(p);
        long objectId = p.getId();

        p = null;
        p = postRepo.get(objectId);

        postRepo.remove(objectId);

        assertThrows(NoResultException.class, () -> postRepo.get(objectId));
        assertThrows(IllegalArgumentException.class, () -> postRepo.remove(Long.MIN_VALUE));

    }

    @Test
    @Order(50)
    void getAll() {
        List<Post> posts = postRepo.getAll();
        int collectionSize = posts.size();

        Post post = new Post();
        post.setContent("new one content");
        postRepo.add(post);
        post = new Post();
        post.setContent("another new one");
        postRepo.add(post);

        posts = postRepo.getAll();
        int collectionSizeAfterPersist = posts.size();

        assertEquals(collectionSize + 2, collectionSizeAfterPersist);
    }

    @Test
    @Order(60)
    void containsId() {
        assertThrows(IllegalArgumentException.class, () -> postRepo.containsId(null));

        assertFalse(postRepo.containsId(Long.MAX_VALUE));
        assertFalse(postRepo.containsId(Long.MAX_VALUE - 1L));
        assertFalse(postRepo.containsId(Long.MIN_VALUE));
        assertFalse(postRepo.containsId(Long.MIN_VALUE + 1L));

        List<Long> idList = new ArrayList<>();

        Stream.of("one", "two", "three", "fourth")
                .forEach( v -> {
                    Post p;
                    p = new Post();
                    p.setContent("three");
                    postRepo.add(p);
                    idList.add(p.getId());
                });

        idList.forEach((id -> assertTrue(postRepo.containsId(id))));
        idList.forEach((id -> assertFalse(postRepo.containsId(id + 500L))));
    }

    @Test
    @Order(70)
    void deleteByStatus() {

        Post post = new Post();
        post.setContent("1");
        postRepo.add(post);
        post = new Post();
        post.setContent("2");
        postRepo.add(post);

        post = new Post();
        post.setContent("3");
        post.setPostStatus(PostStatus.DELETED);
        postRepo.add(post);
        post = new Post();
        post.setContent("4");
        post.setPostStatus(PostStatus.DELETED);
        postRepo.add(post);

        int collectionSizeBeforeFirstDelete = postRepo.getAll().size();
        postRepo.deleteByStatus(PostStatus.DELETED);
        int collectionSizeAfterFirstDelete = postRepo.getAll().size();
        postRepo.deleteByStatus(PostStatus.ACTIVE);
        int collectionSizeAfterSecondDelete = postRepo.getAll().size();

        assertTrue(collectionSizeBeforeFirstDelete > collectionSizeAfterFirstDelete);
        assertEquals(0L, collectionSizeAfterSecondDelete);
    }

}