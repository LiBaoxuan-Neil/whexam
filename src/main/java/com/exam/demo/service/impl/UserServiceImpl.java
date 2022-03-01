package com.exam.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exam.demo.entity.RoleMessage;
import com.exam.demo.entity.Userwx;
import com.exam.demo.mapper.*;
import com.exam.demo.otherEntity.UserPojo;
import org.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exam.demo.entity.User;
import com.exam.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: gaoyk
 * @Date: 2022/2/3 20:23
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private UserwxMapper userwxMapper;

    /**
     * 微信小程序登录服务层
     *
     * @param userwx
     * @return
     */
    @Override
    public Userwx loginWx(Userwx userwx) {
        String s = UserServiceImpl.sendGet("https://api.weixin.qq.com/sns/jscode2session?appid=wxfe52cfbbf230d5ee&secret=3b61d56a43ce72243813b1f71b017c90&js_code=" + userwx.getOpenid() + "&grant_type=authorization_code", "");
        JSONObject json = new JSONObject(s);
        System.err.println(s);
        QueryWrapper<Userwx> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",json.getString("openid"));
//        wrapper.eq("openid","123456");
        Userwx p = userwxMapper.selectOne(wrapper);
        if(p != null){
            Userwx q = p;
            q.setImage(userwx.getImage());
            q.setWxname(userwx.getWxname());
            q.setGender(userwx.getGender());
            userwxMapper.updateById(q);
            return q;
        }else{
            Userwx now = new Userwx(json.getString("openid"), userwx.getGender(), userwx.getImage(), userwx.getWxname());
//            User now = new User(user.getGender(), "123456", 2, user.getImage(), user.getWxname(), 0, "123456");
            userwxMapper.insert(now);
            return now;
        }
    }

    /**
     * Web登录服务层
     *
     * @param user
     * @return
     */
    @Override
    public User loginWeb(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",user.getName());
        User p = userMapper.selectOne(wrapper);

        if (p != null){
            if (p.getPassword().equals(user.getPassword())){
                return p;
            }else {
                return null;
            }
        }
        return null;
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public Boolean updateUser(User user) {
        if(userMapper.updateById(user) == 1){
            return true;
        }
        return false;
    }

    /**
     * 返回所有用户
     *
     * @return
     */
    @Override
    public List<User> findAll() {
        return userMapper.selectList(new LambdaQueryWrapper<>());
    }

    /**
     * 根据Id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public Integer deleteById(Integer id) {
        return userMapper.deleteById(id);
    }

    /**
     * 给用户授权
     *
     * @param
     * @param user_id
     * @return
     */
    @Override
    public Boolean grantRole(Integer user_id) {
        User user = userMapper.selectById(user_id);
        if (user.getRoleId() == 1){
            user.setRoleId(2);
        }else{
            user.setRoleId(1);
        }
        if(userMapper.updateById(user) == 1){
            return true;
        }
        return false;
    }

    /**
     * 返回部分信息
     *
     * @return
     */
    @Override
    public List<UserPojo> findPart() {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<>());
        List<UserPojo> userPojos = new ArrayList<>();
        for (User x : users) {
            UserPojo userPojo = new UserPojo();
            userPojo.setId(x.getId());
            userPojo.setName(x.getName());
            userPojo.setGender(x.getGender());
            userPojo.setPosition(x.getPosition());
            userPojo.setRole(roleMapper.selectById(x.getRoleId()).getName());
//            System.err.println(x.getName());
            String dname = departmentMapper.selectById(x.getDepartmentId()).getName();
            userPojo.setDepartment(dname);
            userPojo.setAddress(x.getAddress());
            userPojo.setEmail(x.getEmail());
            userPojo.setTele(x.getTele());
            userPojo.setTime(x.getTime());
            userPojo.setWxname(x.getWxname());
            userPojo.setNums(x.getNums());
            userPojo.setIdentity(x.getIdentity());
            userPojos.add(userPojo);
        }
        return userPojos;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }

        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 通过部分条件查找用户
     * @param name
     * @param nums
     * @param department
     * @param address
     * @return
     */
    @Override
    public List<RoleMessage> findUser(String name, String nums, String department, String address) {
        Integer temp=userMapper.findbydepartment(department);
        if(temp==null&&department.length()!=0)
            temp=-2;
        if(name.length()==0)
            name=null;
        if(nums.length()==0)
           nums=null;
        if(department.length()==0||department==null||temp==null){
           temp=-1;
        }
        if(address.length()==0)
            address=null;
        return userMapper.findUser(name, nums, temp, address);


    }

    /**
     * 根据工号校验
     *
     * @param userwx
     * @return
     */
    @Override
    public User check(Userwx userwx) {
        String openid = userwx.getOpenid();
        QueryWrapper<Userwx> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        Userwx now = userwxMapper.selectOne(wrapper);
        now.setNums(userwx.getNums());
        userwxMapper.updateById(now);
        QueryWrapper<User> query = new QueryWrapper<>();
        wrapper.eq("nums",userwx.getNums());
        User user = userMapper.selectOne(query);
        return user;
    }
}
