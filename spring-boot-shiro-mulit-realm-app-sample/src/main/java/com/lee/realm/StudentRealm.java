package com.lee.realm;

import com.lee.bean.Student;
import com.lee.service.StudentService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Adam
 * @Description: 总服务器管理员shiro登陆Realm
 * @Title: DeviceController
 * @ProjectName wanmo
 * @date 2018/8/17 16:51
 */
public class StudentRealm extends AuthorizingRealm {

  @Autowired
  private StudentService studentService;

  /**
   * @return org.apache.shiro.authz.AuthorizationInfo
   * @Author Adam
   * @Description 权限配置
   * @Date 14:29 2018/9/28
   * @Param [principals]
   **/
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

    System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    return authorizationInfo;
  }

  /**
   * @return org.apache.shiro.authc.AuthenticationInfo
   * @Author Adam
   * @Description 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
   * @Date 14:24 2018/9/28
   * @Param [token]
   **/
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
    //获取用户的输入的账号.
    String account = (String) token.getPrincipal();
    Object credentials = token.getCredentials();
    System.out.println("credentials=" + credentials);
    //通过username从数据库中查找对象
    Student student = studentService.getStudentByAccount(account);
    System.out.println("----->>student=" + student);
    if (student == null) {
      return null;
    }
    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        student,
        student.getPassword(),
        getName()
    );
    return authenticationInfo;
  }

}