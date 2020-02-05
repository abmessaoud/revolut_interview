package com.revolut.payments.core;

import com.revolut.payments.dto.RequestDTO;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequestParserTest {
    private RequestParser parser;
    private HttpExchange exchange;

    @Before
    public void setUp() throws IOException {
        parser = new RequestParser();
        exchange = Mockito.mock(HttpExchange.class);
        Mockito.doReturn("get").when(exchange).getRequestMethod();
        Mockito.doReturn(URI.create("http://localhost:8000/test")).when(exchange).getRequestURI();
        final InputStream stream = Mockito.mock(InputStream.class);
        Mockito.doReturn(stream).when(exchange).getRequestBody();
        Mockito.when(stream.read()).thenReturn(-1);
    }

    @Test
    public void testParseUrlParam_whenPathHasManyNumericSections_thenReturnsFirstNumericSectionString() {
        final URI uriMock = URI.create("http://localhost:8000/test/1/hello/2/world/3");
        Mockito.doReturn(uriMock).when(exchange).getRequestURI();

        final RequestDTO response = parser.parse(exchange);

        assertEquals("1", response.getUrlParam());
    }

    @Test
    public void testParseUrlParam_whenPathHasOneNumericSection_thenReturnsNumericString() {
        final URI uriMock = URI.create("http://localhost:8000/test/1");
        Mockito.doReturn(uriMock).when(exchange).getRequestURI();

        final RequestDTO response = parser.parse(exchange);

        assertEquals("1", response.getUrlParam());
    }

    @Test
    public void testParseUrlParam_whenPathHasNotNumericSection_thenReturnsEmptyString() {

        final RequestDTO response = parser.parse(exchange);

        assertTrue(response.getUrlParam().isEmpty());
    }

    @Test
    public void testParseBodyParams_whenStreamContainsChars_thenReturnsFullMap() {
        final InputStream stream = new ByteArrayInputStream("{\"test\":\"value\"}".getBytes());
        Mockito.doReturn(stream).when(exchange).getRequestBody();

        final RequestDTO response = parser.parse(exchange);

        assertTrue(response.hasBodyParam("test"));
        assertEquals("value", response.getBodyParam("test"));
    }

    @Test
    public void testParseBodyParams_whenStreamThrowsIOException_thenReturnsEmptyMap() throws IOException {
        final InputStream stream = Mockito.mock(InputStream.class);
        Mockito.when(stream.read()).thenThrow(new IOException());
        Mockito.when(exchange.getRequestBody()).thenReturn(stream);

        final RequestDTO response = parser.parse(exchange);

        assertFalse(response.hasBodyParam("test"));
    }

    @Test
    public void testParseBodyParams_whenStreamIsEmpty_thenReturnsEmptyMap() throws IOException {
        final InputStream stream = Mockito.mock(InputStream.class);
        Mockito.when(stream.read()).thenReturn(-1);
        Mockito.when(exchange.getRequestBody()).thenReturn(stream);

        final RequestDTO response = parser.parse(exchange);

        assertFalse(response.hasBodyParam("test"));
    }

    @Test
    public void testParseQueryParams_whenQueryHasManyParams_thenReturnFullMap() {
        final URI uriMock = URI.create("http://localhost:8000/test?hello=world&test=value&execution=true");
        Mockito.doReturn(uriMock).when(exchange).getRequestURI();

        final RequestDTO response = parser.parse(exchange);

        assertTrue(response.hasQueryParam("hello"));
        assertEquals("world", response.getQueryParam("hello"));
        assertTrue(response.hasQueryParam("test"));
        assertEquals("value", response.getQueryParam("test"));
        assertTrue(response.hasQueryParam("execution"));
        assertEquals("true", response.getQueryParam("execution"));
    }

    @Test
    public void testParseQueryParams_whenQueryHasOneParam_thenReturnsSingletonMap() {
        final URI uriMock = URI.create("http://localhost:8000/test?hello=world");
        Mockito.doReturn(uriMock).when(exchange).getRequestURI();

        final RequestDTO response = parser.parse(exchange);

        assertTrue(response.hasQueryParam("hello"));
        assertEquals("world", response.getQueryParam("hello"));
    }

    @Test
    public void testParseQueryParams_whenQueryIsEmpty_thenReturnsEmptyMap() {

        final RequestDTO response = parser.parse(exchange);

        assertFalse(response.hasQueryParam("test"));
    }
}