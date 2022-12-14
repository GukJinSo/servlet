package gukjin.servlet.basic.request;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8081/request-param?username=hello&age=20
 */
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회 - start]");
        req.getParameterNames().asIterator().forEachRemaining(param -> System.out.println(param + " = " + req.getParameter(param)));
        System.out.println("[전체 파라미터 조회 - end]");


        System.out.println("[단일 파라미터 조회 - start]");
        String age = req.getParameter("age");
        System.out.println("age = " + age);
        System.out.println("[단일 파라미터 조회 - end]");

        System.out.println("[이름이 같은 복수 파라미터 조회 - start]");
        String[] users = req.getParameterValues("username");
        for (String user : users) {
            System.out.println("user = " + user);
        }
        System.out.println("[이름이 같은 복수 파라미터 조회 - end]");
    }
}
