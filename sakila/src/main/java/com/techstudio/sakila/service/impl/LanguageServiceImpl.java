package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Language;
import com.techstudio.sakila.dao.mapper.LanguageMapper;
import com.techstudio.sakila.service.ILanguageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class LanguageServiceImpl extends ServiceImpl<LanguageMapper, Language> implements ILanguageService {

}
