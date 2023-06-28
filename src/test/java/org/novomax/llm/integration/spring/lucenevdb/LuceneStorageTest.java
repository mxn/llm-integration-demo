package org.novomax.llm.integration.spring.lucenevdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.novomax.llm.integration.spring.SearchResult;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LuceneStorageTest {

    private LuceneStorage testee;

    @BeforeEach
    void setUp() throws IOException {
        String pathStr = Files.createTempDirectory("temp").toAbsolutePath().toString();
        this.testee = new LuceneStorage(pathStr);
    }

    @Test
    void test() {
        float[] arr1 = genFloatArray(1024);
        String id1 = "123435";
        testee.upcert("entity", id1, arr1);
        testee.upcert("entity", "234556", genFloatArray(1024));

        SearchResult result = testee.search(arr1, 1).get(0);
        assertEquals(id1, result.entityId());
        assertEquals("entity", result.entityClass());

    }

    private float[] genFloatArray(final int dim) {
        float[] result = new float[dim];
        IntStream.range(0, dim).forEach(i -> result[i] = (float) Math.random());
        return result;
    }
}