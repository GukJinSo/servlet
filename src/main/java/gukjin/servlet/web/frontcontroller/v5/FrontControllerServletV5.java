package gukjin.servlet.web.frontcontroller.v5;

import gukjin.servlet.web.frontcontroller.ModelView;
import gukjin.servlet.web.frontcontroller.MyView;
import gukjin.servlet.web.frontcontroller.v3.ControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import gukjin.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import gukjin.servlet.web.frontcontroller.v4.ControllerV4;
import gukjin.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import gukjin.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import gukjin.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import gukjin.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import gukjin.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    // 아무 컨트롤러나 지원하기 위해 Object 타입
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    // 어댑터가 여러개 있으므로 List 타입
    private List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters(handlerAdapters);
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdapters(List<MyHandlerAdapter> handlerAdapters) {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 리퀘스트 URI에 맞는 핸들러(컨트롤러)를 가져옴
        // handlerMappingMap의 "/front-controller/v5/v3/members/new-form 키의 밸류는 MemberFormControllerV3()
        Object handler = getHandler(req);

        // 핸들러가 없으면(그런 URI를 처리할 컨트롤러가 없으면) 404 에러
        if(handler == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 핸들러(컨트롤러) 종류에 맞게 처리할 어댑터를 조회하여 가져옴
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        // 핸들러 어댑터로 핸들러(컨트롤러) 실행
        ModelView mv = adapter.handle(req, resp, handler);
        String viewName = mv.getViewName();// 논리적 view 이름만 가져옴
        MyView view = viewResolver(viewName); // 논리적 이름을 주고 실제 물리적 이름을 가져옴

        view.render(mv.getModel(), req, resp);

    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalStateException("다음 핸들러가 없음 : handler = " + handler);
    }

    private Object getHandler(HttpServletRequest req) {
        String uri = req.getRequestURI(); // /front-controller/v5/members/new-form
        return handlerMappingMap.get(uri);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
