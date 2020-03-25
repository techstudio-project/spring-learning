package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Category;
import com.techstudio.sakila.dao.mapper.CategoryMapper;
import com.techstudio.sakila.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
