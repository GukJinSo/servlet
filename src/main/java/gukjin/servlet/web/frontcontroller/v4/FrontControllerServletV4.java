package gukjin.servlet.web.frontcontroller.v4;

import gukjin.servlet.web.frontcontroller.ModelView;
import gukjin.servlet.web.frontcontroller.MyView;
import gukjin.servlet.web.frontcontroller.v3.ControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import gukjin.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import gukjin.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import gukjin.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    private Map<String, ControllerV4> controllerMap = new HashMap<>(); // 프론트 컨트롤러에 매핑할 컨트롤러들 저장소

    public FrontControllerServletV4() { // 프론트 컨트롤러 서블릿 최초 생성시에 맵핑된 컨트롤러들을 사용할 수 있도록 넣음
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontControllerServletV4.service");

        String uri = req.getRequestURI(); // /front-controller/v2/members/new-form
        ControllerV4 controller = controllerMap.get(uri); // URI에 맞는 컨트롤러를 꺼냄
        if(controller == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 없으면 404 에러 후 종료
            return;
        }

        Map<String, String> paramMap = createParamMap(req); // HttpServletRequest의 역할
        Map<String, Object> model = new HashMap<>(); // req.setAttritube() 와 같은 역할의 저장소

        String viewName = controller.process(paramMap, model);
        MyView view = viewResolver(viewName); // 논리적 이름을 주고 실제 물리적 이름을 가져옴

        view.render(model, req, resp);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();
        req.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }
}

