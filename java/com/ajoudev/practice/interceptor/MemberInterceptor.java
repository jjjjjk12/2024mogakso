package com.ajoudev.practice.interceptor;

import com.ajoudev.practice.Member;
import com.ajoudev.practice.service.MemberService;
import com.ajoudev.practice.service.PostBoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Component
public class MemberInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final PostBoardService postBoardService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        Member member = memberService.findOne((String) httpSession.getAttribute("id")).orElse(null);
        request.setAttribute("member", member);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("member", request.getAttribute("member"));
            modelAndView.addObject("boards", postBoardService.findBoards());
        }
    }
}
