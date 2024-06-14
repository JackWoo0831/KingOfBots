import { GameObjects } from "./GameObject";
import { Cell } from "./Cell";

export class Snake extends GameObjects {
    constructor (info, gamemap) {
        // info 蛇有关的信息
        // gamemap 地图

        // 取id和颜色
        super()
        this.color = info.color 
        this.id = info.id 

        this.gamemap = gamemap

        // 记录蛇身
        this.cells = [new Cell (info.r, info.c)]

        // 速度 一帧移动几个格子
        this.speed = 5
        this.direction = -1  // -1 表示没有指令 0 1 2 3表示上 下 左 右
        this.state = 'idle'  // 蛇的状态 idle 表示闲置 moving表示正在移动 die表示死亡

        this.next_cell = null  // 记录蛇下一步走什么地方

        // 行 列 的偏移量
        this.dr = [-1, 1, 0, 0]
        this.dc = [0, 0, -1, 1]

        // 回合数
        this.step = 0

        // 判断两个格子是否重合的允许误差
        this.eps = 1e-2

        // 蛇头朝向 为了画眼睛
        this.eye_direction = this.id === 0 ? 0 : 1

        // 两个眼睛的偏移量 对应 上下左右
        this.eye_dx = [
            [-1, 1],
            [-1, 1],
            [-1, -1],
            [1, 1]
        ]

        this.eye_dy = [
            [-1, -1],
            [1, 1],
            [-1, 1], 
            [-1, 1]
        ]

    }

    start () {

    }

    update_move () {
        // 记录蛇更新的位置

        // 注意一秒刷新若干次 蛇头的移动是连续的 所以蛇头一直要向next cell移动

        // 计算坐标的差
        const dx = this.next_cell.x - this.cells[0].x 
        const dy = this.next_cell.y - this.cells[0].y
        const dist = Math.sqrt(dx * dx + dy * dy)

        if (dist < this.eps) {
            // 到了目的地
            this.cells[0] = this.next_cell 
            this.next_cell = null 
            this.state = 'idle'

            // 如果不需要加蛇尾 就把蛇尾砍掉
            if (!this.check_tail_increasing()) {
                this.cells.pop()
            }

        }
        else {
            // 否则 接着移动 根据几何关系推断
            const interval_dist = this.speed * this.time_delta / 1e3
            this.cells[0].x += interval_dist * dx / dist 
            this.cells[0].y += interval_dist * dy / dist

            // 如果不需要砍蛇尾 也让蛇尾跟着移动
            if (!this.check_tail_increasing()) {
                const k = this.cells.length 
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2]
                const tail_dx = tail_target.x - tail.x 
                const tail_dy = tail_target.y - tail.y 
                const dist_tail = Math.sqrt(tail_dx * tail_dx + tail_dy * tail_dy)
                tail.x += interval_dist * tail_dx / dist_tail 
                tail.y += interval_dist * tail_dy / dist_tail
            }
        }
        
    }

    check_tail_increasing () {
        // 检查尾巴是否要增加
        // 注意当前的游戏设计是 自动增加 而不是吃食物
        if (this.step < 5) return true;
        else if (this.step % 3 == 0) return true;
        else return false;

    }

    set_direction (d) {
        // 设置方向 单独写一个函数是统一接口 方便
        this.direction = d
    }

    update () {
        if (this.state === 'moving') {
            this.update_move()
        }
        this.render()
    }

    next_step () {
        // 蛇的状态更新函数

        const d = this.direction 
        // 根据d对应的方向 判断新的蛇头
        this.next_cell = new Cell (this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d])
        // 清空方向 方向需要用户指定
        this.direction = -1
        this.state = 'moving'

        this.eye_direction = d

        this.step ++

        // 将cell所有元素后退一位 把蛇头让出来
        const num_of_cells = this.cells.length
        for (let i = num_of_cells; i > 0; i --) {
            // 注意 js事直接可以索引到[n]来添加元素的
            // 注意使用deepcopy
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]))
        }

        // 检查 如果nextcell要撞了 就改变状态
        // 牢记这个游戏的时间有两个粒度 宏观（要去哪）和微观（每一帧的更新过程）

        // if (!this.gamemap.check_valid(this.next_cell)) this.state = 'die'
        

    }


    render () {
        const L = this.gamemap.L
        const ctx = this.gamemap.ctx

        ctx.fillStyle = this.color

        // 如果蛇去世 变颜色
        if (this.state === 'die') {
            ctx.fillStyle = 'white'
        }
        
        // 枚举每一个身体
        for (const cell of this.cells) {
            // 画圆
            ctx.beginPath()
            ctx.arc(cell.x * L, cell.y * L, L / 2, 0, 2 * Math.PI)  // 圆心x 圆心y 半径 起始弧度 终止弧度
            ctx.fill()
        }

        // 为了让蛇线条流畅 在圆圈之间两两画一个矩形
        for (let i = 1; i < this.cells.length; i ++) {
            const a = this.cells[i], b = this.cells[i - 1] 

            // 如果两个圆圈很接近 就不用画了
            // 否则 两个cell 要么同一行 要么同一列
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
            if (Math.abs(a.y - b.y) < this.eps) {
                // 同一行
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.5) * L, L, L)
            }
            else {
                // 同一列
                ctx.fillRect((a.x - 0.5) * L, Math.min(a.y, b.y) * L, L, L)
            }
        }

        // 画眼睛
        ctx.fillStyle = 'black'
        for (let i = 0; i < 2; i ++) {
            const eye_x = (this.cells[0].x + 0.25 * this.eye_dx[this.eye_direction][i])* L 
            const eye_y = (this.cells[0].y + 0.25 * this.eye_dy[this.eye_direction][i]) * L 
            ctx.beginPath()
            ctx.arc(eye_x, eye_y, 0.1 * L, 0, 2 * Math.PI)
            ctx.fill()
        }
    }
}