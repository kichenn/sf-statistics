
import channel.bean.BaseResult;
import channel.ChannelManageRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ConfigManagedService;
import config.ConfigManager;
import config.Constants;
import log.LoggerFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import report.*;
import report.bean.ReportResult;
import staticPart.RedisCache;
import staticPart.ThreadPool;
import task.TimerManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by yuao on 11/9/17.
 * Modified by zhipeng on 28/11/17.
 */
public class ControllerServlet {


    static {
        System.out.println(String.format("ConfigManager initialize result: %s", ConfigManagedService.INSTANCE.getConfig().isCompleted()));
    }

    public static void main(String[] args) throws Exception {

        ConfigManager configManager = ConfigManagedService.INSTANCE.getConfig();
        int port = 9901;
        int timeout = 10;
        port = configManager.getInteger(Constants.SERVICE_PORT);
        int maxThreads = configManager.getInteger(Constants.SERVICE_THREADPOOL_MAX);
        int minThreads = configManager.getInteger(Constants.SERVICE_THREADPOOL_MIN);
        timeout = configManager.getInteger(Constants.SERVICE_TIMEOUT);


        /**
         * init place
         */
        ThreadPool.INSTANCE.init(minThreads, maxThreads, timeout);



        /***
         * redis 缓存启动
         */
        RedisCache.INSTANCE.init(configManager.getStr(Constants.REDIS_HOST), configManager.getInteger(Constants.REDIS_PORT), configManager.getInteger(Constants.REDIS_CONNECTION_TIMEOUT), configManager.getStr(Constants.REDIS_PASSWORD));


        /***
         * 定时任务处理 报表
         */
        TimerManager.init();

        /**
         * start service
         */
        Server server = new Server(ThreadPool.INSTANCE.getThreadPool());
        ServerConnector http = new ServerConnector(server);
        http.setPort(port);
        server.addConnector(http);

        ServletHandler handler = new ServletHandler();


        LoggerFactory.getLogger().info(String.format("[server start on port: '%s'", port));

        handler.addServletWithMapping(WebServlet.class, "/*");

        server.setHandler(handler);

        server.start();

        server.join();
    }


    public static class WebServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            if ("/_health_check".equals(request.getRequestURI())) {
                healthCheck(response);
            } else if (request.getRequestURI().equals(ReportApiRequest.entryPoint)) {

                ReportApiRequest reportApiRequest = new ReportApiRequest(request);
                reportApiRequest.process(request, response);
            } else if (request.getRequestURI().equals(ReportSatisfactionApiRequest.entryPoint)) {

                ReportSatisfactionApiRequest reportApiRequest = new ReportSatisfactionApiRequest(request);
                reportApiRequest.process(request, response);
            } else if (request.getRequestURI().equals(ReportRoundNumApiRequest.entryPoint)) {

                ReportRoundNumApiRequest reportApiRequest = new ReportRoundNumApiRequest(request);
                reportApiRequest.process(request, response);
            } else if (request.getRequestURI().equals(HourSessionReportApiRequest.entryPoint)) {

                HourSessionReportApiRequest reportApiRequest = new HourSessionReportApiRequest(request);
                reportApiRequest.process(request, response);
            } else if (request.getRequestURI().equals(DialogueTimeReportApiRequest.entryPoint)) {

                DialogueTimeReportApiRequest reportApiRequest = new DialogueTimeReportApiRequest(request);
                reportApiRequest.process(request, response);
            } else if (request.getRequestURI().equals(FaqIndexApiRequest.entryPoint)) {

                FaqIndexApiRequest reportApiRequest = new FaqIndexApiRequest(request);
                reportApiRequest.process(request, response);
            } else if (request.getRequestURI().equals(ReportApiRequest.listEntryPoint)) {       // 2会话数据统计

                ReportApiRequest reportApiRequest = new ReportApiRequest(request);
                ReportResult reportResult = reportApiRequest.list();
                setResponseInfo(response, new Gson().toJson(reportResult));
            } else if (request.getRequestURI().equals(ReportApiRequest.exportEntryPoint)) {

                ReportApiRequest reportApiRequest = new ReportApiRequest(request);
                reportApiRequest.list4download(response);

            } else if (request.getRequestURI().equals(ReportSatisfactionApiRequest.listEntryPoint)) {   //1满意度评价统计

                ReportSatisfactionApiRequest reportApiRequest = new ReportSatisfactionApiRequest(request);
                ReportResult reportResult = reportApiRequest.list();
                setResponseInfo(response, new Gson().toJson(reportResult));
            } else if (request.getRequestURI().equals(SessionDetailRequest.listEntryPoint)) {

                SessionDetailRequest reportApiRequest = new SessionDetailRequest(request);
                ReportResult reportResult = reportApiRequest.list();
                setResponseInfo(response, new Gson().toJson(reportResult));
            } else if (request.getRequestURI().equals(SessionDetailRequest.exportEntryPoint)) {

                SessionDetailRequest reportApiRequest = new SessionDetailRequest(request);
                reportApiRequest.process(request, response);
            } else if(request.getRequestURI().equals(ChannelManageRequest.listAllChannelPoint)) {

                ChannelManageRequest channelManageRequeset = new ChannelManageRequest();
                BaseResult baseResult = channelManageRequeset.listAllChannel(request);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                setResponseInfo(response, gson.toJson(baseResult));
            }else if(request.getRequestURI().equals(ChannelManageRequest.listActiveChannelPoint)) {

                ChannelManageRequest channelManageRequeset = new ChannelManageRequest();
                BaseResult baseResult = channelManageRequeset.listActiveChannel(request);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                setResponseInfo(response, gson.toJson(baseResult));
            }else {
                setResponseInfo(response, otherAPIUrl(), "text/html;charset=utf-8");
            }
            return;

        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            String result = "";
            if (request.getRequestURI().equals("/_health_check")) {
                healthCheck(response);
                return;
            } else if(request.getRequestURI().equals(ChannelManageRequest.addChannelPoint)) {
                ChannelManageRequest channelManageRequeset = new ChannelManageRequest();
                BaseResult baseResult = channelManageRequeset.addChannel(request);
                setResponseInfo(response, new Gson().toJson(baseResult));
            }else if(request.getRequestURI().equals(ChannelManageRequest.updateChannelPoint)) {
                ChannelManageRequest channelManageRequeset = new ChannelManageRequest();
                BaseResult baseResult = channelManageRequeset.updateChannel(request);
                setResponseInfo(response, new Gson().toJson(baseResult));
            }else {
                setResponseInfo(response, otherAPIUrl(), "text/html;charset=utf-8");
                return;
            }
        }

        private void setResponseInfo(HttpServletResponse response, String result) throws IOException {
            String contentType = "application/json;charset=utf-8";
            setResponseInfo(response, result, contentType);
        }

        private void setResponseInfo(HttpServletResponse response, String result, String contentType) throws IOException {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(contentType);

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            response.setHeader("X-Requested-With", "XMLHttpRequest");
            response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, DELETE, PUT, POST");

            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print(result);
            out.flush();
            out.close();
        }


        private void healthCheck(HttpServletResponse response) throws IOException {
            setResponseInfo(response, "ok", "text/html;charset=utf-8");
        }

        private String otherAPIUrl() {
            return "Entrypoint are unvalid";
        }

    }
}