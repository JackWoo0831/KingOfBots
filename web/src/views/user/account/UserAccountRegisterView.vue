<template>
    <ContentField>
        <div class = "row justify-content-md-center">
            <div class = "col-3">
                <form @submit.prevent="register">  <!-- 和函数register绑定 下面的v-model也是绑定 -->
                    <div class="mb-3"> 
                        <label for="username" class="form-label">UserName</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="Username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="password">
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm password</label>
                        <input v-model="confirmPassword" type="password" class="form-control" id="confirmPassword" placeholder="repeat password">
                    </div>
                    <button type="submit" class="btn btn-primary">Enter</button>
                    <div class = "error_message"> {{ error_message }} </div>
                </form>
            </div>
        </div>

    </ContentField>
</template>

<script>
    import ContentField from '../../../components/ContentField.vue'
    import { ref } from 'vue'
    import router from '../../../router/index'
    import $ from 'jquery'


    export default {
    names: "UserAccountRegisterView", 
    components: {
        ContentField
    }, 

    setup () {
        let username = ref('')
        let password = ref('')
        let confirmPassword = ref('')
        let error_message = ref ('')

        const register =  () => {
            // 注意 注册的话 不需要调用store里的user 因为还没有user 
            // 直接用ajax跟后端发请求即可
            $.ajax({
                url: 'http://localhost:3000/user/account/register/', 
                type: 'post',  // 一般来讲 修改数据库用post 读取用get
                data: {
                    username: username.value, 
                    password: password.value, 
                    confirmPassword: confirmPassword.value, 
                }, 
                success(resp) {  // success(resp) 是 success: function(resp)的简写
                    if (resp.error_message === 'success') {
                        router.push({name: 'user_login'})
                        console.log(resp)
                    }
                    else {
                        error_message.value = resp.error_message
                        console.log(resp)
                    }
                }, 
                error(resp) {
                    console.log(resp)
                }

            })
        }

        return  {
            username, 
            password, 
            confirmPassword, 
            error_message, 
            register
        }
    }
   }
</script>

<style>
    button {
        width: 100%
    }
    div.error_message {
        color: red
    }
</style>