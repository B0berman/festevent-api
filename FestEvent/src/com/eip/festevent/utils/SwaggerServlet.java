package com.eip.festevent.utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import io.swagger.jaxrs.config.BeanConfig;

public class SwaggerServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.1");
        beanConfig.setFilterClass("com.eip.festevent.authentication.AuthenticationFilter");
        beanConfig.setTitle("FestEvent-API");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("92.222.82.30:8080");
        beanConfig.setBasePath("/eip");
        beanConfig.setResourcePackage("com.eip.festevent.services");
        beanConfig.setScan(true);
    }
}