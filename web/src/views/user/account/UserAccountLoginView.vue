<template>
    <ContentField>
        <div class = "row justify-content-md-center">
            <div class = "col-3">
                <form @submit.prevent="login">  <!-- 和函数login绑定 下面的v-model也是绑定 -->
                    <div class="mb-3"> 
                        <label for="username" class="form-label">UserName</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="Username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="password">
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
    import { useStore } from 'vuex'
    import router from '../../../router/index'

    export default {
    names: "UserAccountLoginView", 
    components: {
        ContentField
    }, 

    setup () {
        const store = useStore()
        let username = ref('')
        let password = ref('')
        let error_message = ref ('')
        
        // 检查本地是否有保存的token 以及是否过期
        const jwt_token = localStorage.getItem('jwt_token')
        if (jwt_token) {
            store.commit("updateToken", jwt_token)
            store.dispatch("getInfo", {
                success(resp) {
                    console.log(resp)
                    router.push({name: 'home'})
                }, 
                error() {

                }
            }) 
        }

        const login =  () => {
            error_message.value = " "
            store.dispatch("login",  {  // 调用action中的方法是dispatch
                username: username.value, 
                password: password.value, 
                success (resp) {
                    
                    store.dispatch("getInfo", {
                        success() {
                            router.push({ name: 'home' })
                            console.log(store.state.user)
                        }
                    })

                    console.log(resp)
                }, 
                error () {
                    error_message.value = "Wrong username or password"
                }

            })
        }

        return  {
            username, 
            password, 
            error_message, 
            login
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