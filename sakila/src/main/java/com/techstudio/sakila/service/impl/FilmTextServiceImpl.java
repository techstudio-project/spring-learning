package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.FilmText;
import com.techstudio.sakila.dao.mapper.FilmTextMapper;
import com.techstudio.sakila.service.IFilmTextService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class FilmTextServiceImpl extends ServiceImpl<FilmTextMapper, FilmText> implements IFilmTextService {

}
