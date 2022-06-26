package service;

import model.Post;
import model.Tag;
import model.Writer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import repo.GenericRepository;
import repo.PostRepository;
import repo.TagRepository;
import repo.WriterRepository;
import util.EntityPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class WriterServiceTest {

    private static TagRepository tR = null;
    private static PostRepository pR = null;
    private static WriterRepository wR = null;
    static WriterService writerService = null;

    @BeforeAll
    static void set() {
        tR = mock(TagRepository.class);
        pR = mock(PostRepository.class);
        wR = mock(WriterRepository.class);

        Stream.of(wR, pR, tR).forEach(repo -> {
            doThrow(NullPointerException.class).when(repo).add(isNull());
            doThrow(NullPointerException.class).when(repo).get(isNull());
            doThrow(NullPointerException.class).when(repo).update(isNull());
            doThrow(NullPointerException.class).when(repo).remove(isNull());
        });

        doThrow(NullPointerException.class).when(wR).containsId(isNull());
        doThrow(NullPointerException.class).when(pR).containsId(isNull());
        doThrow(NullPointerException.class).when(tR).containsId(isNull());

        doThrow(NullPointerException.class).when(wR).nameContains(isNull());
        doThrow(NullPointerException.class).when(wR).getByName(isNull());

        doThrow(NullPointerException.class).when(tR).nameContains(isNull());
        doThrow(NullPointerException.class).when(tR).getByName(isNull());

        doThrow(NullPointerException.class).when(pR).deleteByStatus(isNull());

//         TODO: find out how to check object properties and throws exceptions
//        doThrow(IllegalArgumentException.class).when(wR).add(any(Writer.class));

        writerService = new WriterService(tR,pR,wR);
    }

    @Test
    void add() {
        writerService.add(null);

        Writer w = new Writer();
        w.setName("new one name");

        writerService.add(w);
        verify(wR).add(argThat(writer -> writer.getId() == 0L));
    }

    @Test
    void get() {
        writerService.get(null);

        writerService.get(500L);
    }

    @Test
    void update() {
        writerService.update(null);

        writerService.update(new Writer());
    }

    @Test
    void remove() {
        writerService.remove(null);

        LongStream.range(1, 500).forEach(writerService::remove);
    }

    @Test
    void containsId() {
        writerService.containsId(null);
    }

    @Test
    void nameContains() {
        writerService.nameContains(null);
    }

    @Test
    void getByName() {
        writerService.getByName(null);
    }

}