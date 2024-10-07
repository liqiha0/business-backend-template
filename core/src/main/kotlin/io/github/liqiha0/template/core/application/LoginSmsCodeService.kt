package io.github.liqiha0.template.core.application

interface LoginSmsCodeService {
    /**
     * 发送登录短信验证码
     * @param phoneNumber 手机号码
     * @param scene 场景类型（如：LOGIN、REGISTER等）
     * @return 验证码ID，用于后续验证
     * @throws IllegalArgumentException 如果手机号码格式不正确
     */
    fun send(phoneNumber: String)

    /**
     * 验证短信验证码
     * @param codeId 验证码ID
     * @param code 输入的验证码
     * @return 验证是否成功
     * @throws AuthenticationException 如果验证码无效或已过期
     */
    fun verify(codeId: String, code: String): Boolean
}