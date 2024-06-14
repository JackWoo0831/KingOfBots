// 存储匹配界面需要的信息

/*
    将用户的信息以全局变量的形式储存 借助vuex
*/ 
export default {
    state: {
        status: "matching",  // matching表示匹配界面，playing表示对战界面
        socket: null,
        opponent_username: "",  // 对手的id
        opponent_photo: "",  // 对手的photo
        gamemap: null,  // 地图

        // 存下当前对战的两个用户的信息 id 起始位置
        a_id: 0, 
        a_sx: 0, 
        a_sy: 0, 

        b_id: 0, 
        b_sx: 0, 
        b_sy: 0, 

        gameObject: null,  // 实例化 src\assets\scripts\GameMap.js

        loser: 'none'  // none all A B
    },
    getters: {
    },
    mutations: {  // 放一些修改内容的辅助函数
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGame(state, game) {
            state.gamemap = game.map
            state.a_id = game.a_id
            state.a_sx = game.a_sx 
            state.a_sy = game.a_sy 

            state.b_id = game.b_id 
            state.b_sx = game.b_sx 
            state.b_sy = game.b_sy
        }, 
        updateGameObject(state, gameObject) {
           state.gameObject = gameObject 
        }, 
        updateLoser(state, loser) {
            state.loser = loser
        }

    },
    actions: {
        // 一些行为 例如 登录 和后端交互
    },
    modules: {
    }
}