package service;

import model.Writer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repo.WriterRepository;

import java.util.stream.LongStream;

import static org.mockito.Mockito.*;
class WriterServiceTest {

    private static WriterRepository wR = mock(WriterRepository.class);
    static WriterService writerService = new WriterService(wR);

    @BeforeAll
    static void set() {
        doThrow(NullPointerException.class).when(wR).add(isNull());
        doThrow(NullPointerException.class).when(wR).get(isNull());
        doThrow(NullPointerException.class).when(wR).update(isNull());
        doThrow(NullPointerException.class).when(wR).remove(isNull());

        doThrow(NullPointerException.class).when(wR).containsId(isNull());

        doThrow(NullPointerException.class).when(wR).nameContains(isNull());
        doThrow(NullPointerException.class).when(wR).getByName(isNull());
    }

    private Writer getWriter() {

        Writer w = new Writer();
        w.setName("new name");
        return w;
    }

    @Test
    @DisplayName("verify that object passed to a repository level without ")
    void add() {
        Writer w = getWriter();
        writerService.add(w);
        verify(wR).add(w);
    }

    @Test
    @DisplayName("verify that null argument doesn't pass to a repository level")
    void nullCheck(){
        writerService.add(null);
        writerService.get(null);
        writerService.update(null);
        writerService.remove(null);
        writerService.nameContains(null);
        writerService.getByName(null);
    }

    @Test
    @DisplayName("verify that id and string arguments passed to a repository level")
    void verifyThatIdArgumentsPassedToRepositoryLevel() {
        LongStream.range(1L,1001L).forEach(
                id -> {
                    writerService.get(id);
                    writerService.remove(id);
                    writerService.nameContains(String.valueOf(id));
                    writerService.getByName(String.valueOf(id));
                    writerService.getAll();
                }
        );

        verify(wR, times(1000)).get(isA(Long.class));
        verify(wR, times(1000)).remove(isA(Long.class));
        verify(wR, times(1000)).nameContains(isA(String.class));
        verify(wR, times(1000)).getByName(isA(String.class));
        verify(wR, times(1000)).getAll();
    }

    @Test
    void update() {
        writerService.update(null);
        Writer w = getWriter();
        writerService.update(w);
        verify(wR).update(w);
    }


    @Test
    void nameNullContains() {
        writerService.nameContains(null);

        verify(wR,times(0)).containsId(null);
    }

}