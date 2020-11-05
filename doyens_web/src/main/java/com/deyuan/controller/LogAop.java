package com.deyuan.controller;

import com.deyuan.pojo.SysLog;
import com.deyuan.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;


@Component
@Aspect
public class LogAop {
    private Date visitTime;//开始时间
    private Class classzz;//访问的类
    private Method method;//访问的方法

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ISysLogService sysLogService;

    //前置通知
    @Before("execution(* com.deyuan.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime=new Date();
        //具体访问的类
        classzz=jp.getTarget().getClass();
        //执行的方法名
        String methodName=jp.getSignature().getName();
        //获取访问的方法参数
        Object[] args = jp.getArgs();
        if (args==null || args.length==0){
            //在类对象中通过方法名获取方法,只能获取无参的方法
            method=classzz.getMethod(methodName);
        }else{
            Class[] classArgs=new Class[args.length];
            for(int i=0;i<args.length;i++){
                classArgs[i]=args[i].getClass();
            }
            //封装参数
            classzz.getMethod(methodName,classArgs);
        }

    }

    //后置通知
    @After("execution(* com.deyuan.controller.*.*(..))")
    public void doAfter(){
        long time=new Date().getTime()-visitTime.getTime();
        //获取操作的url,通过Java反射方式获取
        String url="";
        if (classzz!=null && method!=null &&classzz!=LogAop.class){
            //获取类上的RequestMapping注解里的内容
            RequestMapping classzzAnnotation = (RequestMapping) classzz.getAnnotation(RequestMapping.class);
            if (classzzAnnotation!=null){
                //获取到类上的RequestMapping里的valur值
                String[] classValue = classzzAnnotation.value();
                //获取方法上的RequestMapping
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                //取出value值
                String[] methodValue=methodAnnotation.value();
                url=classValue[0]+methodValue[0];
                //获取的请求的IP地址
                String ip=request.getRemoteAddr();
                //获取当前操作的用户
                SecurityContext context = SecurityContextHolder.getContext();
                //获取到当前的操作用户对象
                User principal = (User) context.getAuthentication().getPrincipal();
                //获取用户名
                String userName=principal.getUsername();
                SysLog sysLog=new SysLog();
                sysLog.setIp(ip);
                sysLog.setExecutionTime(time);
                sysLog.setMethod("[类名] "+classzz.getName()+"[方法名] "+method.getName());//访问的方法
                sysLog.setUrl(url);//访问的url
                sysLog.setUsername(userName);
                sysLog.setVisitTime(visitTime);
                sysLogService.save(sysLog);
            }
        }

    }
}
