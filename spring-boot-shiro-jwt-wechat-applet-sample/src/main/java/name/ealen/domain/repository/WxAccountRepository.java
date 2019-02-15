package name.ealen.domain.repository;

import name.ealen.domain.entity.WxAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author EalenXie
 * @date 2018/11/26 10:32
 */
public interface WxAccountRepository extends JpaRepository<WxAccount, Integer> {

  /**
   * 根据OpenId查询用户信息
   * @param wxOpenId
   * @return
   */
  WxAccount findByWxOpenid(String wxOpenId);
}
