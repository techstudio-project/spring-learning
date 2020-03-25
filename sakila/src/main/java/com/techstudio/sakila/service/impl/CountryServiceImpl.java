package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Country;
import com.techstudio.sakila.dao.mapper.CountryMapper;
import com.techstudio.sakila.service.ICountryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements ICountryService {

}
