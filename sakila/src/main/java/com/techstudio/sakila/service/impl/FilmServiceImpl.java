package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Film;
import com.techstudio.sakila.dao.mapper.FilmMapper;
import com.techstudio.sakila.service.IFilmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class FilmServiceImpl extends ServiceImpl<FilmMapper, Film> implements IFilmService {

}
