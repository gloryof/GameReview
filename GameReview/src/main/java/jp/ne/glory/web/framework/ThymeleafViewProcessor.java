/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ne.glory.web.framework;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Thymeleaf用のテンプレートプロセッサ.<br>
 *
 * @see http://bufferings.hatenablog.com/entry/2013/01/05/122307
 * @author Junki Yamada
 */
@Provider
public class ThymeleafViewProcessor implements TemplateProcessor<String> {

    /**
     * リクエスト.
     */
    @Context
    private HttpServletRequest request;

    /**
     * *
     * レスポンス.
     */
    @Context
    private HttpServletResponse response;

    @Context
    private ServletContext servletContext;
     private final TemplateEngine templateEngine;

    public ThymeleafViewProcessor() {
        System.out.println("load ThymeleafViewProcessor");
        TemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCacheTTLMs(3600000L);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    public String resolve(String name, MediaType mediaType) {

        return name;
    }

    @Override
    public void writeTo(String templateReference, Viewable viewable, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {

        WebContext context = new WebContext(request, response, servletContext);

        context.setVariable("item", viewable.getModel());

        Writer writer = new OutputStreamWriter(out);
        templateEngine.process(templateReference, context, response.getWriter());

        writer.flush();
    }
}
