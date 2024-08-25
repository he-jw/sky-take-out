# sky-take-out

#### 介绍
黑马苍穹外卖项目学习记录
项目介绍：基于Springboot、MyBatis、MySql、Redis、WebSocket的专门为餐饮企业定制的软件产品，包括系统管理后台和小程序端应用两部分。系统管理后台提供给内部员工使用，可以对菜品、套餐、订单、外卖状态、员工等进行管理维护，对餐厅的各类数据进行统计，支持来单的语音播报功能。小程序端提供给消费者使用，可以在线浏览菜品、添加购物车、下单、支付、催单等。
通过自定义切面来实现对持久层实体类的公共字段自动填充，避免了重复代码。
通过Redis缓存菜品数据，提高系统响应速度，并采用清理缓存的方式保证数据一致性。
通过HttpClient调用百度地图开放平台接口，检验客户的收货地址是否超出配送范围，并规划配送路线。
使用WebSocket实现当客户下单或催单时，向商家管理端推送客户下单和催单的消息提示和语音播报。

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
