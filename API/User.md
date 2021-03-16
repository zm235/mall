#### 1. Login
**POST /user/login**

> request

Content-Type: application/json

```
{
    "username":"admin",
    "password":"admin",
}
```

> response

fail
````
{
    "status":1,
    "msg":"Password Error",
}
````

success
````
{
    "status":0,
    "data":{
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000,
    }
{
````

------

#### 2. Register
**POST /user/register**

>request

````
{
    "username":"admin",
    "password":"admin",
    "email":"admin@qq.com",
}
````

>response

success
````
{
    "status":0,
    "msg":"Register success",
}
````

fail
````
{
    "status":2,
    "msg":"Register failure",
}
````

------
#### 3.Get user's information
**GET /user**
>request
````
Don't need any parameter for the request
````

>response

success
````
{
    "status":0,
    "data":{
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000,
    }
}
````

fail
````
{
    "status":10,
    "msg":"User has not been logged in, cannot retrieve the user data",
}   
````

#### 4.log out
**POST /user/logout

> request

```
None
```

> response

success

```
{
    "status": 0,
    "msg": "successfully log out"
}
```

fail
```
{
    "status": -1,
    "msg": "server error"
}
```