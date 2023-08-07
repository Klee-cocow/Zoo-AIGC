# Zoo-AIGC
动物园派对！汇集多方模型的AIGC平台。
立项时间:2023-07-18
# 更新日志
------
2023-07-19：
数据表的创建，全局异常处理，全局结果返回，创建多线程池。

2023-07-21：
完成登陆注册、获取登录状态判断是否登录、完成发送邮件、添加security鉴权、jwt令牌工具，加入redis

2023-08-04:
完成建立sse连接，关闭连接，发送消息功能。

2023-08-07：
完成用户发送的信息记录进行保存。

------

需要sql 文件的加我QQ：1952789737 ，目前个人开发，加上准备秋招进度较慢，有不全的地方。

文件结构:


|-- src

    |-- main
    |   |-- java
    |   |   |-- com
    |   |       |-- animal
    |   |           |-- product
    |   |               |-- ZooApplication.java
    |   |               |-- common     //全局命令啥的
    |   |               |   |-- BaseResponse.java
    |   |               |   |-- ErrorCode.java
    |   |               |   |-- ValidatorCommon.java
    |   |               |-- config     //全局配置
    |   |               |   |-- CorsConfig.java
    |   |               |   |-- MybatisPlusConfig.java
    |   |               |   |-- RedisConfig.java
    |   |               |   |-- SecurityConfig.java
    |   |               |-- constant   
    |   |               |   |-- IdentityEnum.java
    |   |               |   |-- ModelConstant.java
    |   |               |   |-- UserConstant.java
    |   |               |-- controller  //方法入口
    |   |               |   |-- testController.java
    |   |               |   |-- UserController.java
    |   |               |-- exception   //处理异常
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
