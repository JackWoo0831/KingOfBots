<template>
    <div class="result-board">
        <div class="result-board-text" v-if="store.state.pk.loser === 'all'" >  <!-- 平局 -->
            Draw!
        </div>
        <div class="result-board-text" v-else-if="store.state.pk.loser === 'A' && store.state.user.id == store.state.pk.a_id" >  <!-- 当前是A且A 就输 当前是B且B 就输 否则都是赢 -->
            Lose!
        </div>
        <div class="result-board-text" v-else-if="store.state.pk.loser === 'B' && store.state.user.id == store.state.pk.b_id" >  <!-- a_id和id 一个是字符串 一个是int 因此两个等号 表示值相等 -->
            Lose! 
        </div>
        <div class="result-board-text" v-else >  
            Win!
        </div>
        <div class="result-board-btn">
            <button type="button" class="btn btn-warning btn-lg" style="margin-top: 20%;" @click="restart"> Try Again </button>
        </div>
    </div>

</template>


<script>
    import { useStore } from 'vuex'
    export default {
        setup () {
            const store = useStore()

            const restart = () => {
                store.commit("updateStatus", "matching")  // 直接改回匹配状态
                store.commit("updateLoser", "none")
                store.commit("updateOpponent", {
                    username: "My competitor", 
                    photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
                })
            }

            return {
                store, 
                restart
            }        
        }
    }

    
</script>

<style>
    div.result-board {
        height: 30vh;
        width: 30vw;
        color: rgba(50, 50, 50, 0.5);
        position: absolute;
        top: 30vh;
        left: 35vw;
    }

    div.result-board-text {
        text-align: center;
        color: white;
        font-size: 50px;
        padding-top: 5vh;
    }
</style>