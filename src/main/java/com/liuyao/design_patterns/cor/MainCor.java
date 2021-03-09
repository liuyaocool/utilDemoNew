package com.liuyao.design_patterns.cor;

import java.util.ArrayList;
import java.util.List;

public class MainCor {
    /**
     * 责任链模式
     */
    public static void main(String[] args) {
        Msg m = new Msg();
        m.str = "ABCD";

        FilterChain chain = new FilterChain();
        chain.add(new CcFilter()).add(new DdFilter());
        chain.add(new FilterChain().add(new BbFilter()).add(new AaFilter()));
        chain.doFilter(m);

        System.out.println(m.str);

    }
}

class Msg {
    String str;
}

interface Filter {
    public boolean doFilter(Msg m);
}

class FilterChain implements Filter {
    List<Filter> filters = new ArrayList<>();

    public FilterChain add(Filter f){
        if (null != f) {
            filters.add(f);
        }
        return this;
    }

    @Override
    public boolean doFilter(Msg m) {
        for (Filter f: filters) {
            if (!f.doFilter(m)) {
                return false;
            }
        }
        return true;
    }
}

class AaFilter implements Filter {
    @Override
    public boolean doFilter(Msg m) {
        m.str = m.str.replaceAll("A", "[a]");
        return true;
    }
}

class BbFilter implements Filter {
    @Override
    public boolean doFilter(Msg m) {
        m.str = m.str.replaceAll("B", "[b]");
        return false;
    }
}

class CcFilter implements Filter {
    @Override
    public boolean doFilter(Msg m) {
        m.str = m.str.replaceAll("C", "[c]");
        return true;
    }
}

class DdFilter implements Filter {
    @Override
    public boolean doFilter(Msg m) {
        m.str = m.str.replaceAll("D", "[d]");
        return true;
    }
}
