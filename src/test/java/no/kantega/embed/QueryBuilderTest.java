package no.kantega.embed;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class QueryBuilderTest {

    @Test
    public void thatParametersAreAdded() {
        String expected = "greeting=Hei&name=Navn&greeting=Hello&greeting=Hola";
        String actual = QueryBuilder.forQuery("greeting=Hei&name=Navn", "UTF-8")
                .add("greeting", "Hello")
                .add("greeting", "Hola")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void thatParametersAreReplaced() {
        String expected = "name=Navn&greeting=Hola";
        String actual = QueryBuilder.forQuery("greeting=Hei&name=Navn", "UTF-8")
                .add("greeting", "Hello")
                .put("greeting", "Hola")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void thatParametersAreEncoded() {
        String expected = "name%26=%26Navn&greeting%26=%26Hola";
        String actual = QueryBuilder.forQuery("greeting%26=%26Hei&name%26=%26Navn", "UTF-8")
                .add("greeting&", "&Hello")
                .put("greeting&", "&Hola")
                .build();
        assertEquals(expected, actual);
    }
}
