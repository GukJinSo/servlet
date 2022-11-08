package gukjin.servlet.basic;

import org.springframework.http.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "testServlet", urlPatterns = "/hello")
public class ServletTest extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletTest.service");
        System.out.println("req = " + req);
        System.out.println("resp = " + resp);

        String username = req.getParameter("username"); // 쿼리파라미터의 데이터 가져옴
        System.out.println("username = " + username);

        resp.setContentType("text/plain"); // 헤더 안에 들어감
        resp.setCharacterEncoding("UTF-8"); // 헤더 안에 들어감. EUC-KR은 사실상 deprecated
        resp.getWriter().write("hello " + username); // 바디 안에 들어감

    }
}
