package gukjin.servlet.web.frontcontroller.v3.controller;

import gukjin.servlet.web.frontcontroller.ModelView;
import gukjin.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form"); // 논리적 이름
    }
}
