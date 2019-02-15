package name.ealen.infrastructure.util;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author EalenXie
 * @date 2018/11/22 16:37
 */
public enum HttpUtil {

  ;

  /**
   * 根据url和请求参数获取URI
   */
  public static URI getURIwithParams(String url, MultiValueMap<String, String> params) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
    return builder.build().encode().toUri();
  }

  /**
   * 获取用户IP地址
   */
  public static String getIpAddress(HttpServletRequest request) {
    String[] ipHeaders = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
    String ip = request.getRemoteAddr();
    for (String header : ipHeaders) {
      if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
        break;
      }
      ip = request.getHeader(header);
    }
    return ip;
  }
}
