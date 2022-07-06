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

    private static TagRepository tR = mock(TagRepository.class);;
    private static PostRepository pR = mock(PostRepository.class);;
    private static WriterRepository wR =  mock(WriterRepository.class);
    static WriterService writerService = new WriterService(tR,pR,wR);

    @BeforeAll
    static void set() {

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

    }

    private Writer getWriter() {
        Writer w = new Writer();
        w.setName("new name");
        return w;
    }

    @Test
    void add() {
        writerService.add(getWriter());
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
        Writer w = getWriter();
        writerService.update(w);
        verify(wR).update(w);
    }

    @Test
    void remove() {
        writerService.remove(null);

        LongStream.range(1, 500).forEach(writerService::remove);

        verify(wR,times(499)).remove(isNotNull());
    }

    @Test
    void containsId() {
        writerService.containsId(null);

        LongStream.range(1, 500).forEach(writerService::containsId);

        verify(wR,times(499)).containsId(isNotNull());
    }

    @Test
    void nameContains() {
        writerService.nameContains(null);

        String name = "new name";
        writerService.nameContains(name);

        verify(wR, times(1)).nameContains(name);
    }

    @Test
    void getByName() {
        writerService.getByName(null);

        String name = "new name";
        writerService.getByName(name);

        verify(wR, times(1)).getByName(name);
    }

}