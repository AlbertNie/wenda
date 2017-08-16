package com.nowcoder.controller;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albert on 2017/8/9.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;


     @RequestMapping(path = {"/","/index"})
     public String index(Model model){
         List<ViewObject> vos = getQuestionswithId(0,0,5);
         model.addAttribute("vos",vos);
         return "index";
     }

     @RequestMapping("/user/{userId}")
     public String userhome(@PathVariable("userId") int id,
                            Model model){
         List<ViewObject> vos = getQuestionswithId(id,0,5);
         model.addAttribute("vos",vos);
         return "index";
     }

     private List<ViewObject> getQuestionswithId(int userId, int offset, int limit){
         List<Question> questions = questionService.getLatestQuestions(userId,offset,limit);
         List<ViewObject> vos = new ArrayList<>();
         for (Question question : questions) {
             ViewObject vo = new ViewObject();
             vo.set("question",question);
             vo.set("user",userService.getUser(question.getUserId()));
             vos.add(vo);
         }
         return vos;
     }
}
