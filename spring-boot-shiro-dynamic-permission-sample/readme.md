https://blog.csdn.net/haoyuyang/article/details/80036989

用户名：admin

密码：123456

本文章是介绍SpringBoot整合Apache Shiro，并实现在项目启动时从数据库中读取权限列表，在对角色进行增删改时，动态更新权限以及在分布式环境下的Session共享，Session共享使用的是shiro-redis框架，是根据真实项目写的一个Demo。网上有很多关于Shiro相关的文章，但是大多都是零零散散的，要么就只介绍上述功能中的一两个功能，要么就是缺少配置相关的内容。所以，我整理了一下，给大家一个参考的。废话不多说，直接上代码。关于Shiro相关的概念，大家可以在网上自行百度。

一、使用到的相关的表

    CREATE TABLE `t_user` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `username` varchar(32) DEFAULT '' COMMENT '用户名',
      `password` varchar(255) DEFAULT '' COMMENT '密码',
      `role_id` int(11) DEFAULT '0' COMMENT '角色id',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
     
    CREATE TABLE `t_role` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `role_name` varchar(64) DEFAULT '' COMMENT '角色名称',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
     
    CREATE TABLE `t_authority` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `authority_name` varchar(64) DEFAULT '' COMMENT '权限名称',
      `icon` varchar(255) DEFAULT '' COMMENT '图标',
      `uri` varchar(255) DEFAULT '' COMMENT '请求uri',
      `permission` varchar(1000) DEFAULT '',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
     
    CREATE TABLE `t_role_authority` (
      `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',
      `authority_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限id',
      PRIMARY KEY (`role_id`,`authority_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

二、初始化数据

    INSERT INTO `spring-boot-shiro`.`t_user` (`id`, `username`, `password`, `role_id`) VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '1');
    INSERT INTO `spring-boot-shiro`.`t_user` (`id`, `username`, `password`, `role_id`) VALUES ('2', 'guest', 'e10adc3949ba59abbe56e057f20f883e', '2');
     
    INSERT INTO `spring-boot-shiro`.`t_role` (`id`, `role_name`) VALUES ('1', 'admin');
    INSERT INTO `spring-boot-shiro`.`t_role` (`id`, `role_name`) VALUES ('2', '普通用户');
     
    INSERT INTO `spring-boot-shiro`.`t_authority` (`id`, `authority_name`, `icon`, `uri`, `permission`) VALUES ('1', '查询用户列表', '', '/user/list', 'roles[admin,普通用户]');
    INSERT INTO `spring-boot-shiro`.`t_authority` (`id`, `authority_name`, `icon`, `uri`, `permission`) VALUES ('2', '查询角色列表', '', '/role/list', 'roles[admin]');
     
    INSERT INTO `spring-boot-shiro`.`t_role_authority` (`role_id`, `authority_id`) VALUES ('1', '1');
    INSERT INTO `spring-boot-shiro`.`t_role_authority` (`role_id`, `authority_id`) VALUES ('1', '2');
    INSERT INTO `spring-boot-shiro`.`t_role_authority` (`role_id`, `authority_id`) VALUES ('2', '1');

三、pom.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
     
        <groupId>com.example</groupId>
        <artifactId>spring-boot-shiro</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <packaging>jar</packaging>
     
        <name>spring-boot-shiro</name>
        <description>Demo project for Spring Boot</description>
     
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>1.5.3.RELEASE</version>
            <relativePath/>
        </parent>
     
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
            <java.version>1.8</java.version>
        </properties>
     
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>1.3.2</version>
            </dependency>
     
            <!-- shiro -->
            <dependency>
                <groupId>org.apache.shiro</groupId>
                <artifactId>shiro-spring</artifactId>
                <version>1.4.0</version>
            </dependency>
            <dependency>
                <groupId>org.apache.shiro</groupId>
                <artifactId>shiro-core</artifactId>
                <version>1.4.0</version>
            </dependency>
            <dependency>
                <groupId>org.crazycake</groupId>
                <artifactId>shiro-redis</artifactId>
                <version>3.0.0</version>
            </dependency>
            <!-- mysql -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <scope>runtime</scope>
            </dependency>
            <!-- druid -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.1.9</version>
            </dependency>
     
            <dependency>
                <groupId>redis.clients</groupId>
                <artifactId>jedis</artifactId>
                <version>2.9.0</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-redis</artifactId>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.47</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>
     
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </project>

四、Shiro和自定义MessageConverter的配置Bean

    @Configuration
    public class ShiroConfig {
     
        private static final String CACHE_KEY = "shiro:cache:";
        private static final String SESSION_KEY = "shiro:session:";
        private static final String NAME = "custom.name";
        private static final String VALUE = "/";
     
        @Bean
        public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroService shiroService) {
            ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
            shiroFilter.setSecurityManager(securityManager);
            Map<String, Filter> filterMap = new LinkedHashMap<>(1);
            filterMap.put("roles", rolesAuthorizationFilter());
            shiroFilter.setFilters(filterMap);
            shiroFilter.setFilterChainDefinitionMap(shiroService.loadFilterChainDefinitions());
            return shiroFilter;
        }
     
        @Bean
        public CustomRolesAuthorizationFilter rolesAuthorizationFilter() {
            return new CustomRolesAuthorizationFilter();
        }
     
        @Bean("securityManager")
        public SecurityManager securityManager(Realm realm, SessionManager sessionManager, RedisCacheManager redisCacheManager) {
            DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
            manager.setSessionManager(sessionManager);
            manager.setCacheManager(redisCacheManager);
            manager.setRealm(realm);
            return manager;
        }


​     
        @Bean("defaultAdvisorAutoProxyCreator")
        @DependsOn("lifecycleBeanPostProcessor")
        public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
            DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
            //指定强制使用cglib为action创建代理对象
            defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
            return defaultAdvisorAutoProxyCreator;
        }
     
        @Bean("lifecycleBeanPostProcessor")
        public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
            return new LifecycleBeanPostProcessor();
        }
     
        @Bean("delegatingFilterProxy")
        public FilterRegistrationBean delegatingFilterProxy(){
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
            DelegatingFilterProxy proxy = new DelegatingFilterProxy();
            proxy.setTargetFilterLifecycle(true);
            proxy.setTargetBeanName("shiroFilter");
            filterRegistrationBean.setFilter(proxy);
            return filterRegistrationBean;
        }
     
        /**
         * Redis集群使用RedisClusterManager，单个Redis使用RedisManager
         * @param redisProperties
         * @return
         */
        @Bean
        public RedisClusterManager redisManager(RedisProperties redisProperties) {
            RedisClusterManager redisManager = new RedisClusterManager();
            redisManager.setHost(redisProperties.getHost());
            redisManager.setPassword(redisProperties.getPassword());
            return redisManager;
        }
     
        @Bean
        public RedisCacheManager redisCacheManager(RedisClusterManager redisManager) {
            RedisCacheManager redisCacheManager = new RedisCacheManager();
            redisCacheManager.setRedisManager(redisManager);
            redisCacheManager.setExpire(86400);
            redisCacheManager.setKeyPrefix(CACHE_KEY);
            return redisCacheManager;
        }
     
        @Bean
        public RedisSessionDAO redisSessionDAO(RedisClusterManager redisManager) {
            RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
            redisSessionDAO.setExpire(86400);
            redisSessionDAO.setKeyPrefix(SESSION_KEY);
            redisSessionDAO.setRedisManager(redisManager);
            return redisSessionDAO;
        }
     
        @Bean
        public DefaultWebSessionManager sessionManager(RedisSessionDAO sessionDAO, SimpleCookie simpleCookie) {
            DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
            sessionManager.setSessionDAO(sessionDAO);
            sessionManager.setSessionIdCookieEnabled(true);
            sessionManager.setSessionIdCookie(simpleCookie);
            return sessionManager;
        }
     
        @Bean
        public SimpleCookie simpleCookie() {
            SimpleCookie simpleCookie = new SimpleCookie();
            simpleCookie.setName(NAME);
            simpleCookie.setValue(VALUE);
            return simpleCookie;
        }
     
        @Bean
        public Realm realm(RedisCacheManager redisCacheManager) {
            PasswordRealm realm = new PasswordRealm();
            realm.setCacheManager(redisCacheManager);
            realm.setAuthenticationCachingEnabled(false);
            realm.setAuthorizationCachingEnabled(false);
            return realm;
        }
    }


    @Configuration
    public class MessageConverterConfig {
        @Bean
        public HttpMessageConverters fastJsonHttpMessageConverter() {
            //定义一个转换消息的对象
            FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
            //添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
            FastJsonConfig config = new FastJsonConfig();
            config.setSerializerFeatures(SerializerFeature.PrettyFormat);
            //在转换器中添加配置信息
            converter.setFastJsonConfig(config);
            return new HttpMessageConverters(converter);
        }
    }

五、自定义的Realm

    public class PasswordRealm extends AuthorizingRealm {
     
        @Autowired
        private RoleMapper roleMapper;
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private AuthorityMapper authorityMapper;
     
        /**
         * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
         */
        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
            User user = (User) principalCollection.getPrimaryPrincipal();
            System.out.println(user.getUsername() + "进行授权操作");
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            Integer roleId = user.getRoleId();
            Role role = roleMapper.findRoleById(roleId);
            info.addRole(role.getRoleName());
            List<Authority> authorities = authorityMapper.findAuthoritiesByRoleId(roleId);
            if (authorities.size() == 0) {
                return null;
            }
            return info;
        }
     
        /**
         * 认证回调函数，登录信息和用户验证信息验证
         */
        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
            //toke强转
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
            String username = usernamePasswordToken.getUsername();
            //根据用户名查询密码，由安全管理器负责对比查询出的数据库中的密码和页面输入的密码是否一致
            User user = userMapper.findUserByUserName(username);
            if (user == null) {
                return null;
            }
     
            //单用户登录
            //处理session
            DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
            //获取当前已登录的用户session列表
            Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
            User temp;
            for(Session session : sessions){
                //清除该用户以前登录时保存的session，强制退出
                Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (attribute == null) {
                    continue;
                }
     
                temp = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
                if(username.equals(temp.getUsername())) {
                    sessionManager.getSessionDAO().delete(session);
                }
            }
     
            String password = user.getPassword();
            //最后的比对需要交给安全管理器,三个参数进行初步的简单认证信息对象的包装,由安全管理器进行包装运行
            return new SimpleAuthenticationInfo(user, password, getName());
        }
    }

六、自定义的角色过滤器

    public class CustomRolesAuthorizationFilter extends RolesAuthorizationFilter {
        @Override
        public boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) {
            Subject subject = getSubject(req, resp);
            String[] rolesArray = (String[]) mappedValue;
            //如果没有角色限制，直接放行
            if (rolesArray == null || rolesArray.length == 0) {
                return true;
            }
            for (int i = 0; i < rolesArray.length; i++) {
                //若当前用户是rolesArray中的任何一个，则有权限访问
                if (subject.hasRole(rolesArray[i])) {
                    return true;
                }
            }
     
            return false;
        }
     
        @Override
        public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            //处理跨域问题，跨域的请求首先会发一个options类型的请求
            if (servletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
                return true;
            }
            boolean isAccess = isAccessAllowed(request, response, mappedValue);
            if (isAccess) {
                return true;
            }
            servletResponse.setCharacterEncoding("UTF-8");
            Subject subject = getSubject(request, response);
            PrintWriter printWriter = servletResponse.getWriter();
            servletResponse.setContentType("application/json;charset=UTF-8");
            servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
            servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            servletResponse.setHeader("Vary", "Origin");
            String respStr;
            if (subject.getPrincipal() == null) {
                respStr = JSONObject.toJSONString(new BaseResponse<>(300, "您还未登录，请先登录"));
            } else {
                respStr = JSONObject.toJSONString(new BaseResponse<>(403, "您没有此权限，请联系管理员"));
            }
            printWriter.write(respStr);
            printWriter.flush();
            servletResponse.setHeader("content-Length", respStr.getBytes().length + "");
            return false;
        }
    }

七、ShiroService

    @Service("shiroService")
    public class ShiroServiceImpl implements ShiroService {
     
        @Autowired
        private AuthorityMapper authorityMapper;
     
        /**
         * 初始化权限
         */
        @Override
        public Map<String, String> loadFilterChainDefinitions() {
            List<Authority> authorities = authorityMapper.findAuthorities();
            // 权限控制map.从数据库获取
            Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
            if (authorities.size() > 0) {
                String uris;
                String[] uriArr;
                for (Authority authority : authorities) {
                    if (StringUtils.isEmpty(authority.getPermission())) {
                        continue;
                    }
                    uris = authority.getUri();
                    uriArr = uris.split(",");
                    for (String uri : uriArr) {
                        filterChainDefinitionMap.put(uri, authority.getPermission());
                    }
                }
            }
            filterChainDefinitionMap.put("/user/login", "anon");
            //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
            filterChainDefinitionMap.put("/user/logout", "anon");
            filterChainDefinitionMap.put("/**", "authc");
            return filterChainDefinitionMap;
        }
     
        /**
         * 在对角色进行增删改操作时，需要调用此方法进行动态刷新
         * @param shiroFilterFactoryBean
         */
        @Override
        public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean) {
            synchronized (this) {
                AbstractShiroFilter shiroFilter;
                try {
                    shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
                } catch (Exception e) {
                    throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
                }
     
                PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
                DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
     
                // 清空老的权限控制
                manager.getFilterChains().clear();
     
                shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
                shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
                // 重新构建生成
                Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
                for (Map.Entry<String, String> entry : chains.entrySet()) {
                    String url = entry.getKey();
                    String chainDefinition = entry.getValue().trim()
                            .replace(" ", "");
                    manager.createChain(url, chainDefinition);
                }
            }
        }
    }


其他的Service、Mapper等文件就不贴出来了。

用guest用户登录，调用/user/list可以查询到数据，但是调用/role/list则会提示无权限，如下图：


用户登录和认证后都会在Redis中保存相应的数据：


同时在控制台只打印了一次xxx进行授权操作：


需要注意的是，退出登录时需要调用Subject.logout()方法，该方法会自动删除redis中的session和cache缓存。

使用shiro-redis做Session共享后，跟踪源码发现在修改角色名称后AuthorizationInfo中的角色名称依然是修改之前的。所以就需要用户退出后重登才会更新认证信息。

============================华丽的分割线===========================

为了解决上述问题，我想了两天的时间。一开始思维进入了误区，一心的想通过RedisCache和RedisCacheManager来删除授权相关的信息。RedisCache提供了remove(key)方法来删除缓存，但是由于本人能力有限，实在没看明白Redis中的key是怎么拿到Realm类的全限定名，然后拼凑出来的。后来想到当调用subject.logout()方法会删除cache和session，于是跟踪源码，发现是下图红框中的方法删除cache的：


因此，我写了下面的工具类，来删除cache和session：

    public class ShiroUtil {
     
        private static RedisSessionDAO redisSessionDAO = SpringUtil.getBean(RedisSessionDAO.class);
     
        private ShiroUtil() {
        }
     
        /**
         * 获取指定用户名的Session
         * @param username
         * @return
         */
        private static Session getSessionByUsername(String username){
            Collection<Session> sessions = redisSessionDAO.getActiveSessions();
            User user;
            Object attribute;
            for(Session session : sessions){
                attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (attribute == null) {
                    continue;
                }
                user = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
                if (user == null) {
                    continue;
                }
                if (Objects.equals(user.getUsername(), username)) {
                    return session;
                }
            }
            return null;
        }
     
        /**
         * 删除用户缓存信息
         * @param username 用户名
         * @param isRemoveSession 是否删除session，删除后用户需重新登录
         */
        public static void kickOutUser(String username, boolean isRemoveSession){
            Session session = getSessionByUsername(username);
            if (session == null) {
                return;
            }
     
            Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                return;
            }
     
            User user = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (!username.equals(user.getUsername())) {
                return;
            }
     
            //删除session
            if (isRemoveSession) {
                redisSessionDAO.delete(session);
            }
            DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            Authenticator authc = securityManager.getAuthenticator();
            //删除cache，在访问受限接口时会重新授权
            ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
        }
     
    }

在修改角色时，调用ShiroUtil.kickOutUser(username, isRemoveSession)方法就可以删除session和cache了。删除session主要是在禁用用户或角色时，强制用户退出的。如果仅仅修改角色信息是不需要删除session的，只需要删除cache，使用户在访问受限接口时重新授权即可。

附上[源码下载地址](https://download.csdn.net/download/haoyuyang/10369202)，错误之处请各位大神多多指正。