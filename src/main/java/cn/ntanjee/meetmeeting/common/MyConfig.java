package cn.ntanjee.meetmeeting.common;

import cn.ntanjee.meetmeeting.Interceptor.TokenInterceptor;
import cn.ntanjee.meetmeeting.controller.BarrageController;
import cn.ntanjee.meetmeeting.controller.ContactController;
import cn.ntanjee.meetmeeting.controller.GroupController;
import cn.ntanjee.meetmeeting.controller.MessageController;
import cn.ntanjee.meetmeeting.controller.activity.PrizeController;
import cn.ntanjee.meetmeeting.controller.activity.SinginController;
import cn.ntanjee.meetmeeting.controller.activity.VoteController;
import cn.ntanjee.meetmeeting.controller.meeting.MeetingController;
import cn.ntanjee.meetmeeting.controller.meeting.MeetingRequestController;
import cn.ntanjee.meetmeeting.controller.user.UserController;
import cn.ntanjee.meetmeeting.controller.user.UserSignupController;
import cn.ntanjee.meetmeeting.model.*;
import com.github.youyinnn.server.Server;
import com.github.youyinnn.server.ServerConfig;
import com.jfinal.config.*;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.template.Engine;

public class MyConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants constants) {
        constants.setUrlParaSeparator("&");
        PropKit.use("config.properties");
        constants.setDevMode(true);
        constants.setBaseUploadPath("image");

    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/meeting", MeetingController.class);
        routes.add("/meeting/request", MeetingRequestController.class);
        routes.add("/activity/vote", VoteController.class);
        routes.add("/activity/signin", SinginController.class);
        routes.add("/activity/prize", PrizeController.class);
        routes.add("/user", UserController.class);
        routes.add("/user/signup", UserSignupController.class);
        routes.add("/group", GroupController.class);
        routes.add("/contact", ContactController.class);
        routes.add("/barrage", BarrageController.class, "");
        routes.add("message", MessageController.class);
        routes.addInterceptor(new TokenInterceptor());
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {
        //druid 数据源插件
        DruidPlugin dp = new DruidPlugin(PropKit.get("url"),PropKit.get("username"),PropKit.get("password"));
        dp.setDriverClass(PropKit.get("driverClass"));
        dp.set(PropKit.getInt("initialSize"),PropKit.getInt("minIdle"),PropKit.getInt("maxActive"));
        dp.setMaxWait(PropKit.getInt("maxWait"));
        plugins.add(dp);
        //arp 插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        plugins.add(arp);
        arp.setBaseSqlTemplatePath(PathKit.getWebRootPath()+"/WEB-INF");
        arp.addSqlTemplate("sql/all.sql");
        arp.addMapping("user","u_id", User.class);
        arp.addMapping("contact", Contact.class);
        arp.addMapping("meeting","m_id" ,Meeting.class);
        arp.addMapping("request","r_id", Request.class);
        arp.addMapping("message","msg_id", Message.class);
        arp.addMapping("group","g_id",Group.class);
        arp.addMapping("vote","v_id",Vote.class);
        arp.addMapping("voting_result",VotingResult.class);
        arp.addMapping("signin","g_id",Signin.class);

        ServerConfig serverConfig = ServerConfig.getWsServerConfig();
        serverConfig.disableUserManagementModule();
        serverConfig.setBindPort(5999);
        serverConfig.setSqlPropertiesFile("config.properties");
        Server.enableAllLogEnabled();
        Server.init(serverConfig);
        Server.start();
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {
        handlers.add(new DruidStatViewHandler("/druid"));
    }

    @Override
    public void afterJFinalStart() {
        System.out.println("----");
    }
}
