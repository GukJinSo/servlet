package gukjin.servlet.web.frontcontroller.v2;

import gukjin.servlet.web.frontcontroller.MyView;
import gukjin.servlet.web.frontcontroller.v1.ControllerV1;
import gukjin.servlet.web.frontcontroller.v2.ControllerV2;
import gukjin.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import gukjin.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import gukjin.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {
    private Map<String, ControllerV2> controllerMap = new HashMap<>(); // 프론트 컨트롤러에 매핑할 컨트롤러들 저장소

    public FrontControllerServletV2() { // 프론트 컨트롤러 서블릿 최초 생성시에 맵핑된 컨트롤러들을 사용할 수 있도록 넣음
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontControllerServletV2.service");

        String uri = req.getRequestURI(); // /front-controller/v2/members/new-form
        ControllerV2 controller = controllerMap.get(uri); // URI에 맞는 컨트롤러를 꺼냄
        if(controller == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 없으면 404 에러 후 종료
            return;
        }

        MyView view = controller.process(req, resp);// JSP 호출
        view.render(req, resp);

    }
}

