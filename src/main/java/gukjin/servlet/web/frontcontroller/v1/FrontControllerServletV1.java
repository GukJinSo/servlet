package gukjin.servlet.web.frontcontroller.v1;

import gukjin.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import gukjin.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import gukjin.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {
    private Map<String, ControllerV1> controllerMap = new HashMap<>(); // 프론트 컨트롤러에 매핑할 컨트롤러들 저장소

    public FrontControllerServletV1() { // 프론트 컨트롤러 서블릿 최초 생성시에 맵핑된 컨트롤러들을 사용할 수 있도록 넣음
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String uri = req.getRequestURI(); // /front-controller/v1/members/new-form
        ControllerV1 controller = controllerMap.get(uri); // URI에 맞는 컨트롤러를 꺼냄
        if(controller == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 없으면 404 에러 후 종료
            return;
        }

        controller.process(req, resp); // 컨트롤러 호출

    }
}
