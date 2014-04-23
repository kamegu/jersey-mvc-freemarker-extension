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

package com.github.kamegu.jersey.freemarker.common.flexible;

import com.github.kamegu.jersey.freemarker.common.ExtendedFreemarkerProperties;
import com.github.kamegu.jersey.freemarker.common.macro.ViewableMacroAvailableInterceptor;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.jvnet.hk2.annotations.Optional;

public class FlexibleConfiguration extends Configuration {
    @Inject
    public FlexibleConfiguration(final javax.ws.rs.core.Configuration config, @Optional final ServletContext servletContext) {
        super();

        final List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
        if (servletContext != null) {
            loaders.add(new WebappTemplateLoader(servletContext));
        }
        loaders.add(new ClassTemplateLoader(FlexibleConfiguration.class, "/"));
        try {
            loaders.add(new FileTemplateLoader(new File("/")));
        } catch (IOException e) {
            // NOOP
        }

        // Create Factory.
        this.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
        try {
            settingConfiguration(config);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private void settingConfiguration(javax.ws.rs.core.Configuration config) throws TemplateException {
        Object properties = config.getProperty(ExtendedFreemarkerProperties.TEMPLATE_PROPERTIES);
        Map<String, String> settings = null;
        if (properties == null) {
            settings = ExtendedFreemarkerProperties.defaultProperties();
        }
        if (properties instanceof Map) {
            settings = (Map<String, String>) properties;
        }
        if (settings != null) {
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                this.setSetting(entry.getKey(), entry.getValue());
            }
        }

        if (config.isRegistered(ViewableMacroAvailableInterceptor.class)) {
            this.addAutoImport("c", ViewableMacroAvailableInterceptor.class.getPackage().getName().replace(".", "/") + "/common.ftl");
        }
    }

}
