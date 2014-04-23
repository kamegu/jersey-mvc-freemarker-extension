/*
 * The MIT License
 *
 * Copyright 2014 kamegu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.kamegu.jersey.freemarker.common;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ExtendedFreemarkerProperties {
    public static final String TEMPLATE_PROPERTIES = "jersey.config.server.mvc.freemarker.properties";

    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public static final String UTF8_STRING = UTF8_CHARSET.name();

    public static Map<String, String> defaultProperties() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("output_encoding", UTF8_STRING);
        map.put("default_encoding", UTF8_STRING);
        map.put("url_escaping_charset", UTF8_STRING);
        return map;
    }

    public static Map<String, String> extendedProperties(String... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Map<String, String> map = defaultProperties();
        int length = args.length;
        for (int i = 0; i < length / 2; i++) {
            map.put(args[i * 2], args[i * 2 + 1]);
        }
        return map;
    }
}
