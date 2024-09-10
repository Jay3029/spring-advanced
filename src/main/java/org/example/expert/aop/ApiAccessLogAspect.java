package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.domain.common.dto.AuthUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class ApiAccessLogAspect {

    // PointCut
    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    public void deleteCommentPointcut() {}

    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void changeUserRolePointcut() {}


    // Advice
    @After("deleteCommentPointcut()")
    public void logApiAccess(JoinPoint joinPoint) {
        logRequestDetails(joinPoint);
    }

    @After("changeUserRolePointcut()")
    public void logUserRoleChange(JoinPoint joinPoint) {
        logRequestDetails(joinPoint);
    }


    // Logging Method
    private void logRequestDetails(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        // 요청한 사용자의 ID
        Object[] args = joinPoint.getArgs();
        String userId = "";
        for (Object arg : args) {
            if (arg instanceof AuthUser) {
                userId = String.valueOf(((AuthUser) arg).getId());
                break;
            }
        }

        // API 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        // API 요청 URL
        String requestURI = request.getRequestURI();

        log.info(
                "API ACCESS LOG - "
                        + "USER_ID: " + userId
                        + ", REQUEST_TIME: " + requestTime
                        + ", REQUEST_URI: " + requestURI
        );
    }
}
