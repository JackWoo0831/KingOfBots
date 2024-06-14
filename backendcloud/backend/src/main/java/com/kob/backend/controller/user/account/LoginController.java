package com.kob.backend.controller.user.account;


import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    /* 注入的时候 实例化接口 而不是类的原因
    * 解耦：注入接口而不是具体的类可以实现代码之间的解耦。接口定义了一组规范，而具体的实现类可以任意变化，不会影响到使用接口的代码。这样做可以增加系统的灵活性和可维护性。
    * 可替换性：通过注入接口，可以在不改变代码的情况下替换具体的实现。
    *
    * */

    @PostMapping("/user/account/token/")  // post是密文传输
    // @RequestParam 用于将Web请求中的参数绑定到控制器（Controller）的方法参数上。
    // 当一个HTTP请求到来时，Spring会根据@RequestParam注解的属性值，从请求中提取相应的参数，并将其转换为方法的参数。
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return loginService.login(username, password);
    }
    /* 在访问这个到后端接口"/user/account/token/"的时候 同时也会发送一个请求 也就是信息 然后通过RequestParam提取
    * 例如前端调试代码
    * setup () {
      $.ajax({
        url: "http://localhost:3000/user/account/token/",
        type: 'post',
        data: {
          username: 'root',
          password: 'root',
        },
        success (resp) {
          console.log(resp)
        },
        error (resp) {
          console.log(resp)
        }
      }

      )
    }
    *
    * */

}
