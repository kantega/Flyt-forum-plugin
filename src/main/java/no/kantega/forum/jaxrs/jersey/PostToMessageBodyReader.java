package no.kantega.forum.jaxrs.jersey;

import no.kantega.forum.jaxrs.bol.Fault;
import no.kantega.forum.jaxrs.tol.AttachmentTo;
import no.kantega.forum.jaxrs.tol.PostTo;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-25
 */
@Provider
@Consumes("multipart/form-data")
public class PostToMessageBodyReader implements MessageBodyReader<PostTo> {

    /**
     * HTTP content disposition header name.
     */
    public static final String CONTENT_DISPOSITION = "Content-disposition";

    /**
     * Content-disposition value for form data.
     */
    public static final String FORM_DATA = "form-data";


    /**
     * Content-disposition value for file attachment.
     */
    public static final String ATTACHMENT = "attachment";


    /**
     * Part of HTTP content type header.
     */
    public static final String MULTIPART = "multipart/";

    private static final Logger LOG = LoggerFactory.getLogger(PostToMessageBodyReader.class);

    @Context
    private HttpServletRequest request;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (PostTo.class.equals(type)) {
            return isMultipartContent(request);
        }
        return false;
    }

    @Override
    public PostTo readFrom(Class<PostTo> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try {
            PostTo postTo = new PostTo();
            postTo.setSubject(request.getParameter("subject"));
            postTo.setBody(request.getParameter("body"));
            Collection<Part> parts = null;
            try {
                parts = request.getParts();
            } catch (Exception cause){
                LOG.error("Could not get parts", cause);
                throw new Fault(500, cause);
            }

            if (parts != null) {
                for (Part part : parts) {
                    if (ATTACHMENT.equals(part.getName())) {
                        if (postTo.getAttachments() == null) {
                            postTo.setAttachments(new ArrayList<AttachmentTo>());
                        }
                        AttachmentTo attachmentTo = new AttachmentTo();
                        attachmentTo.setCreated(Instant.now());
                        attachmentTo.setData(part.getInputStream());
                        attachmentTo.setFileName(getFileName(part.getHeader(CONTENT_DISPOSITION)));
                        attachmentTo.setMimeType(part.getContentType());
                        attachmentTo.setFileSize(part.getSize());
                        postTo.getAttachments().add(attachmentTo);
                    }
                }
            }
            return postTo;
        } catch (IOException cause) {
            throw new Fault(500, cause);
        }
    }

    /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

    /**
     * Returns the given content-disposition headers file name.
     * @param pContentDisposition The content-disposition headers value.
     * @return The file name
     */
    private String getFileName(String pContentDisposition) {
        String fileName = null;
        if (pContentDisposition != null) {
            String cdl = pContentDisposition.toLowerCase();
            if (cdl.startsWith(FORM_DATA) || cdl.startsWith(ATTACHMENT)) {
                ParameterParser parser = new ParameterParser();
                parser.setLowerCaseNames(true);
                // Parameter parser can handle null input
                Map params = parser.parse(pContentDisposition, ';');
                if (params.containsKey("filename")) {
                    fileName = (String) params.get("filename");
                    if (fileName != null) {
                        fileName = fileName.trim();
                    } else {
                        // Even if there is no value, the parameter is present,
                        // so we return an empty file name rather than no file
                        // name.
                        fileName = "";
                    }
                }
            }
        }
        return fileName;
    }

    /**
     * <p>Utility method that determines whether the request contains multipart
     * content.</p>
     *
     * <p><strong>NOTE:</strong>This method will be moved to the
     * <code>ServletFileUpload</code> class after the FileUpload 1.1 release.
     * Unfortunately, since this method is static, it is not possible to
     * provide its replacement until this method is removed.</p>
     *
     * @param request The request context to be evaluated. Must be non-null.
     *
     * @return <code>true</code> if the request is multipart;
     *         <code>false</code> otherwise.
     */
    public static final boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }
        return false;
    }

    /**
     * A simple parser intended to parse sequences of name/value pairs.
     * Parameter values are exptected to be enclosed in quotes if they
     * contain unsafe characters, such as '=' characters or separators.
     * Parameter values are optional and can be omitted.
     *
     * <p>
     *  <code>param1 = value; param2 = "anything goes; really"; param3</code>
     * </p>
     *
     * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
     */

    public class ParameterParser {
        /**
         * String to be parsed.
         */
        private char[] chars = null;

        /**
         * Current position in the string.
         */
        private int pos = 0;

        /**
         * Maximum position in the string.
         */
        private int len = 0;

        /**
         * Start of a token.
         */
        private int i1 = 0;

        /**
         * End of a token.
         */
        private int i2 = 0;

        /**
         * Whether names stored in the map should be converted to lower case.
         */
        private boolean lowerCaseNames = false;

        /**
         * Default ParameterParser constructor.
         */
        public ParameterParser() {
            super();
        }

        /**
         * Are there any characters left to parse?
         *
         * @return <tt>true</tt> if there are unparsed characters,
         *         <tt>false</tt> otherwise.
         */
        private boolean hasChar() {
            return this.pos < this.len;
        }

        /**
         * A helper method to process the parsed token. This method removes
         * leading and trailing blanks as well as enclosing quotation marks,
         * when necessary.
         *
         * @param quoted <tt>true</tt> if quotation marks are expected,
         *               <tt>false</tt> otherwise.
         * @return the token
         */
        private String getToken(boolean quoted) {
            // Trim leading white spaces
            while ((i1 < i2) && (Character.isWhitespace(chars[i1]))) {
                i1++;
            }
            // Trim trailing white spaces
            while ((i2 > i1) && (Character.isWhitespace(chars[i2 - 1]))) {
                i2--;
            }
            // Strip away quotation marks if necessary
            if (quoted) {
                if (((i2 - i1) >= 2)
                        && (chars[i1] == '"')
                        && (chars[i2 - 1] == '"')) {
                    i1++;
                    i2--;
                }
            }
            String result = null;
            if (i2 > i1) {
                result = new String(chars, i1, i2 - i1);
            }
            return result;
        }

        /**
         * Tests if the given character is present in the array of characters.
         *
         * @param ch the character to test for presense in the array of characters
         * @param charray the array of characters to test against
         *
         * @return <tt>true</tt> if the character is present in the array of
         *   characters, <tt>false</tt> otherwise.
         */
        private boolean isOneOf(char ch, final char[] charray) {
            boolean result = false;
            for (int i = 0; i < charray.length; i++) {
                if (ch == charray[i]) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        /**
         * Parses out a token until any of the given terminators
         * is encountered.
         *
         * @param terminators the array of terminating characters. Any of these
         * characters when encountered signify the end of the token
         *
         * @return the token
         */
        private String parseToken(final char[] terminators) {
            char ch;
            i1 = pos;
            i2 = pos;
            while (hasChar()) {
                ch = chars[pos];
                if (isOneOf(ch, terminators)) {
                    break;
                }
                i2++;
                pos++;
            }
            return getToken(false);
        }

        /**
         * Parses out a token until any of the given terminators
         * is encountered outside the quotation marks.
         *
         * @param terminators the array of terminating characters. Any of these
         * characters when encountered outside the quotation marks signify the end
         * of the token
         *
         * @return the token
         */
        private String parseQuotedToken(final char[] terminators) {
            char ch;
            i1 = pos;
            i2 = pos;
            boolean quoted = false;
            boolean charEscaped = false;
            while (hasChar()) {
                ch = chars[pos];
                if (!quoted && isOneOf(ch, terminators)) {
                    break;
                }
                if (!charEscaped && ch == '"') {
                    quoted = !quoted;
                }
                charEscaped = (!charEscaped && ch == '\\');
                i2++;
                pos++;

            }
            return getToken(true);
        }

        /**
         * Returns <tt>true</tt> if parameter names are to be converted to lower
         * case when name/value pairs are parsed.
         *
         * @return <tt>true</tt> if parameter names are to be
         * converted to lower case when name/value pairs are parsed.
         * Otherwise returns <tt>false</tt>
         */
        public boolean isLowerCaseNames() {
            return this.lowerCaseNames;
        }

        /**
         * Sets the flag if parameter names are to be converted to lower case when
         * name/value pairs are parsed.
         *
         * @param b <tt>true</tt> if parameter names are to be
         * converted to lower case when name/value pairs are parsed.
         * <tt>false</tt> otherwise.
         */
        public void setLowerCaseNames(boolean b) {
            this.lowerCaseNames = b;
        }

        /**
         * Extracts a map of name/value pairs from the given string. Names are
         * expected to be unique. Multiple separators may be specified and
         * the earliest found in the input string is used.
         *
         * @param str the string that contains a sequence of name/value pairs
         * @param separators the name/value pairs separators
         *
         * @return a map of name/value pairs
         */
        public Map parse(final String str, char[] separators) {
            if (separators == null || separators.length == 0) {
                return new HashMap();
            }
            char separator = separators[0];
            if (str != null) {
                int idx = str.length();
                for (int i = 0;  i < separators.length;  i++) {
                    int tmp = str.indexOf(separators[i]);
                    if (tmp != -1) {
                        if (tmp < idx) {
                            idx = tmp;
                            separator = separators[i];
                        }
                    }
                }
            }
            return parse(str, separator);
        }

        /**
         * Extracts a map of name/value pairs from the given string. Names are
         * expected to be unique.
         *
         * @param str the string that contains a sequence of name/value pairs
         * @param separator the name/value pairs separator
         *
         * @return a map of name/value pairs
         */
        public Map parse(final String str, char separator) {
            if (str == null) {
                return new HashMap();
            }
            return parse(str.toCharArray(), separator);
        }

        /**
         * Extracts a map of name/value pairs from the given array of
         * characters. Names are expected to be unique.
         *
         * @param chars the array of characters that contains a sequence of
         * name/value pairs
         * @param separator the name/value pairs separator
         *
         * @return a map of name/value pairs
         */
        public Map parse(final char[] chars, char separator) {
            if (chars == null) {
                return new HashMap();
            }
            return parse(chars, 0, chars.length, separator);
        }

        /**
         * Extracts a map of name/value pairs from the given array of
         * characters. Names are expected to be unique.
         *
         * @param chars the array of characters that contains a sequence of
         * name/value pairs
         * @param offset - the initial offset.
         * @param length - the length.
         * @param separator the name/value pairs separator
         *
         * @return a map of name/value pairs
         */
        public Map parse(
                final char[] chars,
                int offset,
                int length,
                char separator) {

            if (chars == null) {
                return new HashMap();
            }
            HashMap params = new HashMap();
            this.chars = chars;
            this.pos = offset;
            this.len = length;

            String paramName = null;
            String paramValue = null;
            while (hasChar()) {
                paramName = parseToken(new char[] {
                        '=', separator });
                paramValue = null;
                if (hasChar() && (chars[pos] == '=')) {
                    pos++; // skip '='
                    paramValue = parseQuotedToken(new char[] {
                            separator });
                }
                if (hasChar() && (chars[pos] == separator)) {
                    pos++; // skip separator
                }
                if ((paramName != null) && (paramName.length() > 0)) {
                    if (this.lowerCaseNames) {
                        paramName = paramName.toLowerCase();
                    }
                    params.put(paramName, paramValue);
                }
            }
            return params;
        }
    }
}
