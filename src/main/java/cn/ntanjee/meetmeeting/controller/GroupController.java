package cn.ntanjee.meetmeeting.controller;

import cn.ntanjee.meetmeeting.model.Group;
import cn.ntanjee.meetmeeting.model.User;
import cn.ntanjee.meetmeeting.service.GroupService;
import cn.ntanjee.meetmeeting.service.MeetingService;
import cn.ntanjee.meetmeeting.service.UserService;
import cn.ntanjee.meetmeeting.vo.GouList;
import cn.ntanjee.meetmeeting.vo.GroupInfo;
import cn.ntanjee.meetmeeting.vo.TestObject;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 74123
 */
public class GroupController extends Controller {

    public void list(){
        String token = getPara("token");

        int uid = TokenAnalysis.analysis(token);

        List<Group> list = GroupService.getInstance().getListByConferee(uid);
        List<GouList> glist = new LinkedList<>();
        for (Group g:
             list) {
            glist.add(new GouList(g.get("gid"), g.getTitle()));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupList", glist);
        jsonObject.put("authorization", "T000");

        renderJson(jsonObject);
    }

    public void info(){
        String token = getPara("token");
        int gid = getParaToInt("gid");

        int uid = TokenAnalysis.analysis(token);

        Group group = GroupService.getInstance().getGroupById(gid);

        int isAdmin = 0;
        if (group.get("admin").equals(uid)) {
            isAdmin = 1;
        }

        User admin = UserService.getInstance().getByUid(group.get("admin"));

        int[] confereeUid = GroupService.getInstance().getConfereeUid(gid);
        List<User> list = new LinkedList<>();
        for (int i:
             confereeUid) {
            list.add(UserService.getInstance().getByUid(i));
        }

        GroupInfo groupInfo = new GroupInfo(group.get("m_id"), admin, list, isAdmin, "T000");

        renderJson(groupInfo);
    }

    public void drop() {
        String token = getPara("token");
        int gid = getParaToInt("gid");

        int uid = TokenAnalysis.analysis(token);

        Boolean b = GroupService.getInstance().dropGroup(gid, uid);

        JSONObject jsonObject = new JSONObject();

        if (b) {
            jsonObject.put("code", "R000");
        } else {
            jsonObject.put("code", "R001");
        }

        jsonObject.put("authorization", "T000");

        renderJson(jsonObject);
    }
}
