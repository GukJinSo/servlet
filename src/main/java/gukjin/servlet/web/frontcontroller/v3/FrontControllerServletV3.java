package gukjin.servlet.web.frontcontroller.v3;

import gukjin.servlet.web.frontcontroller.ModelView;
import gukjin.servlet.web.frontcontroller.MyView;
import gukjin.servlet.web.frontcontroller.v2.ControllerV2;
import gukjin.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import gukjin.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import gukjin.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import org.springframework.ui.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>(); // 프론트 컨트롤러에 매핑할 컨트롤러들 저장소

    public FrontControllerServletV3() { // 프론트 컨트롤러 서블릿 최초 생성시에 맵핑된 컨트롤러들을 사용할 수 있도록 넣음
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        String uri = req.getRequestURI(); // /front-controller/v2/members/new-form
        ControllerV3 controller = controllerMap.get(uri); // URI에 맞는 컨트롤러를 꺼냄
        if(controller == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 없으면 404 에러 후 종료
            return;
        }

        /*
        컨트롤러의 서블릿 종속성 제거
        서블릿 종속성을 제거하기 위해, 모델뷰:ModelView 객체를 통해 데이터를 운반할 것이다.
        이 모델뷰 객체는 Model(data)와 view(viewName)를 가진다.
        이제부터 컨트롤러는 HttpServletRequest, Response 객체 대신 Map을 파라미터로 받아 서블릿 종속성을 제거한다.
        각각의 컨트롤러마다 자신이 처리할 JSP의 논리 이름과 처리 로직에 포함될 데이터가 있을 것이다.
        컨트롤러는 비즈니스 처리가 끝난 후의 데이터와 논리 이름을 포함한 모델뷰를 반환한다.
         */

        // Map을 파라미터로 받는데, 그러기 위해서는 req의 파라미터를 전부 꺼내서 ParamMap에 전달해야 한다
        Map<String, String> paramMap = createParamMap(req);
        ModelView mv = controller.process(paramMap);

        /*
        뷰 리졸버를 통한 중복 제거
        위의 컨트롤러 처리를 통해 모델뷰는 논리적 이름을 가지고 있다.
        viewResolver 함수를 통해 논리적 이름을 전달하면 prefix, suffix가 포함된 실제 물리적 이름을 가져온다.
         */

        String viewName = mv.getViewName();// 논리적 view 이름만 가져옴
        MyView view = viewResolver(viewName); // 논리적 이름을 주고 실제 물리적 이름을 가져옴

        /*
        JSP는 request 객체를 사용하도록 되어 있다. 다시 모델 데이터를 request 안에 포함하는 과정이 필요하다.
        render 함수 내부에는 데이터를 받아 다시 request 객체 안에 저장하는 로직이 포함되어 있다.
         */
        view.render(mv.getModel(), req, resp);
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

