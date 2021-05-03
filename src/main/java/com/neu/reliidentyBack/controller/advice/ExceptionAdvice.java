package com.neu.reliidentyBack.controller.advice;

import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jasonR
 * @date 2021/5/2 19:12
 * 统一异常处理
 */
@Slf4j(topic = "ExceptionAdvice")
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletResponse response) throws IOException {
        log.error("服务器出现异常 "+e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }
        PrintWriter writer =response.getWriter();
        writer.write(ReliidentyUtils.getJSONString(500,"服务器出现异常！"));
    }
}
