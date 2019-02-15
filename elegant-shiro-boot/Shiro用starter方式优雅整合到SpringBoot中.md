# Shiro用starter方式优雅整合到SpringBoot中

> 网上找到大部分文章都是以前SpringMVC下的整合方式，很多人都不知道shiro提供了官方的starter可以方便地跟SpringBoot整合。本文介绍我的3种整合思路：1.完全使用注解；2.完全使用url配置；3.url配置和注解混用，url配置负责鉴权控制，注解负责权限控制。三种方式各有优劣，需考虑实际应用场景使用。

## 代码

Talk is cheap, show you my code: [elegant-shiro-boot](https://github.com/CaiBaoHong/elegant-shiro-boot)
这个工程使用gradle构建，有三个子工程：

- demo1演示只用注解来做鉴权授权
- demo2演示只用url配置来做鉴权授权
- demo3演示两种方式结合，url配置负责控制鉴权，注解配置负责控制授权。

## 如何整合

请看shiro官网关于springboot整合shiro的链接：[Integrating Apache Shiro into Spring-Boot Applications](https://shiro.apache.org/spring-boot.html)

> 可笑的是，我自己直接上去官网找，找来找去都找不到这一页的文档，而是通过google找出来的。

这篇文档的介绍也相当简单。我们只需要按照文档说明，引入`shiro-spring-boot-starter`，然后在spring容器中注入一个我们自定义的`Realm`，shiro通过这个realm就可以知道如何获取用户信息来处理`鉴权（Authentication）`，如何获取用户角色、权限信息来处理`授权（Authorization）`。

> ps：鉴权可以理解成判断一个用户是否已登录的过程，授权可以理解成判断一个已登录用户是否有访问权限的过程。

**整合过程：**
1.引入starter，我的是用gradle做项目构建的，maven也是引入对应的依赖即可：

```
dependencies {
    //spring boot的starter
    compile 'org.springframework.boot:spring-boot-starter-web'
    compile 'org.springframework.boot:spring-boot-starter-aop'
    compile 'org.springframework.boot:spring-boot-devtools'
    testCompile 'org.springframework.boot:spring-boot-starter-test'
    //shiro
    compile 'org.apache.shiro:shiro-spring-boot-web-starter:1.4.0'
}
```

2.编写自定义realm

User.java（其它RBAC模型请看github上的代码com.abc.entity包下的类）

```java
public class User {

    private Long uid;       // 用户id
    private String uname;   // 登录名，不可改
    private String nick;    // 用户昵称，可改
    private String pwd;     // 已加密的登录密码
    private String salt;    // 加密盐值
    private Date created;   // 创建时间
    private Date updated;   // 修改时间
    private Set<String> roles = new HashSet<>();    //用户所有角色值，用于shiro做角色权限的判断
    private Set<String> perms = new HashSet<>();    //用户所有权限值，用于shiro做资源权限的判断
    //getters and setters...
}
```

UserService.java

```java
@Service
public class UserService {

    /**
     * 模拟查询返回用户信息
     * @param uname
     * @return
     */
    public User findUserByName(String uname){
        User user = new User();
        user.setUname(uname);
        user.setNick(uname+"NICK");
        user.setPwd("J/ms7qTJtqmysekuY8/v1TAS+VKqXdH5sB7ulXZOWho=");//密码明文是123456
        user.setSalt("wxKYXuTPST5SG0jMQzVPsg==");//加密密码的盐值
        user.setUid(new Random().nextLong());//随机分配一个id
        user.setCreated(new Date());
        return user;
    }
}
```

RoleService.java

```java
@Service
public class RoleService {

    /**
     * 模拟根据用户id查询返回用户的所有角色，实际查询语句参考：
     * SELECT r.rval FROM role r, user_role ur
     * WHERE r.rid = ur.role_id AND ur.user_id = #{userId}
     * @param uid
     * @return
     */
    public Set<String> getRolesByUserId(Long uid){
        Set<String> roles = new HashSet<>();
        //三种编程语言代表三种角色：js程序员、java程序员、c++程序员
        roles.add("js");
        roles.add("java");
        roles.add("cpp");
        return roles;
    }

}
```

PermService.java

```java
@Service
public class PermService {

    /**
     * 模拟根据用户id查询返回用户的所有权限，实际查询语句参考：
     * SELECT p.pval FROM perm p, role_perm rp, user_role ur
     * WHERE p.pid = rp.perm_id AND ur.role_id = rp.role_id
     * AND ur.user_id = #{userId}
     * @param uid
     * @return
     */
    public Set<String> getPermsByUserId(Long uid){
        Set<String> perms = new HashSet<>();
        //三种编程语言代表三种角色：js程序员、java程序员、c++程序员
        //js程序员的权限
        perms.add("html:edit");
        //c++程序员的权限
        perms.add("hardware:debug");
        //java程序员的权限
        perms.add("mvn:install");
        perms.add("mvn:clean");
        perms.add("mvn:test");
        return perms;
    }

}
```

CustomRealm.java

```java
/**
 * 这个类是参照JDBCRealm写的，主要是自定义了如何查询用户信息，如何查询用户的角色和权限，如何校验密码等逻辑
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermService permService;

    //告诉shiro如何根据获取到的用户信息中的密码和盐值来校验密码
    {
        //设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher hashMatcher = new HashedCredentialsMatcher();
        hashMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashMatcher.setStoredCredentialsHexEncoded(false);
        hashMatcher.setHashIterations(1024);
        this.setCredentialsMatcher(hashMatcher);
    }


    //定义如何获取用户的角色和权限的逻辑，给shiro做权限判断
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        User user = (User) getAvailablePrincipal(principals);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        System.out.println("获取角色信息："+user.getRoles());
        System.out.println("获取权限信息："+user.getPerms());
        info.setRoles(user.getRoles());
        info.setStringPermissions(user.getPerms());
        return info;
    }

    //定义如何获取用户信息的业务逻辑，给shiro做登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        User userDB = userService.findUserByName(username);


        if (userDB == null) {
            throw new UnknownAccountException("No account found for admin [" + username + "]");
        }

        //查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方
        //SecurityUtils.getSubject().getPrincipal()就能拿出用户的所有信息，包括角色和权限
        Set<String> roles = roleService.getRolesByUserId(userDB.getUid());
        Set<String> perms = permService.getPermsByUserId(userDB.getUid());
        userDB.getRoles().addAll(roles);
        userDB.getPerms().addAll(perms);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDB, userDB.getPwd(), getName());
        if (userDB.getSalt() != null) {
            info.setCredentialsSalt(ByteSource.Util.bytes(userDB.getSalt()));
        }

        return info;

    }

}
```

3.使用注解或url配置，来控制鉴权授权

请参照官网的示例：

```java
//url配置
@Bean
public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
    
    // logged in users with the 'admin' role
    chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
    
    // logged in users with the 'document:read' permission
    chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
    
    // all other paths require a logged in user
    chainDefinition.addPathDefinition("/**", "authc");
    return chainDefinition;
}
//注解配置
@RequiresPermissions("document:read")
public void readDocument() {
    ...
}
```

4.解决spring aop和注解配置一起使用的bug。如果您在使用shiro注解配置的同时，引入了spring aop的starter，会有一个奇怪的问题，导致shiro注解的请求，不能被映射，需加入以下配置：

```java
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
```

## 思路1：只用注解控制鉴权授权

使用注解的优点是控制的粒度细，并且非常适合用来做基于资源的权限控制。

> 关于基于资源的权限控制，建议看看这篇文章：[The New RBAC: Resource-Based Access Control](https://stormpath.com/blog/new-rbac-resource-based-access-control) 

只用注解的话非常简单。我们只需要使用url配置配置一下所以请求路径都可以匿名访问：

```java
    //在 ShiroConfig.java 中的代码：
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        // 由于demo1展示统一使用注解做访问控制，所以这里配置所有请求路径都可以匿名访问
        chain.addPathDefinition("/**", "anon"); // all paths are managed via annotations

        // 这另一种配置方式。但是还是用上面那种吧，容易理解一点。
        // or allow basic authentication, but NOT require it.
        // chainDefinition.addPathDefinition("/**", "authcBasic[permissive]");
        return chain;
    }
```

然后在控制器类上使用shiro提供的种注解来做控制：

| 注解                    | 功能                                 |
| ----------------------- | ------------------------------------ |
| @RequiresGuest          | 只有游客可以访问                     |
| @RequiresAuthentication | 需要登录才能访问                     |
| @RequiresUser           | 已登录的用户或“记住我”的用户能访问   |
| @RequiresRoles          | 已登录的用户需具有指定的角色才能访问 |
| @RequiresPermissions    | 已登录的用户需具有指定的权限才能访问 |

代码示例：（更详细的请参考github代码的demo1）

```java
/**
 * created by CaiBaoHong at 2018/4/18 15:51<br>
 *     测试shiro提供的注解及功能解释
 */
@RestController
@RequestMapping("/t1")
public class Test1Controller {

    // 由于TestController类上没有加@RequiresAuthentication注解，
    // 不要求用户登录才能调用接口。所以hello()和a1()接口都是可以匿名访问的
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }

    // 游客可访问，这个有点坑，游客的意思是指：subject.getPrincipal()==null
    // 所以用户在未登录时subject.getPrincipal()==null，接口可访问
    // 而用户登录后subject.getPrincipal()！=null，接口不可访问
    @RequiresGuest
    @GetMapping("/guest")
    public String guest() {
        return "@RequiresGuest";
    }

    // 已登录用户才能访问，这个注解比@RequiresUser更严格
    // 如果用户未登录调用该接口，会抛出UnauthenticatedException
    @RequiresAuthentication
    @GetMapping("/authn")
    public String authn() {
        return "@RequiresAuthentication";
    }

    // 已登录用户或“记住我”的用户可以访问
    // 如果用户未登录或不是“记住我”的用户调用该接口，UnauthenticatedException
    @RequiresUser
    @GetMapping("/user")
    public String user() {
        return "@RequiresUser";
    }

    // 要求登录的用户具有mvn:build权限才能访问
    // 由于UserService模拟返回的用户信息中有该权限，所以这个接口可以访问
    // 如果没有登录，UnauthenticatedException
    @RequiresPermissions("mvn:install")
    @GetMapping("/mvnInstall")
    public String mvnInstall() {
        return "mvn:install";
    }

    // 要求登录的用户具有mvn:build权限才能访问
    // 由于UserService模拟返回的用户信息中【没有】该权限，所以这个接口【不可以】访问
    // 如果没有登录，UnauthenticatedException
    // 如果登录了，但是没有这个权限，会报错UnauthorizedException
    @RequiresPermissions("gradleBuild")
    @GetMapping("/gradleBuild")
    public String gradleBuild() {
        return "gradleBuild";
    }

    // 要求登录的用户具有js角色才能访问
    // 由于UserService模拟返回的用户信息中有该角色，所以这个接口可访问
    // 如果没有登录，UnauthenticatedException
    @RequiresRoles("js")
    @GetMapping("/js")
    public String js() {
        return "js programmer";
    }

    // 要求登录的用户具有js角色才能访问
    // 由于UserService模拟返回的用户信息中有该角色，所以这个接口可访问
    // 如果没有登录，UnauthenticatedException
    // 如果登录了，但是没有该角色，会抛出UnauthorizedException
    @RequiresRoles("python")
    @GetMapping("/python")
    public String python() {
        return "python programmer";
    }

}
```

## 思路2：只用url配置控制鉴权授权

shiro提供和多个默认的过滤器，我们可以用这些过滤器来配置控制指定url的权限：

| 配置缩写          | 对应的过滤器                   | 功能                                                         |
| ----------------- | ------------------------------ | ------------------------------------------------------------ |
| anon              | AnonymousFilter                | 指定url可以匿名访问                                          |
| authc             | FormAuthenticationFilter       | 指定url需要form表单登录，默认会从请求中获取`username`、`password`,`rememberMe`等参数并尝试登录，如果登录不了就会跳转到loginUrl配置的路径。我们也可以用这个过滤器做默认的登录逻辑，但是一般都是我们自己在控制器写登录逻辑的，自己写的话出错返回的信息都可以定制嘛。 |
| authcBasic        | BasicHttpAuthenticationFilter  | 指定url需要basic登录                                         |
| logout            | LogoutFilter                   | 登出过滤器，配置指定url就可以实现退出功能，非常方便          |
| noSessionCreation | NoSessionCreationFilter        | 禁止创建会话                                                 |
| perms             | PermissionsAuthorizationFilter | 需要指定权限才能访问                                         |
| port              | PortFilter                     | 需要指定端口才能访问                                         |
| rest              | HttpMethodPermissionFilter     | 将http请求方法转化成相应的动词来构造一个权限字符串，这个感觉意义不大，有兴趣自己看源码的注释 |
| roles             | RolesAuthorizationFilter       | 需要指定角色才能访问                                         |
| ssl               | SslFilter                      | 需要https请求才能访问                                        |
| user              | UserFilter                     | 需要已登录或“记住我”的用户才能访问                           |

在spring容器中使用`ShiroFilterChainDefinition`来控制所有url的鉴权和授权。优点是配置粒度大，对多个Controller做鉴权授权的控制。下面是例子，具体可以看github代码的demo2：

```java
@Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();

        /**
         * 这里小心踩坑！我在application.yml中设置的context-path: /api/v1
         * 但经过实际测试，过滤器的过滤路径，是context-path下的路径，无需加上"/api/v1"前缀
         */

        //访问控制
        chain.addPathDefinition("/user/login", "anon");//可以匿名访问
        chain.addPathDefinition("/page/401", "anon");//可以匿名访问
        chain.addPathDefinition("/page/403", "anon");//可以匿名访问
        chain.addPathDefinition("/t4/hello", "anon");//可以匿名访问

        chain.addPathDefinition("/t4/changePwd", "authc");//需要登录
        chain.addPathDefinition("/t4/user", "user");//已登录或“记住我”的用户可以访问
        chain.addPathDefinition("/t4/mvnBuild", "authc,perms[mvn:install]");//需要mvn:build权限
        chain.addPathDefinition("/t4/gradleBuild", "authc,perms[gradle:build]");//需要gradle:build权限
        chain.addPathDefinition("/t4/js", "authc,roles[js]");//需要js角色
        chain.addPathDefinition("/t4/python", "authc,roles[python]");//需要python角色

        // shiro 提供的登出过滤器，访问指定的请求，就会执行登录，默认跳转路径是"/"，或者是"shiro.loginUrl"配置的内容
        // 由于application-shiro.yml中配置了 shiro:loginUrl: /page/401，返回会返回对应的json内容
        // 可以结合/user/login和/t1/js接口来测试这个/t4/logout接口是否有效
        chain.addPathDefinition("/t4/logout", "anon,logout");

        //其它路径均需要登录
        chain.addPathDefinition("/**", "authc");

        return chain;
    }
```

## 思路3：二者结合，url配置控制鉴权，注解控制授权

就个人而言，我是非常喜欢注解方式的。但是两种配置方式灵活结合，才是适应不同应用场景的最佳实践。只用注解或只用url配置，会带来一些比较累的工作。

我举两个场景：
**场景1**
假如我是写系统后台管理系统的，而且我的java后台是一个纯粹返回json数据的后台，不会做页面跳转的工作。那我们后台管理系统一般都是全部接口都需要登录才能访问。如果只用注解，我需要在每个Controller上加上`@RequiresAuthentication`来声明每个Controller下每个方法都需要登录才能访问。这样显得有点麻烦，而且日后再加Controller，还是要加上这个注解，万一忘记加了就会出错。这时候其实用url配置的方式就可以配置全部请求都需要登录才能访问：`chain.addPathDefinition("/**", "authc");`

**场景2**
假如我是写商城的前台的，而且我的java后台是一个纯粹返回json数据的后台，但是这些接接口中，在同一个Controller下，有些是可以匿名访问的，有些是需要登录才能访问的，有些是需要特定角色、权限才能访问的。如果只用url配置，每个url都需要配置，而且容易配置错，粒度不好把控。

所以我的想法是：`用url配置控制鉴权，实现粗粒度控制；用注解控制授权，实现细粒度控制`。

下面是示例代码（详细的请看github代码的demo3）：
ShiroConfig.java

```java
@Configuration
public class ShiroConfig {

    //注入自定义的realm，告诉shiro如何获取用户信息来做登录或权限控制
    @Bean
    public Realm realm() {
        return new CustomRealm();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        creator.setUsePrefix(true);
        return creator;
    }

    /**
     * 这里统一做鉴权，即判断哪些请求路径需要用户登录，哪些请求路径不需要用户登录。
     * 这里只做鉴权，不做权限控制，因为权限用注解来做。
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        //哪些请求可以匿名访问
        chain.addPathDefinition("/user/login", "anon");
        chain.addPathDefinition("/page/401", "anon");
        chain.addPathDefinition("/page/403", "anon");
        chain.addPathDefinition("/t5/hello", "anon");
        chain.addPathDefinition("/t5/guest", "anon");

        //除了以上的请求外，其它请求都需要登录
        chain.addPathDefinition("/**", "authc");
        return chain;
    }
}
```

PageController.java

```java
@RestController
@RequestMapping("/page")
public class PageController {

    // shiro.loginUrl映射到这里，我在这里直接抛出异常交给GlobalExceptionHandler来统一返回json信息，
    // 您也可以在这里json，不过这样子就跟GlobalExceptionHandler中返回的json重复了。
    @RequestMapping("/401")
    public Json page401() {
        throw new UnauthenticatedException();
    }

    // shiro.unauthorizedUrl映射到这里。由于demo3统一约定了url方式只做鉴权控制，不做权限访问控制，
    // 也就是说在ShiroConfig中如果没有roles[js],perms[mvn:install]这样的权限访问控制配置的话，
    // 是不会跳转到这里的。
    @RequestMapping("/403")
    public Json page403() {
        throw new UnauthorizedException();
    }

    @RequestMapping("/index")
    public Json pageIndex() {
        return new Json("index",true,1,"index page",null);
    }


}
```

GlobalExceptionHandler.java

```java
/**
 * 统一捕捉shiro的异常，返回给前台一个json信息，前台根据这个信息显示对应的提示，或者做页面的跳转。
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //不满足@RequiresGuest注解时抛出的异常信息
    private static final String GUEST_ONLY = "Attempting to perform a guest-only operation";


    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public Json handleShiroException(ShiroException e) {
        String eName = e.getClass().getSimpleName();
        log.error("shiro执行出错：{}",eName);
        return new Json(eName, false, Codes.SHIRO_ERR, "鉴权或授权过程出错", null);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public Json page401(UnauthenticatedException e) {
        String eMsg = e.getMessage();
        if (StringUtils.startsWithIgnoreCase(eMsg,GUEST_ONLY)){
            return new Json("401", false, Codes.UNAUTHEN, "只允许游客访问，若您已登录，请先退出登录", null)
                    .data("detail",e.getMessage());
        }else{
            return new Json("401", false, Codes.UNAUTHEN, "用户未登录", null)
                    .data("detail",e.getMessage());
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Json page403() {
        return new Json("403", false, Codes.UNAUTHZ, "用户没有访问权限", null);
    }

}
```

TestController.java

​                     

```java
@RestController
@RequestMapping("/t5")
public class Test5Controller {

    // 由于ShiroConfig中配置了该路径可以匿名访问，所以这接口不需要登录就能访问
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }

    // 如果ShiroConfig中没有配置该路径可以匿名访问，所以直接被登录过滤了。
    // 如果配置了可以匿名访问，那这里在没有登录的时候可以访问，但是用户登录后就不能访问
    @RequiresGuest
    @GetMapping("/guest")
    public String guest() {
        return "@RequiresGuest";
    }

    @RequiresAuthentication
    @GetMapping("/authn")
    public String authn() {
        return "@RequiresAuthentication";
    }
```