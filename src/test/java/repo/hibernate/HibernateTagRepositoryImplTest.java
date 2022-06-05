package repo.hibernate;


import jakarta.persistence.PersistenceException;
import model.Tag;
import org.junit.jupiter.api.*;
import repo.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Education purpose only
// Test shall be run with create-drop hibernate property. Test
// Tests shall be run in the described order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernateTagRepositoryImplTest {

    static TagRepository tagRepo = null;
    static long entityId = 0L;
    static String tagName = null;
    static String newTagName = null;

    @BeforeAll
    static void set() {
        tagRepo = new HibernateTagRepositoryImpl();
        tagName = "Tag name";
        newTagName = "A new one tag name";
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
        // Create a new tag entity
        Tag tag = new Tag();
        // Check that entity is created properly
        assertEquals(tag.getId(), 0L);
        assertNull(tag.getName());
        // Set up new name for the entity
        tag.setName(tagName);
        assertEquals(tagName, tag.getName());
        // Persist an entity
        tagRepo.add(tag);
        // Check the entity is persisted
        assertTrue(tag.getId() > 0L);
        // Entity is in detached state
        assertThrows(PersistenceException.class, () -> tagRepo.add(tag));

        entityId = tag.getId();
    }

    @Test
    @Order(20)
    void get() {
        // Check that entity added to the database
        assertTrue(entityId > 0L);
        // Get the persisted entity
        Tag tag = tagRepo.get(entityId);
        // Check the entity values
        assertNotNull(tag);
        assertEquals(tag.getId(), entityId);
        assertEquals(tag.getName(), tagName);
    }

    @Test
    @Order(30)
    void update() {
        // Get the persisted entity
        Tag tag = tagRepo.get(entityId);
        // Check this property values
        assertNotNull(tag);
        assertEquals(tag.getId(), entityId);
        assertEquals(tag.getName(), tagName);
        // Change entity name
        tag.setName(newTagName);
        assertNotEquals(tag.getName(), tagName);
        assertEquals(tag.getName(), newTagName);
        // Update detached entity
        tagRepo.update(tag);
        // Check that entity doesn't change
        assertEquals(tag.getId(), entityId);
        assertEquals(tag.getName(), newTagName);
        // Check entity persistence
        Tag updatedTag = tagRepo.get(entityId);
        assertNotNull(updatedTag);
        assertEquals(updatedTag.getId(), entityId);
        assertEquals(updatedTag.getName(), newTagName);
    }

    @Test
    @Order(40)
    void remove() {
        // Get persisted entity
        Tag tagForDelete = tagRepo.get(entityId);
        // Check the entity property values
        assertNotNull(tagForDelete);
        assertEquals(tagForDelete.getId(), entityId);
        assertEquals(tagForDelete.getName(), newTagName);
        // Deletion the entity
        tagRepo.remove(tagForDelete.getId());
        tagForDelete = null;
        tagForDelete = tagRepo.get(entityId);
        assertNull(tagForDelete);

    }

    @Test
    @Order(50)
    void getAll() {
        // Create link
        List<Tag> tags = null;
        // Check that link is null
        assertNull(tags);
        // Get all data from repository
        tags = tagRepo.getAll();
        assertTrue(tags.isEmpty());
        //
        Tag t = new Tag();
        t.setName("first");
        tagRepo.add(t);
        t = new Tag();
        t.setName("second");
        tagRepo.add(t);
        t = new Tag();
        t.setName("third");
        tagRepo.add(t);
        tags = null;
        assertNull(tags);

        tags = tagRepo.getAll();
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
        assertTrue(tags.size() == 3);
    }

    @Test
    @Order(60)
    void getByName() {
        // Check that object doesn't contain in database
        String tagName = "TagName";
        Tag t = null;
        assertNull(t);
        t = tagRepo.getByName(tagName);
        assertEquals(0L, t.getId());
        assertNull(t.getName());
        // Create a new object with existed name
        t = new Tag();
        t.setName(tagName);
        // Add object to the database
        tagRepo.add(t);
        long objectId = t.getId();
        assertTrue(objectId != 0L);
        assertEquals(t.getName(), tagName);
        // Get tag by its name
        t = tagRepo.getByName(tagName);
        // Check object properties
        assertEquals(objectId, t.getId());
        assertEquals(tagName, t.getName());
    }

    @Test
    @Order(70)
    void containsId() {
        // Create an object
        Tag t = new Tag();
        t.setName("New name");
        // Save the object
        tagRepo.add(t);
        // Get and check object id
        long objectId = t.getId();
        assertTrue(objectId > 0L);
        // Long.MAX_VALUE and MIN_VALUE doesn't contain in database
        assertFalse(tagRepo.containsId(Long.MAX_VALUE));
        assertFalse(tagRepo.containsId(Long.MIN_VALUE));
        // Object that we added previously shall be in the database
        assertTrue(tagRepo.containsId(objectId));
    }

    @Test
    @Order(80)
    void nameContains() {
        // Check that specific name doesn't contain in Tag name
        String tagName = "LLolZ";
        assertFalse(tagRepo.nameContains(tagName));
        // Create an object and check its name
        Tag t = new Tag();
        t.setName(tagName);
        tagRepo.add(t);
        assertTrue(tagRepo.nameContains(tagName));
        // Delete the object from database
        tagRepo.remove(t.getId());
        // Check that name doesn't contain in database yet
        assertFalse(tagRepo.nameContains(tagName));
    }
}