package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Store;
import com.techstudio.sakila.dao.mapper.StoreMapper;
import com.techstudio.sakila.service.IStoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

}
