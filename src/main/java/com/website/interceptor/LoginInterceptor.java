package com.website.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 登陆拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("Lanjie11111111111111111111111111111111111111111");
		String uri = request.getRequestURI();
		if(uri.indexOf("api") != -1) {
			request.getRequestDispatcher("index.html").forward(request, response);
			return false;
		}
		return true;
	}
}
