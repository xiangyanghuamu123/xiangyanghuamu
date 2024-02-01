package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2021000122684572";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWlYqCHoBOULAmQ5QNeIOVz/ZNhdWoapTTbYoGom26oN8STqehsd1RQGSuYtnS/DSxlNL2fRwSq0ner8ksqGgvJ+pddiskMEmwKKJScvpSETckiaPBqt3vf6fNGfRGglgfeIbh+OyazV3jNcM40FiH/spg3f5LxR5wQNYlvSQxECLVyM/ENP6DVht9zb0/b1Ab9nXpvpBJSdZp7IYdCSoXjy1evosq6CkHQ1tnj3DCOxYf6uTbieprsVqN9eQ25F6R+iLKViK/ehhGDKOY/GTuNk1JP4go+etw6AoNr5a5Fh1sdcCryhqbEJKHs89W+rpVVpMafVUc5mrJHXeFsI9hAgMBAAECggEAcdd4MUrx4UMrCKKKaWy0vivMn2sQzhRVk0n60W0226MHQgXowVuAYA74jsX4u4MkKyTCIMsQ0CRi4lGvG23t5cidbC8MCn3fF6iAnyiBik2tjJfqD14oDwpJR8vZWvzrtk8eWfQgnB+qXT2l62vHonjDc1whBhNuIZtCyqElnVjWZhYtdG4ZTRuR7hmLEZoW4dIgvowxNQ4OeZJwWhHDcAb+fD82tHMVswz0dDdeLS9I+qlRB+SIYCKwb76HiIn4KWqpTrIy2ylPRDsosONNrwissoNPClKap18TIWIlHcq6vZT0R5b01xKUWIR2Yx6scLCtBZq69ES3PbCARXh6KQKBgQDOBFe4X28X3lmZlhAIdPSRUiQP9amUUde7L0OyREYLtt9hwdHbTN2wVfMMkO23Q2WCCER9PWDoMXMwrM8z0dZBEiewr4iDo1aGVVMnk2WqZ1hfsuY1bcis2mPyN6KQ0RKEwsBIiG+xLNkgVgbHemx03V4HaM/dv83dcG2at6UG5wKBgQC7Hkf5tjS46983S/NvpC/H+cFa11Ftl0hsD/Ht7+Dcqnp1tjaHEjUJJgfsCno8q5UiyD5LAezdSntrahcOxKKjx8Bo0WJGp9dVz2VPhiw0XGdBeKD4TUCoUuczyWqVP0i3ch/wgPYBkKbjnM10HvSZZz62wPEjv1oHooiqn7mWdwKBgQCo4e52+eS8EE4rFVT3WcjTCB7/5kapi53pmIlKLaWuHK9qF9TMjWp+Dzc66eO1A87GlbhvYk16im7/S+pfdHjKxvM6Z+rk3pUjcfbEzpPc6PMNID6PNfv3lt+vw+PEC1++7VdrHk1RVI55b4wHIlieRIm8UTN1eLJuAKgY2MqzqQKBgF4V/sjfxijJaUaLqQ8UtnXN7lw38hixvdegONyUBjPOgJ7ZjRdjn0lHVypnpkkFWYrQtwnOIQSwI9jB0nA5jTHfCa9qwQiX8l2SV/WhmG6Hm9++wOm5Jm68qMZQW4hJ9s1O1CKBJbFpw5ClUmU1/3V3BYCtCVMZLzKxlhh9EbtHAoGBALQ88YKwqxsppngsynPXPytHiTIcDLYi09Z8ca+yiDecCFHCYB9+gu/mVF+25/WnXD2pgwUmeUX6uORwi/mQv9qlKN1k9h2KlboajsG5/24XiRaM6nWq81qoOpY9NLLeEcQc9lQrn/uht1+XK1MdLDLhjLqDkIFfXZIZvouNTIC1";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://ikpcp4.natappfree.cc/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://ikpcp4.natappfree.cc/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
//	public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 日志路径
	public static String log_path = "/Library/idea-maven-workspace/log";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

