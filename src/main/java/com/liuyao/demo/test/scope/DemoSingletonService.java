package com.liuyao.demo.test.scope;

import org.springframework.stereotype.Service;

//1： 编写singleton的Bean
@Service //默认为singleton 相当于@Scope("singleton")
public class DemoSingletonService {

}
