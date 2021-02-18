package com.liuyao.demo.filter;

import com.liuyao.demo.config.SysConfig;
import com.liuyao.demo.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

//@Component
//@WebFilter(urlPatterns = {"/systemFile/*"},filterName = "normalFilter") 不管用
public class LogFiler implements Filter {

    @Autowired
    private SysConfig sysConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        String uri = httpReq.getRequestURI();
        MyRequestWrapper requestWrapper = new MyRequestWrapper(httpReq);
        String body = requestWrapper.getBody();
        LogUtil.info("=====================数据接入请求数据:\n" + body);
        MyResponseWrapper responseWrapper = new MyResponseWrapper((HttpServletResponse) response);
        chain.doFilter(requestWrapper, responseWrapper);
        LogUtil.info("=====================数据接入请求数据:\n" + body);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }


    class LogRespWrapper extends HttpServletResponseWrapper {
        //1. 也是实现了自定义的ServletOutputStream内部类LoggingServletOutpuStream，并初始化
        private final LoggingServletOutpuStream lsos = new LoggingServletOutpuStream();

        private final HttpServletResponse hsResp;

        public LogRespWrapper(HttpServletResponse response) {
            super(response);
            hsResp = response;
        }

        @Override
        //2. 当外部获取ServletOutputStream时候返回内部实现类
        public ServletOutputStream getOutputStream() throws IOException {
            return lsos;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(lsos.baos);
        }

        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>(0);
            for (String headerName : getHeaderNames()) {
                headers.put(headerName, getHeader(headerName));
            }
            return headers;
        }

        public String getContent() {
            try {
                String responseEncoding = hsResp.getCharacterEncoding();
                return lsos.baos.toString(responseEncoding != null ? responseEncoding : "utf-8");
            } catch (UnsupportedEncodingException e) {
                return "[UNSUPPORTED ENCODING]";
            }
        }

        //4. 获取请求内容时候从内部实现类的私有变量返回
        public byte[] getContentAsBytes() {
            return lsos.baos.toByteArray();
        }

        private class LoggingServletOutpuStream extends ServletOutputStream {

            private ByteArrayOutputStream baos = new ByteArrayOutputStream();

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                baos.write(b);
            }

            @Override
            //3. 当向内部实现类LoggingServletOutpuStream 写入信息时，写到其内部字段baos上
            public void write(byte[] b, int off, int len) throws IOException {
                baos.write(b, off, len);
            }
        }
    }

    class MyRequestWrapper extends HttpServletRequestWrapper {

        private final String body;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        public MyRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            BufferedReader br = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
            this.body = sb.toString();
        }

        public String getBody() { return body; }
        @Override
        public ServletInputStream getInputStream()  {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
            return new ServletInputStream() {
                @Override
                public boolean isFinished() { return false; }
                @Override
                public boolean isReady() { return false; }
                @Override
                public void setReadListener(ReadListener readListener) { }
                @Override
                public int read(){ return bais.read(); }
            };
        }
        @Override
        public BufferedReader getReader(){
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
    }
    class MyResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream buffer;
        private ServletOutputStream out;
        public MyResponseWrapper(HttpServletResponse httpServletResponse) {
            super(httpServletResponse);
            buffer = new ByteArrayOutputStream();
            out = new ServletOutputStream(){
                @Override
                public void write(int b) throws IOException {
                    buffer.write(b);
                }
                @Override
                public boolean isReady() { return false; }
                @Override
                public void setWriteListener(WriteListener arg0) { }
            };
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException { return out; }
        @Override
        public void flushBuffer() throws IOException { if (out != null) { out.flush(); } }
        public String getContent() throws IOException {
            flushBuffer();
            return new String(buffer.toByteArray(), "utf-8");
        }

    }


}
