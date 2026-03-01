package com.itheima;

import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TliasWebManagementApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("你好");
    }
      @Test
    public void testCode() {
          Map<String, Object> map = new HashMap<>();
          map.put("username","admin");
          map.put("id","1001");
          String token= JwtUtils.generateJwt(map);
          System.out.println(token);
          //eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEwMDEiLCJ1c2VybmFtZSI6ImFkbWluIiwiZXhwIjoxNzMzMDU2NzQ2fQ.lEB06z69DcM8bVKKTAURavEhBnavlPL9FLFnrfHZ6ds
      }
      @Test
    public void testParseJwt(){
        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEwMDEiLCJ1c2VybmFtZSI6ImFkbWluIiwiZXhwIjoxNzMzMDU2NzQ2fQ.lEB06z69DcM8bVKKTAURavEhBnavlPL9FLFnrfHZ6ds";
        Claims claims=JwtUtils.parseJWT(jwt);
          System.out.println(claims);
      }
}
