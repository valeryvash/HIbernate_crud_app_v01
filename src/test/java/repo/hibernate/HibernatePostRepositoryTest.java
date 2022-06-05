package repo.hibernate;

import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import model.Post;
import model.PostStatus;
import model.Tag;
import org.junit.jupiter.api.*;
import repo.PostRepository;
import util.EntityPrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernatePostRepositoryTest {


    static PostRepository postRepo = null;
    static long entityId = 0L;
    static String postContent = null;
    static String newPostContent = null;
    static List<Tag> tagList = null;
    static Tag firstTag = null;
    static Tag secondTag = null;
    static Tag thirdTag = null;

    @BeforeEach
    void set() {
        postRepo = new HibernatePostRepository();
        postContent = "Regular post content";
        newPostContent = "New post content";
        firstTag = new Tag();
        firstTag.setName("firstTag");
        secondTag = new Tag();
        secondTag.setName("secondTag");
        thirdTag = new Tag();
        thirdTag.setName("thirdTag");
        tagList = new ArrayList<>();
        tagList.add(firstTag);
        tagList.add(secondTag);
        tagList.add(thirdTag);
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Order(10)
    void add() {
        
        // Persist new post entity with null content cause the Persistence exception
        assertThrows(PersistenceException.class,()->postRepo.add( new Post()));
        // Persist new post entity with null tags list
        Post p = new Post();
        p.setContent(postContent);
        postRepo.add(p);
        assertTrue(p.getId() > 0L);
        // Persist new post entity with empty tags list
        p = new Post();
        p.setContent(postContent);
        p.setTags(new ArrayList<>());
        postRepo.add(p);
        assertTrue(p.getId() > 0L);
        // Persist new post entity detached tag entity cause the persistence exception
        p = new Post();
        p.setContent(postContent);
        thirdTag.setId(5L);
        p.setTags(tagList);
        Post finalPost = p;
        assertThrows(PersistenceException.class, () -> postRepo.add(finalPost));
        // Cause Persistence exception during persist the object with non-zero id (detached entity)
        p = new Post();
        p.setId(5L);
        p.setContent(postContent);
        Post secondFinalPost = p;
        assertThrows(PersistenceException.class, () -> postRepo.add(secondFinalPost));
        // Null method argument cause null pointer exception
        assertThrows(NullPointerException.class, () -> postRepo.add(null));
    }

    @Test
    @Order(20)
    void get() {
        // Persist the new post entity
        Post p = new Post();
        p.setContent(postContent);
        p.setTags(tagList);
        assertEquals(0L, p.getId());
        postRepo.add(p);
        long objectId = p.getId();
        assertTrue(objectId > 0L);
        // new Post entity will be returned for null method argument
        p = postRepo.get(null);
        assertEquals(0L, p.getId());
        assertNull(p.getContent());
        assertNull(p.getTags());
        assertEquals(PostStatus.ACTIVE, p.getPostStatus());
        // Get the object and check it properties
        p = postRepo.get(objectId);
        assertNotNull(p);
        assertEquals(objectId, p.getId());
        assertEquals(postContent,p.getContent());
        List<Tag> tagsList = p.getTags();
        assertNotNull(tagsList);
        assertEquals(3, tagsList.size());
    }

    @Test
    @Order(30)
    void update() {
        // Persist a new object
        Post p = new Post();
        p.setContent(postContent);
        p.setTags(tagList);
        assertEquals(0L, p.getId());
        postRepo.add(p);
        // Check that the object persisted
        long objectId = p.getId();
        assertTrue(objectId > 0L);
        // Update the object content and status properties and persist it
        p.setContent(newPostContent);
        p.setPostStatus(PostStatus.DELETED);
        postRepo.update(p);
        assertEquals(objectId, p.getId());
        assertEquals(p.getContent(), newPostContent);
        assertEquals(p.getPostStatus(), PostStatus.DELETED);
        // Get persisted object and check updated content property
        p = postRepo.get(objectId);
        assertNotNull(p);
        assertEquals(objectId, p.getId());
        assertNotEquals(p.getContent(), postContent);
        assertEquals(p.getContent(), newPostContent);
        // Change the tag list: add a new tag
        String newTagName = "NewTagName";
        Tag newTag = new Tag();
        newTag.setName(newTagName);
        assertEquals(p.getTags().size(), 3);
        p.getTags().add(newTag);
        assertEquals(p.getTags().size(), 4);
        postRepo.update(p);
        p = postRepo.get(objectId);
        assertEquals(p.getTags().size(), 4);
        // Change the tag list : delete one tag
        p.getTags().remove(3);
        assertEquals(p.getTags().size(), 3);
        postRepo.update(p);
        p = postRepo.get(objectId);
        assertEquals(p.getId(), objectId);
        assertEquals(p.getTags().size(), 3L);
    }

    @Test
    @Order(40)
    void remove() {
        // Create new entity
        Post post = new Post();
        post.setContent("New one post");
        post.setPostStatus(PostStatus.ACTIVE);
        // Create new one tag
        Tag tag = new Tag();
        tag.setName("New one tag");
        post.setTags(new ArrayList<>());
        post.getTags().add(tag);
        // Persist entity
        postRepo.add(post);
        entityId = post.getId();
        // Get persisted entity. Works good
        Post postForDelete = postRepo.get(entityId);
        // Remove entity from persistence context
        postRepo.remove(entityId);
        // Attempt to get entity outside persisted context throws the NoResultException
        assertThrows(NoResultException.class,() -> postRepo.get(entityId));
    }

    @Test
    @Order(50)
    void getAll() {
        // Check repository w/o any persisted content
        List<Post> posts = postRepo.getAll();
        assertNotNull(posts);
        System.out.println(posts.size());
        assertTrue(posts.isEmpty());
        // Add few entities to persistent context
        List<Post> postsForPersistedContext = new ArrayList<>();
        // First post
        Post post = new Post();
        post.setContent("first post");
        post.setPostStatus(PostStatus.ACTIVE);
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag());
        tags.add(new Tag());
        tags.add(new Tag());
        tags.get(0).setName("\"first post\" 1st tag");
        tags.get(1).setName("\"first post\" 2nd tag");
        tags.get(2).setName("\"first post\" 3rd tag");
        post.setTags(tags);
        // Second post
        post = new Post();
        post.setContent("second post");
        post.setPostStatus(PostStatus.DELETED);
        tags = new ArrayList<>();
        tags.add(new Tag());
        tags.add(new Tag());
        tags.add(new Tag());
        tags.get(0).setName("\"second post\" 1st tag");
        tags.get(1).setName("\"second post\" 2nd tag");
        tags.get(2).setName("\"second post\" 3rd tag");
        post.setTags(tags);
        posts.add(post);
        // Persist posts
        posts.forEach(postRepo::add);
        posts.forEach(p -> {
            if (p.getId() <= 0L) {
                throw new PersistenceException();
            }
        });
        boolean tagNotSaved =
        posts.stream().flatMap(post1 -> post1.getTags().stream())
                .anyMatch(t -> t.getId() == 0L);
        assertFalse(tagNotSaved);
    }

    @Test
    @Order(60)
    void containsId() {
        // Check object with improper id
        Post p = postRepo.get(-1L);
        assertEquals(0L, p.getId());
        assertFalse(postRepo.containsId(Long.MAX_VALUE));
        assertFalse(postRepo.containsId(Long.MIN_VALUE));
        assertFalse(postRepo.containsId(0L));
        assertFalse(postRepo.containsId(-1L));

        // Create the object
        p = new Post();
        p.setContent("ContainsId method check");
        p.setPostStatus(PostStatus.DELETED);
        postRepo.add(p);
        long objectId = p.getId();
        assertTrue(objectId > 0L);
        p = postRepo.get(objectId);
        assertNotNull(p);
        assertEquals( objectId, p.getId());
    }

    @Test
    @Order(70)
    void deleteByStatus() {
    }

    @Test
    @Order(80)
    void getPostsForWriter() {
    }

    @Test
    @Order(90)
    void getWriterId() {
    }
}