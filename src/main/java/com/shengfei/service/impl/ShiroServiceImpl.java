package com.shengfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shengfei.dto.UserSearchDTO;
import com.shengfei.entity.SysToken;
import com.shengfei.entity.User;
import com.shengfei.exception.ServiceException;
import com.shengfei.mapper.TokenMapper;
import com.shengfei.mapper.UserMapper;
import com.shengfei.service.ShiroService;
import com.shengfei.shiro.auth.TokenGenerator;
import com.shengfei.shiro.vo.PageBean;
import com.shengfei.utils.ObjectUtil;
import com.shengfei.utils.ValidatorUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShiroServiceImpl implements ShiroService
{

    //12小时后失效
    private final static int EXPIRE = 12;
    @Resource
    UserMapper userMapper;
    @Resource
    TokenMapper tokenMapper;

    @Override
    public SysToken findByToken(String accessToken) {
        QueryWrapper<SysToken> wrapper = new QueryWrapper<>();
        wrapper.eq("token",accessToken);
        return tokenMapper.selectOne(wrapper);
    }

    @Override
    public User findByUserId(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User findByUserMobile(String mobile) {
        return userMapper.findByUserMobile(mobile);
    }

    @Override
    public Map<String, Object> createToken(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = now.plusHours(EXPIRE);
        //判断是否生成过token
        SysToken sysToken = tokenMapper.findByUserId(userId);

        if(sysToken!=null) {
            tokenMapper.updateById(ObjectUtil.initObject(new SysToken())
                    .init(v -> v.setUserId(userId))
                    .init(v -> v.setToken(token))
                    .init(v -> v.setUpdateTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant())))
                    .init(v -> v.setExpireTime(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant())))
                    .getObject());
        }else {
            tokenMapper.insert(ObjectUtil.initObject(new SysToken())
                    .init(v -> v.setUserId(userId))
                    .init(v -> v.setToken(token))
                    .init(v -> v.setUpdateTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant())))
                    .init(v -> v.setExpireTime(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant())))
                    .getObject());
        }

        User user = userMapper.selectById(userId);
        user.setLastVisit(new Date());
        user.setLoginCount(user.getLoginCount() + 1);
        userMapper.updateById(user);

//        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

//        String ip = getIpAddress(request);
//        Map<String, JSONObject> map = getAddressByIp(ip);

//        if(map != null) {
//            logMapper.insert(ObjectUtil.initObject(new SysLog())
//                    .init(v -> v.setMobile(user.getMobile()))
//                    .init(v -> v.setIp(ip))
//                    .init(v -> v.setLoginTime(new Date()))
//                    .init(v -> v.setProvince(map.get("address").getString("province")))
//                    .init(v -> v.setCity(map.get("address").getString("city")))
//                    .init(v -> v.setOs(userAgent.getOperatingSystem().getName()))
//                    .init(v -> v.setBrowser(userAgent.getBrowser().getName()))
//                    .init(v -> v.setPointX(map.get("point").getBigDecimal("x")))
//                    .init(v -> v.setPointY(map.get("point").getBigDecimal("y")))
//                    .getObject());
//        } else {
//            logMapper.insert(ObjectUtil.initObject(new SysLog())
//                    .init(v -> v.setMobile(user.getMobile()))
//                    .init(v -> v.setIp(ip))
//                    .init(v -> v.setLoginTime(new Date()))
//                    .init(v -> v.setOs(userAgent.getOperatingSystem().getName()))
//                    .init(v -> v.setBrowser(userAgent.getBrowser().getName()))
//                    .getObject());
//        }
        result.put("token", token);
        result.put("expire", expireTime);
        return result;
    }


    @Override
    public void logout(String token) {
        //生成一个token
        String token_ = TokenGenerator.generateValue();
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        //过期时间
        LocalDateTime expireTime = now.plusHours(EXPIRE);
        Subject subject = SecurityUtils.getSubject();

        User user = new User();

        BeanUtils.copyProperties(subject.getPrincipal(),user);

        tokenMapper.updateById(ObjectUtil.initObject(new SysToken())
                .init(v -> v.setUserId(user.getId()))
                .init(v -> v.setToken(token))
                .init(v -> v.setUpdateTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant())))
                .init(v -> v.setExpireTime(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant())))
                .getObject());
    }

    @Override
    public PageInfo<User> page(PageBean page, UserSearchDTO user) {
        QueryWrapper<User> queryWrapper  = new QueryWrapper<>();
        if (!ValidatorUtils.empty(user.getName())){
            queryWrapper.like("name",user.getName());
        }
        if (!ValidatorUtils.empty(user.getCompanyName())){
            queryWrapper.like("company_name",user.getCompanyName());
        }
        if (!ValidatorUtils.empty(user.getIdCard())){
            queryWrapper.like("id_card",user.getIdCard());
        }
        if (!ValidatorUtils.empty(user.getMobile())){
            queryWrapper.like("mobile",user.getMobile());
        }
        if (!ValidatorUtils.empty(user.getStation())){
            queryWrapper.like("station",user.getStation());
        }
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<User> userList = userMapper.selectList(queryWrapper);
        return new PageInfo<User>(userList);
    }


    @Override
    @Transactional
    public Boolean createUser(User user) {
        if(userMapper.findByUserMobile(user.getMobile()) != null) {
            throw new ServiceException("用户已经注册，请去登录");
        }
        userMapper.insert(ObjectUtil.initObject(user)
                .init(v-> v.setStation(user.getStation()))
                .init(v-> v.setCompanyName(user.getCompanyName()))
                .init(v -> v.setMobile(user.getMobile()))
                .init(v -> v.setPassword(user.getPassword()))
                .init(v -> v.setName(user.getName()))
                .init(v -> v.setCreateDate(new Date()))
                .init(v -> v.setLastVisit(new Date()))
                .init(v -> v.setLoginCount(0))
                .init(v -> v.setState("1"))
                .getObject());
        return true;
    }

//    public static String getIpAddress(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("http_client_ip");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip != null && ip.indexOf(",") != -1) {
//            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
//        }
//        if ("0:0:0:0:0:0:0:1".equals(ip)) {
//            ip = "127.0.0.1";
//        }
//        return ip;
//    }
//
//    /**
//     * 根据IP 查询城市信息
//     */
//    public static Map<String,JSONObject> getAddressByIp(String ip) {
//        JSONObject object = HttpUtils.requestGet("http://api.map.baidu.com/location/ip?ak=cTYtHPV1spdnWgFSeKfXqjTh&coor=bd09ll&ip=" + ip).getJSONObject("content");
//        if (object != null) {
//            Map<String,JSONObject> map = new HashMap<>();
//            map.put("point"  , object.getJSONObject("point"));
//            map.put("address", object.getJSONObject("address_detail"));
//            return map;
//        }
//        return null;
//    }

}
