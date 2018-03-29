package cn.ntanjee.meetmeeting.controller.meeting;

import cn.ntanjee.meetmeeting.model.Request;
import cn.ntanjee.meetmeeting.service.MeetingService;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;

import java.util.List;

public class MeetingRequestController extends Controller{
    private JSONObject jsonObject = new JSONObject();

    public void index(){
        int rid = getParaToInt("rid");
        int auth = getParaToInt("auth");
        String token = getPara("token");

        MeetingService.getInstance().reviewRequest(rid, auth);

        jsonObject.put("authorization", "T000");

        renderJson(jsonObject);
    }

    public void put(){
        String token = getPara("token");
        int mid = getParaToInt("mid");
        String name = getPara("name");
        String phone = getPara("phone");
        String remark = getPara("remark");

        int i = -1;
        i = MeetingService.getInstance().createRequest(2, mid, name, phone, remark);

        if (i < 0){
            jsonObject.put("authorization", "T001");
        } else {
            jsonObject.put("mid", mid);
        }

        renderJson(jsonObject);
    }

    public void list(){
        String token = getPara("token");

        List<Request> requestList = MeetingService.getInstance().getRequestByUid(1);

        jsonObject.put("requestList", requestList);

        renderJson(jsonObject);
    }

    public void info(){
        int rid = getParaToInt("rid");
        String token = getPara("token");

        Request request = MeetingService.getInstance().getRequestByRid(rid);

        renderJson(request);
    }

    public void mylist(){
        String token = getPara("token");
        int status = getParaToInt("status");

        List<Request> list = MeetingService.getInstance().getMyRequest(1, status);

        jsonObject.put("meetingList", list);
        jsonObject.put("authorization", "T000");

        renderJson(jsonObject);
    }
}
