package com.mm.common.xss;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * XSS过滤处理
 *
 * @author lwl
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
     */
    HttpServletRequest orgRequest;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //非json类型，直接返回
        if (!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))) {
            return super.getInputStream();
        }

        //为空，直接返回
        String json = IoUtil.read(super.getInputStream(), CharsetUtil.CHARSET_UTF_8);
        if (StrUtil.isBlank(json)) {
            return super.getInputStream();
        }

        //xss过滤
        JSONObject map = JSONUtil.parseObj(json);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                if (StrUtil.isNotEmpty(entry.getValue().toString())) {
                    map.set(entry.getKey(), HtmlUtil.filter(entry.getValue().toString()));
                }
            }
        }
        json = JSONUtil.toJsonStr(map);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.isNotEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = StrUtil.isNotEmpty(values[i]) ? HtmlUtil.filter(values[i]) : values[i];
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = StrUtil.isNotEmpty(values[i]) ? HtmlUtil.filter(values[i]) : values[i];
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StrUtil.isNotEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }
        return request;
    }

}
