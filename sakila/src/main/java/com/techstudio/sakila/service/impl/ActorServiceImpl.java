package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Actor;
import com.techstudio.sakila.dao.mapper.ActorMapper;
import com.techstudio.sakila.service.IActorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class ActorServiceImpl extends ServiceImpl<ActorMapper, Actor> implements IActorService {

}
