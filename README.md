# smart-health-twin-uniapp（后端）

#### 介绍（此为项目详细介绍）
该项目是融合物联网、人工智能与数字孪生技术的智能健康管理系统，通过自主研发的ESP32-S3智能手表实时采集用户心率、血压、血氧、体温等12项生理指标（采样频率1Hz），解析体检报告PDF或者图片进行健康分析与建议告警，构建个人健康数字孪生模型。调用千问大模型进行慢性病风险预测（准确率85%）与健康AI助手问答功能，并基于构建3D虚拟数字人动态展示健康状态。平台包含微信小程序端与PC管理后台，微信小程序供用户使用绑定设备编号即可实现手表与页面数据互通，管理端供以查看数据与手表设备管理等功能，形成"硬件采集-云端分析-多端可视化"的完整闭环。

#### 关联项目：
管理端：https://github.com/3323223659/smart-health-twin-vue3

小程序端: https://github.com/3323223659/smart-health-twin-uniapp

#### 软件架构
SpringBoot+SSM+SpringAI+ MyBatisPlus+MySQL+Redis+WebSocke

#### 安装教程
pull 该项目。

找到代码压缩包下的 smart-health-twin-backend\src\main\resources\sql\create.sql，在mysql 中运行sql脚本。 

阿里云上申请oss、ocr、通用文字识别服务得到key-id与key-secret，同时在oss服务中创建一个bucket桶供存储图片。 

smart-health-twin-backend\src\main\resources下的application.yml文件中修改自己的oss、ocr 配置信息。 

申请通义千问大模型api-key，也配置到application.yml文件中。 

启动redis并且确保在6379端口运行redis服务。 

yml 文件中设置自己的mysql配置信息，刷新maven依赖后，并确保8088端口启动服务，因为管理端与小程序端访问的是8080端口。 
