package com.lee.service.impl;

import com.lee.bean.Parent;
import com.lee.bean.ParentExample;
import com.lee.dao.ParentMapper;
import com.lee.service.ParentService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentServiceImpl implements ParentService {

  @Autowired
  ParentMapper parentMapper;

  @Override
  public Parent getParentByAccount(String account) {
    ParentExample parentExample = new ParentExample();
    parentExample.createCriteria().andAccountEqualTo(account);
    List<Parent> parentList = parentMapper.selectByExample(parentExample);
    if (CollectionUtils.isEmpty(parentList)) {
      return null;
    }
    return parentList.get(0);
  }

}
