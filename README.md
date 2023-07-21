# Zoo-AIGC
动物园派对！汇集多方模型的AIGC平台。
立项时间:2023-07-18
# 更新日志
------
2023-07-19：
数据表的创建，全局异常处理，全局结果返回，创建多线程池。

2023-07-21：
完成登陆注册、获取登录状态判断是否登录、完成发送邮件、添加security鉴权、jwt令牌工具，加入redis
------

文件结构:
|-- src
    |-- main
    |   |-- java
    |   |   |-- com
    |   |       |-- animal
    |   |           |-- product
    |   |               |-- ZooApplication.java
    |   |               |-- common
    |   |               |   |-- BaseResponse.java
    |   |               |   |-- ErrorCode.java
    |   |               |   |-- ValidatorCommon.java
    |   |               |-- config
    |   |               |   |-- CorsConfig.java
    |   |               |   |-- MybatisPlusConfig.java
    |   |               |   |-- RedisConfig.java
    |   |               |   |-- SecurityConfig.java
    |   |               |-- constant
    |   |               |   |-- IdentityEnum.java
    |   |               |   |-- ModelConstant.java
    |   |               |   |-- UserConstant.java
    |   |               |-- controller
    |   |               |   |-- testController.java
    |   |               |   |-- UserController.java
    |   |               |-- exception
    |   |               |   |-- BusinessException.java
    |   |               |   |-- GlobalExceptionHandler.java
    |   |               |-- interceptor
    |   |               |   |-- AuthorizeInterceptor.java
    |   |               |-- mapper
    |   |               |   |-- ZooUsersMapper.java
    |   |               |-- model
    |   |               |   |-- domain
    |   |               |   |   |-- ZooUsers.java
    |   |               |   |-- dto
    |   |               |   |   |-- MailSenderDTO.java
    |   |               |   |   |-- UserDTO.java
    |   |               |   |-- request
    |   |               |   |   |-- UserLoginRequest.java
    |   |               |   |   |-- UserRegisterRequest.java
    |   |               |   |-- vo
    |   |               |       |-- UserVO.java
    |   |               |-- service
    |   |               |   |-- ZooUsersService.java
    |   |               |   |-- impl
    |   |               |   |   |-- ZooUsersServiceImpl.java
    |   |               |   |-- strategyimpl
    |   |               |       |-- EmailStrategyImpl.java
    |   |               |       |-- PhoneStrategyImpl.java
    |   |               |-- strategy
    |   |               |   |-- UserStrategyContent.java
    |   |               |   |-- UserStrategyInterface.java
    |   |               |-- utils
    |   |                   |-- CommonToolUtils.java
    |   |                   |-- JwtUtil.java
    |   |                   |-- ResultUtil.java
    |   |                   |-- ThreadUtil.java
    |   |-- resources
    |       |-- application.yml
    |       |-- logback.xml
    |       |-- lua
    |       |   |-- redis.lua
    |       |-- mapper
    |           |-- UsersMapper.xml
    |-- test
        |-- java
            |-- com
                |-- animal
                    |-- product
                        |-- ZooApplicationTests.java

