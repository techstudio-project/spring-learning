package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.FilmActor;
import com.techstudio.sakila.dao.mapper.FilmActorMapper;
import com.techstudio.sakila.service.IFilmActorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class FilmActorServiceImpl extends ServiceImpl<FilmActorMapper, FilmActor> implements IFilmActorService {

}
