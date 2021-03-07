package com.notspend.util.web;

import com.notspend.entity.User;
import com.notspend.service.persistance.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Allows to use {@link User} as Controller method argument
 */
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        if (User.class.isAssignableFrom(parameter.getParameterType())) {
            HttpServletRequest request = toServletRequest(webRequest);
            Principal userPrincipal = request.getUserPrincipal();
            String username = userPrincipal.getName();
            User user = userService.getUser(username);
            return user;
        }
        throw new UnsupportedOperationException("Unknown parameter type: " + paramType.getName());
    }

    private HttpServletRequest toServletRequest(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException(
                    "Current request is not of type [" + HttpServletRequest.class.getName() + "]: " + webRequest);
        }
        return request;
    }

}
