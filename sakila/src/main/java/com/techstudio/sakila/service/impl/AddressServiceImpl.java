package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Address;
import com.techstudio.sakila.dao.mapper.AddressMapper;
import com.techstudio.sakila.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
