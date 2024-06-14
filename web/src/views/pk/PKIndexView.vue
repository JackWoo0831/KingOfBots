<template>
    <ContentField>PK page</ContentField>
    <PlayGround v-if="store.state.pk.status === 'playing'"> </PlayGround>
    <MatchGround v-if="store.state.pk.status === 'matching'"> </MatchGround>
    <ResultBoard v-if="store.state.pk.loser !== 'none'"> </ResultBoard>
</template>

<script>
    import { onMounted, onUnmounted } from 'vue'
    import ContentField from '../../components/ContentField.vue'
    import PlayGround from '../../components/PlayGround.vue'
    import MatchGround from '../../components/MatchGround.vue'
    import ResultBoard from '../../components/PKResult.vue'
    import { useStore } from 'vuex'

    export default {
    names: "PKIndexView", 
    components: {
        ContentField,
        PlayGround, 
        MatchGround, 
        ResultBoard
    }, 


    setup () {
        const store = useStore()
        const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}/`

        // 更新loser为none 并且把is_record设置为false
        // 因为展示录像界面也服用了Game(PlayGround)

        store.commit("updateLoser", "none")
        store.commit("updateIsRecord", false)


        console.log(store.state.user.id)

        let socket = null
        onMounted(() => {  // onMounted是该组件(PKIndexView)运行的时候 就会调用 可以简单地理解为页面打开
            socket = new WebSocket(socketUrl)            

            // 新建一个对手 方便调试
            store.commit("updateOpponent", {
                username: "My competitor", 
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })

            socket.onopen = () => {
                console.log('connected')
                store.commit("updateSocket", socket)  // 更新pk的 socket
            }            

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data)  // 具体msg怎么用要取决于后端框架
                console.log(data)

                if (data.event === 'match-finished') {
                    // 匹配成功

                    // 更新对手信息
                    store.commit("updateOpponent", {
                        username: data.opponent_username, 
                        photo: data.opponent_photo
                    })

                    // 延迟 显示匹配信息
                    setTimeout(() => {
                        // 更新当前状态
                        store.commit("updateStatus", "playing")
                    }, 200)               
                    
                    store.commit("updateGame", data.game)
                    
                    
                }

                else if (data.event === 'move') {
                    const game = store.state.pk.gameObject
                    const [snake0, snake1] = game.snakes
                    snake0.set_direction(data.a_direction)
                    snake1.set_direction(data.b_direction)

                }
                
                else if (data.event === 'result') {
                    console.log(data.loser)
                    
                    const game = store.state.pk.gameObject
                    const [snake0, snake1] = game.snakes

                    if (data.loser === 'all' || data.loser === 'A') {
                        snake0.state = 'die'
                    }
                    if (data.loser === 'all' || data.loser === 'B') {
                        snake1.state = 'die'
                    }

                    // 更新loser 
                    store.commit("updateLoser", data.loser)
                }
            }

            socket.onclose = () => {
                console.log('disconncted')
            }
        })

        onUnmounted(() => {
            console.log('unmounted')
            socket.close()  // 必须关闭 否则会有冗余链接
            store.commit("updateStatus", "matching")
            // store.commit("updateGameMap", null)
        })

        return {
            store, 
        }
    }, 

    


   }
</script>

<style scoped>
</style>