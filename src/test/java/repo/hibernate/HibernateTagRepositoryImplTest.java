package repo.hibernate;


import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import model.Tag;
import org.junit.jupiter.api.*;
import repo.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Education purpose only
// Test shall be run with create-drop hibernate property.
// Tests shall be run in the described order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernateTagRepositoryImplTest {

    static TagRepository tagRepo = null;

    @BeforeAll
    static void set() {
        tagRepo = new HibernateTagRepositoryImpl();
    }

    @Test
    @Order(10)
    void add() {
        // Create new one tag
        Tag newTag = new Tag();
        String newTagName = "newTagName";
        newTag.setName(newTagName);
        assertEquals(0L, newTag.getId());
        tagRepo.add(newTag);
        assertTrue(newTag.getId() != 0L);
        // Attempt to persist object again
        assertThrows(PersistenceException.class, () -> {
            tagRepo.add(newTag);
        });
    }

    @Test
    @Order(20)
    void get() {
        // Create new one tag
        Tag tag = new Tag();
        String tagName = "tagName";
        tag.setName(tagName);
        assertEquals(0L, tag.getId());
        tagRepo.add(tag);
        long objectId = tag.getId();
        assertTrue(objectId > 0L);

        tag = null;
        assertNull(tag);

        tag = tagRepo.get(objectId);
        assertEquals(objectId, tag.getId());
        assertEquals(tagName, tag.getName());


        tag = null;
        assertNull(tag);

        tag = tagRepo.get(0L);
        assertNull(tag);

        tag = tagRepo.get(Long.MIN_VALUE);
        assertNull(tag);

        tag = tagRepo.get(Long.MAX_VALUE);
        assertNull(tag);

        tag = tagRepo.get(-1L);
        assertNull(tag);

        assertThrows(IllegalArgumentException.class, () -> {
            tagRepo.get(null);
        });

    }

    @Test
    @Order(30)
    void update() {
        Tag tag = new Tag();
        String tagName = "tagName";
        tag.setName(tagName);
        tagRepo.add(tag);
        long objectId = tag.getId();

        String updateTagName = "updateTagName";
        tag.setName(updateTagName);
        tagRepo.update(tag);
        assertEquals(updateTagName, tag.getName());

        tag = null;
        assertNull(tag);

        tag = tagRepo.get(objectId);
        assertEquals(updateTagName, tag.getName());

    }

    @Test
    @Order(40)
    void remove() {
        Tag tag = new Tag();
        tag.setName("string");

        tagRepo.add(tag);
        long objectId = tag.getId();

        tag = null;
        assertNull(tag);

        tag = tagRepo.get(objectId);
        assertNotNull(tag);

        tagRepo.remove(objectId);

        tag = null;
        tag = tagRepo.get(objectId);
        assertNull(tag);
    }

    @Test
    @Order(50)
    void getAll() {
        List<Tag> tagsBeforeInsert = tagRepo.getAll();
        int collectionSizeBeforeInsert = tagsBeforeInsert.size();

        Tag tag = new Tag();
        tag.setName("one");
        tagRepo.add(tag);
        tag = new Tag();
        tag.setName("two");
        tagRepo.add(tag);

        List<Tag> tagsAfterInsert = tagRepo.getAll();
        int collectionSizeAfterInsert = tagsAfterInsert.size();

        assertEquals(collectionSizeBeforeInsert + 2, collectionSizeAfterInsert);
    }

    @Test
    @Order(60)
    void getByName() {
        String tagName = "tagForSearch";
        Tag tag = new Tag();
        tag.setName(tagName);
        tagRepo.add(tag);

        tag = null;
        tag = tagRepo.getByName(tagName);

        assertEquals(tagName, tag.getName());

        assertThrows(NoResultException.class, () -> tagRepo.getByName(null));

        assertThrows(NoResultException.class, () -> tagRepo.getByName(""));

        assertThrows(NoResultException.class, () -> tagRepo.getByName("\n"));

    }

    @Test
    @Order(70)
    void containsId() {
        assertThrows(IllegalArgumentException.class, () -> tagRepo.containsId(null));

        assertFalse(tagRepo.containsId(Long.MIN_VALUE));
        assertFalse(tagRepo.containsId(Long.MIN_VALUE + 1L));
        assertFalse(tagRepo.containsId(Long.MIN_VALUE + 2L));
        assertFalse(tagRepo.containsId(Long.MIN_VALUE + 3L));
        assertFalse(tagRepo.containsId(Long.MAX_VALUE));

        List<Long> idList = new ArrayList<>();

        Tag tag = new Tag();
        tag.setName("one");
        tagRepo.add(tag);
        idList.add(tag.getId());

        tag = new Tag();
        tag.setName("two");
        tagRepo.add(tag);
        idList.add(tag.getId());

        idList.forEach((id) -> assertTrue(tagRepo.containsId(id)));
        idList.forEach((id) -> assertFalse(tagRepo.containsId(id + 500L)));
    }

    @Test
    @Order(80)
    void nameContains() {
        assertFalse(tagRepo.nameContains(null));

        List<String> tagsNames = List.of("Firsts", "Second","Third","Fourth", "Fifth");

        tagsNames.forEach((name) -> {
            Tag tag = new Tag();
            tag.setName(name);
            tagRepo.add(tag);
        });

        tagsNames.forEach((name) -> {
            assertTrue(tagRepo.nameContains(name));
            assertFalse(tagRepo.nameContains(name.substring(1,3)));
        });
    }
}