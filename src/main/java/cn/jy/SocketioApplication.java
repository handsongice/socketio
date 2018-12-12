package cn.jy;

import cn.jy.constent.Constent;
import cn.jy.entity.Employee;
import cn.jy.service.EmployeeService;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = {"cn.jy.mapper"})
public class SocketioApplication {

    @Autowired
    EmployeeService employeeService;

    public static void main(String[] args) {
        SpringApplication.run(SocketioApplication.class, args);
    }

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){   //在本地window环境测试时用localhost
            System.out.println("this is  windows");
            config.setHostname("localhost");
        } else {
            config.setHostname("120.27.10.191");   //部署到你的远程服务器正式发布环境时用服务器公网ip
        }
        config.setPort(9092);

		config.setAuthorizationListener(new AuthorizationListener() {//类似过滤器
			@Override
			public boolean isAuthorized(HandshakeData data) {
			    String type = data.getSingleUrlParam("type");
			    String clientid = data.getSingleUrlParam("clientid");
			    if(Constent.TYPE_EMPLOYEE.equals(type) && StringUtils.isNotEmpty(clientid)) {
                    Employee employee = employeeService.getEmployeeById(Long.parseLong(clientid));
                    if(null != employee && null != employee.getId()) {
                        return true;
                    }
                }
				return false;
			}
		});

        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
