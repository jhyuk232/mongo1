package com.example.mongo.controller;

import com.example.mongo.dto.ScoreDTO;
import com.example.mongo.service.ScoreMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreMongoService service;

    @GetMapping("/list")
    public String mongolist(Model model){
        List<ScoreDTO> list =  service.findAll();
        System.out.println("*************************************");
        model.addAttribute("mongolist",list);
        return "mongo/list";
    }
    @GetMapping("/insert")
    public String insertPage(){
        return "mongo/mongo_insert";
    }
    @PostMapping("/insert")
    public String insert(ScoreDTO document){
        service.insertDocument(document);
        return "redirect:/score/list";
    }

    //동일한 형식의 입력데이터가 여러개인 경우 parameter를 동일한 이름으로 정의하고 형식에 맞게 DTO를 만들면
    //자동으로 List에 DTO가 담긴 형식으로 만들어진다.
    @GetMapping("/multi/insert")
    public String multiInsert(){
        List<ScoreDTO> docs = new ArrayList<>();
        ScoreDTO document = null;
        //임의로 10개의 데이터를 생성하는 코드이므로 나중에 쓰지않아도 되는 코드
        for(int i=1;i<=10;i++){
            document = new ScoreDTO(null,"multi"+i,"multi"+i,"전산실","서울특별시",100,100);
            docs.add(document);
        }
        service.insertAllDocument(docs);
        return "redirect:/score/list";
    }
    @GetMapping("/paginglist")
    public String pagelist(@RequestParam("pageNo") String pageNo,Model model){
        List<ScoreDTO> pagelist =  service.findAll(Integer.parseInt(pageNo));
        model.addAttribute("mongolist",pagelist);
        return "mongo/list";
    }
    @GetMapping("/search")
    public String searchPage(){
        return "mongo/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("field") String field,@RequestParam("criteria") String criteria
            ,@RequestParam("value") String value, Model model){
        List<ScoreDTO> searchlsit = service.findCriteria(field+","+criteria,value);
//        List<ScoreDTO> searchlsit = service.findCriteria(field,value);
        model.addAttribute("mongolist",searchlsit);
        return "mongo/list";
    }

    @GetMapping("/read")
    public String detail(@RequestParam("key") String key,@RequestParam("value") String value,
                         @RequestParam("action") String action, Model model){
        System.out.println(key+","+value);
        String view="";
        if(action.equals("read")){
            view="mongo/mongo_detail";
        }else{
            view="mongo/mongo_update";
        }
        ScoreDTO score = service.findById(key, value);
        model.addAttribute("score",score);
        return view;
    }
    @GetMapping("/read2")
    public String read2(@RequestParam("value") String value,
                         @RequestParam("action") String action, Model model){
        System.out.println(value);
        String view="";
        if(action.equals("read")){
            view="mongo/mongo_detail";
        }else{
            view="mongo/mongo_update";
        }
        ScoreDTO score = service.findById(value);
        System.out.println(value+"====="+score);
        model.addAttribute("score",score);
        return view;
    }
    @PostMapping("/update")
    public String update(ScoreDTO score){
        System.out.println("&&&&&&&&"+score);
        service.update(score);
        return "redirect:/score/paginglist?pageNo=0";
    }
    @GetMapping("/test")
    public  String test(){
        return "/mongo/test";
    }
}
