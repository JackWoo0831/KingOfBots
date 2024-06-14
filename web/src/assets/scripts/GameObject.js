const GAME_OBJECTS = []

export class GameObjects {
    constructor () {
        GAME_OBJECTS.push(this)
        this.has_init = false
        this.time_delta = 0
    }

    start () {  // 第一帧执行

    }

    update () {  // 除了第一帧 后面每一帧执行

    }

    on_destroy () {  // 毁灭前执行

    }

    destroy () {  // 将当前对象从GAME_OBJECTS中删除
        this.on_destroy()

        for (let i in GAME_OBJECTS) {
            const obj = GAME_OBJECTS[i]  // js中 非必要不加分号
            if (obj === this) {
                GAME_OBJECTS.splice(i)
                break
            }
        }

    }
}

let last_timestamp = 0
const step = timestamp => {
    for (let obj of GAME_OBJECTS) {  // 后加进来的 后更新 所以可以先画地图 再画墙
        if (!obj.has_init) {
            obj.has_init = true
            obj.start()
        }
        else {
            obj.time_delta = timestamp - last_timestamp
            obj.update()
        }
    }
    last_timestamp = timestamp
    requestAnimationFrame(step)
}

requestAnimationFrame(step)