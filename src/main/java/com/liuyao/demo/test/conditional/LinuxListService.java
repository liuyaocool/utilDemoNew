package com.liuyao.demo.test.conditional;

//2-3
public class LinuxListService implements ListService {

    @Override
    public String showListCmd() {
        return "ls";
    }
}
