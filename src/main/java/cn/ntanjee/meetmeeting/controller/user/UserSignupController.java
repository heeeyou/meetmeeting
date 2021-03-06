package cn.ntanjee.meetmeeting.controller.user;

import cn.ntanjee.meetmeeting.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 74123
 */
public class UserSignupController extends Controller {
    private JSONObject jsonObject = new JSONObject();

    public void index(){
        String account = getPara("account");
        String username = getPara("username");
        String password = getPara("password");

        boolean b = UserService.getInstance().isAccExist(account);

        if (!b) {
            UserService.getInstance().createUser(account, username, password, null);
            jsonObject.put("code", "S000");
        } else {
            jsonObject.put("code", "S001");
        }

        renderJson(jsonObject);
    }

    public void authcode(){
        String phone = getPara("phone");

        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

        if (phone.length() != 11){
            jsonObject.put("legle", 0);
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            Boolean isMatch = m.matches();
            if (isMatch) {
                jsonObject.put("legle", 1);
            } else {
                jsonObject.put("legle", 0);
            }
        }

        renderJson(jsonObject);
    }
}
