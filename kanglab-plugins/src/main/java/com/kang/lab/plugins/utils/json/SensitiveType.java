package com.kang.lab.plugins.utils.json;

public enum SensitiveType {
    /**
     * 密码
     */
    PASSWORD,
    /**
     * <p>身份证号</p>
     * <p>默认保留前三位和后四位</p>
     */
    ID_CARD,
    /**
     * <p>银行卡号</p>
     * <p>默认保留前四位和后四位</p>
     */
    BANK_CARD,
    /**
     * <p>手机号</p>
     * <p>默认保留前三位和后四位</p>
     */
    MOBILE,
    /**
     * <p>电子邮箱</p>
     * <p>仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示</p>
     */
    EMAIL,
    /**
     * <p>地址</p>
     * <p>默认从第四位开始脱敏</p>
     */
    ADDRESS,
    /**
     * <p>姓名</p>
     * <p>默认仅保留第一位</p>
     */
    NAME,
    /**
     * 车牌号
     */
    CAR_LICENSE,
    /**
     * IPV4
     */
    IPV4,
    /**
     * IPV6
     */
    IPV6,
    /**
     * 忽略字数
     */
    IGNORE_COUNT,
    /**
     * <p>自定义</p>
     * <p>根据前后保留位数进行脱敏，默认不脱敏</p>
     */
    CUSTOM
}
