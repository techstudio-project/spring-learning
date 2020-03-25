package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.City;
import com.techstudio.sakila.dao.mapper.CityMapper;
import com.techstudio.sakila.service.ICityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

}
