package no.kantega.utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-01
 */
public class DefaultQueryBuilderTest {

    @Test
    public void thatParametersAreAdded() {
        String expected = "greeting=Hei&name=Navn&greeting=Hello&greeting=Hola";
        String actual = Http.query("greeting=Hei&name=Navn", "UTF-8")
                .add("greeting", "Hello")
                .add("greeting", "Hola")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void thatParametersAreReplaced() {
        String expected = "name=Navn&greeting=Hola";
        String actual = Http.query("greeting=Hei&name=Navn", "UTF-8")
                .add("greeting", "Hello")
                .put("greeting", "Hola")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void thatParametersAreEncoded() {
        String expected = "name%26=%26Navn&greeting%26=%26Hola";
        String actual = Http.query("greeting%26=%26Hei&name%26=%26Navn", "UTF-8")
                .add("greeting&", "&Hello")
                .put("greeting&", "&Hola")
                .build();
        assertEquals(expected, actual);
    }
}
