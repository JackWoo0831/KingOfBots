package com.kob.backend.configs.filter;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;



@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取JWT
        String token = request.getHeader("Authorization");

        // 如果请求头中没有token，直接放行
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 去掉Bearer前缀，获取真正的token
        token = token.substring(7);

        String userId;
        try {
            // 解析token，获取用户ID
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            // 解析失败，抛出异常
            throw new RuntimeException("Token parsing error", e);
        }

        // 根据用户ID从数据库中查询用户信息
        User user = userMapper.selectById(Integer.parseInt(userId));

        // 用户不存在，抛出异常
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 构建UserDetails对象
        UserDetails userDetails = new UserDetailImpl(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 将认证信息存入上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 继续过滤链
        filterChain.doFilter(request, response);
    }

}
