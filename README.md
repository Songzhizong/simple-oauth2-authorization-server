## 概括

一个非常简单的oauth2授权服务器实现，目的是帮助既有的授权系统实现OAuth2单点登录服务端支持。

## 目标

- 为既有的系统提供OAuth2单点登录服务端支持
- 作为独立的服务部署, 通过简单的配置和少量的开发工作, 就可以让既有的系统支持OAuth2单点登录
- 提供一个简单的OAuth2单点登录服务端实现, 用于学习和参考

## 非目标

- 完整实现OAuth2协议. 这不是目标
- 取代既有的授权系统. 这不是目标
- 提供一个基本框架, 在此之上实现完整的IAM系统. 这不不是目标

## 动机

OAuth2单点登录是一个非常有用的功能, 在日常开发工作中经常会遇到这样的需求:

一个系统需要将外部应用接入进来, 这个外部应用有自己的用户体系, 此时这个系统便需要提供单点登录功能.
若能提供一个符合业界常用规范的单点登录服务, 将会大大简化外部应用的接入工作.
如果外部应用本身便支持这套单点登录协议, 那么接入工作将会更加简单.

OAuth2协议作为被广泛使用的单点登录协议, 有着非常成熟的实现和规范. 因此实现一个独立OAuth2单点登录服务器,
不仅可以满足上述需求, 而且作为独立的服务部署可以实现与既有的系统解耦, 更加利于集成和维护.
