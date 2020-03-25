package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Inventory;
import com.techstudio.sakila.dao.mapper.InventoryMapper;
import com.techstudio.sakila.service.IInventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

}
