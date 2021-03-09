package com.liuyao.design_patterns.cor;

import java.util.ArrayList;
import java.util.List;

public class MainCorFilter {
    /**
     * 责任链模式
     */
    public static void main(String[] args) {
        Req req = new Req();
        req.str = "req:";
        Resp resp = new Resp();
        resp.str = "resp:";

        ReqResChain chain = new ReqResChain();
        chain.add(new AaReqResFilter()).add(new BbReqResFilter());

        chain.doFilter(req, resp, chain);

        System.out.println(req.str);
        System.out.println(resp.str);


    }
}

class Req {
    String str;
}

class Resp {
    String str;
}

interface RFilter {
    public boolean doFilter(Req req, Resp resp, ReqResChain chain);
}

class ReqResChain implements RFilter{

    List<RFilter> filters = new ArrayList<>();
    int idx = 0;

    public ReqResChain add(RFilter f) {
        filters.add(f);
        return this;
    }
    @Override
    public boolean doFilter(Req req, Resp resp, ReqResChain chain) {
        if (idx >= filters.size()){
            return false;
        }
        filters.get(idx++).doFilter(req, resp, chain);
        return true;
    }
}

class AaReqResFilter implements RFilter{

    @Override
    public boolean doFilter(Req req, Resp resp, ReqResChain chain) {
        req.str += "a";
        chain.doFilter(req, resp, chain);
        resp.str += "a";
        return false;
    }
}

class BbReqResFilter implements RFilter{

    @Override
    public boolean doFilter(Req req, Resp resp, ReqResChain chain) {
        req.str += "b";
        chain.doFilter(req, resp, chain);
        resp.str += "b";
        return false;
    }
}

