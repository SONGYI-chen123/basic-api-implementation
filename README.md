### 作业描述

<<<<<<< HEAD
* 将所有接口的返回值都替换成使用ResponseEntity
* 所有post请求都返回201,并且返回的头部带上index字段（值为创建的资源在列表中的位置：eg: 添加的热搜事件在列表中的index）
* 完成demo里的练习：get /rs/list和 get/rs/{index}接口返回的数据中不包含user字段
* 添加获取所有用户的接口: get /users，期望返回的数据格式例子: 
    ```
    [{
        "user_name": "xxxx",
        "user_age": 19,
        "user_gender": "female",
        "user_email": "xxx@xx",
        "user_phone": "1xxxxxxxxxx"
    }]
* 先写测试！！！
* hint: @JsonpProperty的使用


<span style="color: red"> 注意：最终需要将改动合并到master分支 </span> 
=======
根据课堂上的demo，完成下面需求
1. 提供获取某一条热搜事件的接口
2. 提供能够根据起始参数，获取对应范围内的热搜事件列表的接口
3. 提供添加热搜事件的接口（事件包含两个字段：事件名称和关键字）
4. 提供修改某条热搜事件的接口（demo没有展示，请大家自己完成）
5. 提供删除某条热搜事件的接口（demo没有展示，请大家自己完成）

#### 需求4、5详细描述

* 需求4： 修改某条事件时（通过参数传递的序号，修改列表中对应的事件数据），如果RequestBody只传了eventName没有传keyword那么仅仅只修改eventName
         如果只传了keyword没有传eventName，那么只修改keyword字段
         如果两个字段都传了，那么都进行修改
         
* 需求5： 通过参数传递的序号，删除列表中对应的某条事件数据


<span style="color: red"> 注意：所有的需求都请先写测试再写实现 </span> 
>>>>>>> master

