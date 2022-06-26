package repo.hibernate;

import jakarta.persistence.NoResultException;
import model.Post;
import model.Writer;
import org.junit.jupiter.api.*;
import repo.WriterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Education purpose only
// Test shall be run with create-drop hibernate property.
// Tests shall be run in the described order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernateWriterRepositoryImplTest {

    static WriterRepository writerRepo = null;

    @BeforeAll
    static void set() {
        writerRepo = new HibernateWriterRepositoryImpl();
    }


    @Test
    @Order(10)
    void add() {
        Writer writer = new Writer();
        String name = "new one name";
        writer.setName(name);

        assertEquals(0L, writer.getId());
        assertNotNull(writer.getPosts());
        assertTrue(writer.getPosts() instanceof ArrayList<Post>);

        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setContent("new one content");
        posts.add(post);
        writer.setPosts(posts);

        final Writer w = writer;

        assertThrows(IllegalStateException.class, () -> writerRepo.add(w));
        assertTrue(writer.getId() > 0L);

        writer = null;
        writer = new Writer();
        writer.setName("new one writer name here");

        assertEquals(0L, writer.getId());
        writerRepo.add(writer);
        assertTrue(writer.getId() > 0L);

        assertThrows(IllegalArgumentException.class, () -> writerRepo.add(null));
    }

    @Test
    @Order(20)
    void get() {
        assertThrows(NoResultException.class, () -> writerRepo.get(null));
        assertThrows(NoResultException.class, () -> writerRepo.get(Long.MAX_VALUE));
        assertThrows(NoResultException.class, () -> writerRepo.get(Long.MIN_VALUE));
        assertThrows(NoResultException.class, () -> writerRepo.get(0L));

        Writer writer = new Writer();
        String writerName = "new one name";
        writer.setName(writerName);
        writerRepo.add(writer);
        long objectId = writer.getId();

        assertTrue(objectId > 0L);
        writer = null;
        writer = writerRepo.get(objectId);

        assertEquals(objectId, writer.getId());
        assertEquals(writerName, writer.getName());

    }

    @Test
    @Order(30)
    void update() {
        String writerName = "new one writer name";
        Writer writer = new Writer();
        writer.setName("regular writer name");

        writerRepo.add(writer);
        long objectId = writer.getId();

        writer.setName(writerName);
        writerRepo.update(writer);

        writer = null;
        writer = writerRepo.get(objectId);
        assertEquals(writerName, writer.getName());
    }

    @Test
    @Order(40)
    void remove() {
        Writer w = new Writer();
        w.setName("Regular Writer");
        writerRepo.add(w);
        long objectId = w.getId();

        w = null;
        w = writerRepo.get(objectId);

        writerRepo.remove(objectId);

        assertThrows(NoResultException.class, () -> writerRepo.get(objectId));
        assertThrows(IllegalArgumentException.class, () -> writerRepo.remove(Long.MIN_VALUE));

    }

    @Test
    @Order(50)
    void getAll() {
        List<Writer> writers = writerRepo.getAll();
        int collectionSize = writers.size();

        Writer writer = new Writer();
        writer.setName("new one writer");
        writerRepo.add(writer);

        writer = new Writer();
        writer.setName("another one writer");
        writerRepo.add(writer);

        writers = writerRepo.getAll();
        int collectionAfterPersist = writers.size();
        assertEquals(collectionSize + 2, collectionAfterPersist);
    }


    @Test
    @Order(60)
    void containsId() {
        assertThrows(IllegalArgumentException.class, () -> writerRepo.containsId(null));

        assertFalse(writerRepo.containsId(Long.MAX_VALUE));
        assertFalse(writerRepo.containsId(Long.MAX_VALUE - 1L));
        assertFalse(writerRepo.containsId(Long.MIN_VALUE));
        assertFalse(writerRepo.containsId(Long.MIN_VALUE + 1L));

        List<Long> idList = new ArrayList<>();

        Stream.of("one","two","three")
                .forEach(v -> {
                    Writer w;
                    w = new Writer();
                    w.setName(v);
                    writerRepo.add(w);
                    idList.add(w.getId());
        });

        idList.forEach(id -> assertTrue(writerRepo.containsId(id)));
        idList.forEach(id -> assertFalse(writerRepo.containsId(id + 500L)));

    }

    @Test
    @Order(70)
    void nameContains() {
        assertFalse(writerRepo.nameContains(null));

        List<String> names = List.of("first", "second", "third", "fourth", "fifth");

        names.forEach((name) -> {
            Writer w;
            w = new Writer();
            w.setName(name);
            writerRepo.add(w);
        });

        names.forEach( name -> {
            assertTrue(writerRepo.nameContains(name));
            assertFalse(writerRepo.nameContains(name.substring(1,3)));
        });

    }

    @Test
    @Order(80)
    void getByName() {
        String writerName = "writer new name";
        Writer w = new Writer();
        w.setName(writerName);
        writerRepo.add(w);

        w = null;
        w = writerRepo.getByName(writerName);
        assertEquals(writerName, w.getName());

        assertThrows(NoResultException.class, () -> writerRepo.getByName(null));
        assertThrows(NoResultException.class, () -> writerRepo.getByName(""));
        assertThrows(NoResultException.class, () -> writerRepo.getByName("\n"));
    }
}