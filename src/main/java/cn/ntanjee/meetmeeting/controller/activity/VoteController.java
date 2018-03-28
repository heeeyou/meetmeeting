package cn.ntanjee.meetmeeting.controller.activity;

import cn.ntanjee.meetmeeting.model.User;
import cn.ntanjee.meetmeeting.model.Vote;
import cn.ntanjee.meetmeeting.service.UserService;
import cn.ntanjee.meetmeeting.service.VoteService;
import cn.ntanjee.meetmeeting.vo.VoteInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class VoteController extends Controller {
    private JSONObject jsonObject = new JSONObject();

    public void index(){
        String method = getRequest().getMethod();

        if (method.equals("POST")){
            admin();
        }else {
            user();
        }
    }

    public void info(){
        String token = getPara("token");
        int vid = getParaToInt("vid");

        Vote vote = VoteService.getInstance().getByVid(vid);
        User user = UserService.getInstance().getByUid(1);

        String vitem = vote.get("item");
        String[] item = vitem.split(" ");
        Timestamp timestamp = vote.get("deadline");
        LocalDateTime deadline = timestamp.toLocalDateTime();

        int isOver = 0;

        if (deadline.isBefore(LocalDateTime.now())) {
            isOver = 1;
        }

        VoteInfo voteInfo = new VoteInfo(user.get("username"), item, isOver, "T000");

        renderJson(voteInfo);

    }

    private void admin(){
        String token = getPara("token");
        int gid = getParaToInt("gid");
        String vname = getPara("vname");
        int minute = getParaToInt("minute");
        String item = getPara("item");
        List<String> aitem = JSON.parseArray(item, String.class);

        String[] items = new String[aitem.size()];
        for (int i = 0; i < items.length; i++){
            items[i] = aitem.get(i);
        }

        int vid = VoteService.getInstance().createVote(gid, vname, minute, items);

        jsonObject.put("vid", vid);
        jsonObject.put("gid", gid);

        renderJson(jsonObject);
    }

    private void user(){
        String token = getPara("token");
        int vid = getParaToInt("vid");
        String item = getPara("item");

        Boolean b = VoteService.getInstance().createVotingReslt(vid, item, 1);

        if (b) {
            jsonObject.put("isPass", 1);
        } else {
            jsonObject.put("isPass", 0);
        }

        renderJson(jsonObject);
    }

    public void result(){
        String token = getPara("token");
        int vid = getParaToInt("vid");

        Map<String, Integer> map = VoteService.getInstance().getVoteResult(vid);
        jsonObject.put("result", map);

        renderJson(jsonObject);
    }
}