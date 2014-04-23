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

package com.github.kamegu.jersey.freemarker.common.macro;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import org.glassfish.jersey.server.mvc.Viewable;

@Priority(Priorities.ENTITY_CODER + 10)
public class ViewableMacroAvailableInterceptor implements WriterInterceptor {

    private static final String CONTEXT_KEY_NAME = "context";

    @Context HttpServletRequest httpServletRequest;
    @Context UriInfo uriInfo;

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        final Object entity = context.getEntity();

        if (entity.getClass() == Viewable.class) {
            final Viewable viewable = (Viewable) entity;
            Object model = viewable.getModel();
            if (!(model instanceof Map)) {
                model = new HashMap<String, Object>() {{
                    put("model", viewable.getModel());
                }};
            }
            Map<String, Object> map = (Map<String, Object>) model;
            setHttpContext(map, httpServletRequest);

            context.setEntity(new Viewable(viewable.getTemplateName(), model));
        }

        context.proceed();
    }

    private void setHttpContext(Map<String, Object> map, HttpServletRequest httpServletRequest) {
        if (map.containsKey(CONTEXT_KEY_NAME)) {
            return;
        }
        Map<String, Object> context = new HashMap<String, Object>();
        if (httpServletRequest != null) {
            context.put("path", httpServletRequest.getContextPath());
            context.put("fullPath", httpServletRequest.getContextPath() + httpServletRequest.getServletPath() + httpServletRequest.getPathInfo());
            context.put("cookies", httpServletRequest.getCookies());
            context.put("locale", httpServletRequest.getLocale());
        }
        map.put(CONTEXT_KEY_NAME, context);
    }
}
