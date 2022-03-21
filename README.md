# 关于 undo

- undo 是我的毕设项目，之所以叫做 undo，是因为他与 案读 同音，并且我能够买到这个域名。
- undo 是一个开源社区的项目，目前之提供简单的功能。
- 未来 undo 很可能上线运行，成为我的一个技术社区，这也是我的目标之一。
# 模块
undo 将采用为服务的模式进行开发和管理，之所以采用为服务的方式，是因为考虑到后期肯能真正的上线运行，和后期的业务扩展。

- 网站前端

用来展示网页用户的前端页面，目前还没有开发，计划使用 vue.js 做成但页面应用。

- 认证模块

由于使用了为服务的模块，所以认证模块将不可缺少。认证模块用来保存用户的登录信息和未来做第三方登录使用，任何一个模块或第三方应用只能通过认证模块签发的 token 来获取用户的信息。未来 undo 社区可能各个细分领域扩展业务，但是所有的平台将统一使用 undo 帐号来登录。

- 搜索模块

用来搜索文章。目前没有计划。

- 推荐模块

用来展现首页推荐，这次毕设将实现最简单的推荐（按照文章发布时间或者随机）。

- 文章管理模块

管理用可以进行文件的上传，方便在文章中引用和分享。将采用文件分块存储的方式进行存放用户文件。

- 内容审核模块

审核用户的内容，本次毕设将采用人工审核的方式。

- 运维管理模块

提供简单的服务器状态的查看，方便运维人员的管理。