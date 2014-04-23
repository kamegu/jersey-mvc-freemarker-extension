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

package com.github.kamegu.jersey.freemarker.utf8;

import com.github.kamegu.jersey.freemarker.common.ExtendedFreemarkerProperties;
import com.github.kamegu.jersey.freemarker.common.flexible.FlexibleConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.AbstractTemplateProcessor;
import org.jvnet.hk2.annotations.Optional;

@Provider
class Utf8FreemarkerProcessor extends AbstractTemplateProcessor<Template> {
    private final Configuration factory;

    @Inject
    public Utf8FreemarkerProcessor(final javax.ws.rs.core.Configuration config, final ServiceLocator serviceLocator,
                                   @Optional final ServletContext servletContext) {
        super(config, servletContext, "freemarker", "ftl");

        this.factory = new FlexibleConfiguration(config, servletContext);
    }

    @Override
    protected Template resolve(final String templateReference, final Reader reader) throws Exception {
        return factory.getTemplate(templateReference);
    }

    @Override
    public void writeTo(final Template template, final Viewable viewable, final MediaType mediaType,
                        final OutputStream out) throws IOException {
        try {
            Object model = viewable.getModel();
            if (!(model instanceof Map)) {
                model = new HashMap<String, Object>() {{
                    put("model", viewable.getModel());
                }};
            }
            
            template.process(model, new OutputStreamWriter(out, ExtendedFreemarkerProperties.UTF8_CHARSET));
        } catch (TemplateException te) {
            throw new ContainerException(te);
        }
    }
}
