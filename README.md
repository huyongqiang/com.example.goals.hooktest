# com.example.goals.hooktest
#Android插件化原理（一）Activity插件化
##前言
Android插件化原理：
1.Activity插件化
2.Service服务插件化
3.Content Provider内容提供者插件化
4.BroadcastReceiver广播接收器插件化
5.模块交互

一，Activity插件化实现：
1.1反射实现（影响性能）
1.2接口实现（可以阅读dynamic-load-apk的源码）
1.3Hook技术实现（主流技术，本demo实现）
1.3.1Hook IActivityManager来实现
1.3.2Hook Instrumentation来实现

简要介绍：四大组件的插件化是插件化技术的核心知识点，而Activity插件化更是重中之重，Activity插件化主要有三种实现方式，分别是反射实现、接口实现和Hook技术实现。反射实现会对性能有所影响，主流的插件化框架没有采用此方式，关于接口实现可以阅读dynamic-load-apk的源码，这里不做介绍，目前Hook技术实现是主流，因此本篇文章主要介绍Hook技术实现。
     Hook技术实现主要有两种解决方案 ，一种是通过Hook IActivityManager来实现，另一种是Hook Instrumentation实现。在讲到这两个解决方案前，我们需要从整体上了解Activity的启动流程。
     