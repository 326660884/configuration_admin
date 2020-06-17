package cn.cnic.security.configuration.controller;



import cn.cnic.security.common.utils.IPUtils;
import cn.cnic.security.common.utils.JacksonUtils;
import cn.cnic.security.common.utils.R;
import cn.cnic.security.configuration.component.JwtUtils;
import cn.cnic.security.configuration.entity.AppAuthenticationEntity;
import cn.cnic.security.configuration.entity.SysmUserInfo;
import cn.cnic.security.configuration.service.AppAuthenticationService;
import cn.cnic.security.configuration.service.impl.AuthenticationAndAuthorizationServicelImpl;
import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证与授权
 */
@RestController
@Slf4j
public class AuthenticationController {

    @Autowired
    private AppAuthenticationService authenticationService;

    @Autowired
    private AuthenticationAndAuthorizationServicelImpl authenticationAndAuthorizationServicel;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     *
     * @return
     */
    @PostMapping(value = {"sso/authentication"})
    public R authentication(HttpServletRequest request,String code , Integer appKey){
        if(StringUtils.isEmpty(code) && appKey != null){
            return R.error(1,"参数为空");
        }
        /**
         * 1.去科技云认证code
         * 2.1 存在
         * 2.1.1 查看账号是否存在
         * 2.1.2 账号是否有着应用的权限
         *
         * 2.2 不存在
         */

        QueryWrapper<AppAuthenticationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", appKey);
        AppAuthenticationEntity entity = authenticationService.getOne(queryWrapper);
        R r = null;
        //发现appkey不存在
        if(entity == null ){
            log.warn("authentication appKey = {}",appKey);
            return R.error(1,"参数错误appkey");
        }

        try {
            //科技云通行证认证
            AccessToken accessToken = authenticationAndAuthorizationServicel.authentication(request, entity);
            log.info("科技云认证 {} {}", IPUtils.getIpAddr(request), accessToken.toString());
            if(StringUtils.equals("active",accessToken.getUserInfo().getCstnetIdStatus())){
                //通行证账号也就是邮箱
                String cstnetId = accessToken.getUserInfo().getCstnetId();
                //授权
                SysmUserInfo sysmUserInfo = authenticationAndAuthorizationServicel.authorization(cstnetId);
                String userJson = JacksonUtils.obj2String(sysmUserInfo);
                String token = jwtUtils.generateToken(userJson);
                r = R.ok().put("token",token);
            }else {
                r = R.error(5,"帐户未激活");
            }
        } catch (UMTOauthConnectException e) {
            r=R.error(5,"Oauth2.0认证异常");
            log.warn("{}",e);
        } catch (OAuthProblemException e) {
            r=R.error(5,e.getMessage());
            log.warn("{}",e);
        }

        return r;
    }

}
