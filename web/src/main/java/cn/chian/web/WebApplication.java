package cn.chian.web;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

//@SpringBootApplication

//不访问数据库的注解
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableDiscoveryClient//就是发布以来让他发现,就是自己的发现自己I是注册中心,
@RestController
@Configuration  //提示使用负载均衡
public class WebApplication {
    @Autowired
    RestTemplate rest;
    @Autowired
    LoadBalancerClient loadBalancerClient;


    @Bean//手动加入Bean实现类
    @LoadBalanced //加载均衡
    public RestTemplate getRest(){
        return new RestTemplate();
    }

    @Bean  //负载均衡随机规则，随机调用集群服务
    public IRule ribbonRule(){
        return new RandomRule();
    }

    @RequestMapping(value="/getinfo",produces = "application/json;charset=UTF-8")
    public Student myGet(HttpServletResponse res){
        res.setHeader("Access-Control-Allow-Origin", "*");
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider");
        String url = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort())+"/getInfo";

        Student stu = rest.getForObject("http://provider/getInfo",Student.class);
        return stu;
    }

    @RequestMapping(value="/insert",produces = "application/json;charset=UTF-8")
    public int insert(HttpServletResponse res){
        res.setHeader("Access-Control-Allow-Origin", "*");
        return rest.getForObject("http://provider/insert",Integer.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
