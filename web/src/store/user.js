import $ from 'jquery'

/*
    将用户的信息以全局变量的形式储存 借助vuex
*/ 
export default {
    state: {
        id: "", 
        username: "", 
        password: "", 
        photo: "", 
        token: "",  // jwt token
        is_login: false, 
    },
    getters: {
    },
    mutations: {  // 放一些修改内容的辅助函数
        updateUser(state, user) {
            state.id = user.userid
            state.username = user.username
            state.password = user.password 
            state.photo = user.photo
            state.is_login = user.is_login
        }, 

        updateToken(state, token) {
           state.token = token  // 经常用到 故另写一个 
        }, 

        logout(state) {  // 登出 只需要前端自己操作就好了 把一切都置空
          state.id = ""
          state.username = ""
          state.password = ""
          state.photo = ""
          state.is_login = false
        }

    },
    actions: {
        // 一些行为 例如 登录 和后端交互

        login(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/account/token/",
                type: 'post', 
                data: {
                  username: data.username, 
                  password: data.password, 
                }, 
                success (resp) {
                  if (resp.error_message === 'success') {
                    // 将token存到内存里(cookie?) 这样刷新的话不会自动退出
                    // 借助localstorage实现
                    localStorage.setItem('jwt_token', resp.token)
                    context.commit("updateToken", resp.token)
                    data.success(resp)
                  }
                  else {
                    data.error(resp)
                  }
                },
                error (resp) {
                  console.log(resp)
                }
              })
        }, 

        getInfo(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/account/info/", 
                type: 'get', 
                // header协议参见 com/kob/backend/configs/filter/JwtAuthenticationTokenFilter.java
                headers: {
                  Authorization: "Bearer " + context.state.token
                }, 

                success (resp) {
                    if (resp.error_message === 'success') {
                        context.commit("updateUser", {
                            ...resp, // 解析resp中的内容 类似python **
                            is_login: true, 
                        })
                        data.success(resp)
                    }
                    else {
                        data.error(resp)
                    }                  
                }, 
                error (resp) {
                    console.log(resp)
                }
              })
        }, 

        
      logout(context) {
        // 退出 就要删除local storage
        localStorage.removeItem('jwt_token')
        context.commit("logout")
      }
    },
    modules: {
    }
}