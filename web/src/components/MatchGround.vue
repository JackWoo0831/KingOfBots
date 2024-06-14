<template>
    <div class="matchground">
        <!-- 匹配布局 左边4份 中间选择bot4份 右边4份 -->
        <div class="row">
            <div class="col-4">
                <div class="user-photo" style="text-align: center;">
                    <img :src="store.state.user.photo" alt="" style="border-radius: 50%; padding-top: 20%;">
                </div>
                <div class="user-name" style="text-align: center; font-size: 30px; color: white; ">
                    {{ store.state.user.username }}
                </div>
            </div>
            <div class="col-4">
                <div class="user-select" style="padding-top: 20vh; width: 60%; margin: 0 auto;">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>In person</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>          
            <div class="col-4">
                <div class="user-photo" style="text-align: center;">
                    <img :src="store.state.pk.opponent_photo" alt="" style="border-radius: 50%; padding-top: 20%;">
                </div>
                <div class="user-name" style="text-align: center; font-size: 30px; color: white; ">
                    {{ store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center; padding-top: 10vh;">
                <button type="button" class="btn btn-warning btn-lg" style="width: 70%;" @click="update_button"> {{ match_button_info }} </button>
            </div>
        </div>
    </div>
</template>

<script>
    import { useStore } from 'vuex'
    import { ref } from 'vue'
    import $ from 'jquery'

    export default {
        components: {
        }, 

        setup () {
            const store = useStore()

            let match_button_info = ref("Start Match")
            let bots = ref([])  // 动态获取bots
            let select_bot = ref("-1")  // 选择了哪个bot 要向后端发信息

            const update_button = () => {
                if (match_button_info.value === 'Start Match') {
                    match_button_info.value = 'Cancel'
                    console.log(select_bot.value)
                    store.state.pk.socket.send(JSON.stringify({
                        event: 'start-matching', 
                        bot_id: select_bot.value
                    }))  // 向后端发送请求 是开始匹配还是取消匹配 对应后端Scoket server的onMessageing函数
                }
                else {
                    match_button_info.value = 'Start Match'
                    store.state.pk.socket.send(JSON.stringify({
                        event: 'stop-matching'
                    }))
                }
            }

            const refresh_bot = () => {
                $.ajax({
                    url: 'http://localhost:3000/user/bot/getlist/',
                    type: 'get',
                    headers: {
                        Authorization: "Bearer " + store.state.user.token
                    },

                    success (resp) {
                        console.log(resp)
                        bots.value = resp
                    },
                    error (resp) {
                        console.log(resp)
                    }

                })
            }

            refresh_bot()
            
            return {
                store, 
                match_button_info, 
                update_button, 
                bots,
                select_bot
            }
        }
    }

    

</script>

<style scoped>
    div.matchground {
        width: 60vw;
        height: 70vh;
        background: rgba(50, 50, 50, 0.5);
        margin: 40px auto; 
    }

</style>