package com.website;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常处理器
 * @author wangshuli
 */
@Controller
public class ExceptionConfig implements ErrorController {

    private static final String ERROR_PATH = "/error";

    private static final String ERROR_PAGE = "404.html";

    @RequestMapping(value=ERROR_PATH)
    public String handleError(){
        return ERROR_PAGE;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

}