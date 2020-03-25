package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Customer;
import com.techstudio.sakila.dao.mapper.CustomerMapper;
import com.techstudio.sakila.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
