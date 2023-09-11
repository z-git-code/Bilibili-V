package com.imooc.bilibili.service.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * MD5加密//主要用到密码上
 * 单向加密算法（加密结果不可逆，无法还原加密前的明文数据是什么）
 * 特点：加密速度快，不需要秘钥，但是安全性不高，需要搭配随机盐值使用（无法还原加密前的明文是什么）
 */
public class MD5Util {

    public static String sign(String content, String salt, String charset) {//content--要加密的内容
        content = content + salt;
        return DigestUtils.md5Hex(getContentBytes(content, charset));
    }

    public static boolean verify(String content, String sign, String salt, String charset) {
        content = content + salt;
        String mysign = DigestUtils.md5Hex(getContentBytes(content, charset));
        return mysign.equals(sign);
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (!"".equals(charset)) {
            try {
                return content.getBytes(charset);
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException("MD5签名过程中出现错误,指定的编码集错误");
            }
        } else {
            return content.getBytes();
        }
    }
}