package name.ealen.domain.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author EalenXie
 * @date 2018/11/26 10:26
 */
@Entity
@Table
public class WxAccount {

  @Id
  @GeneratedValue
  private Integer id;

  private String wxOpenid;

  private String sessionKey;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date lastTime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getWxOpenid() {
    return wxOpenid;
  }

  public void setWxOpenid(String wxOpenid) {
    this.wxOpenid = wxOpenid;
  }

  public String getSessionKey() {
    return sessionKey;
  }

  public void setSessionKey(String sessionKey) {
    this.sessionKey = sessionKey;
  }

  public Date getLastTime() {
    return lastTime;
  }

  public void setLastTime(Date lastTime) {
    this.lastTime = lastTime;
  }

}
