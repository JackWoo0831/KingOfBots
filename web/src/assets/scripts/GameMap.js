import { GameObjects } from "./GameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends GameObjects {
    constructor (ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;

        this.L = 0;  // 每个格子的单位长度

        this.width = 14;  // 宽度为14个单位长度
        this.height = 13;  // 高度同理

        this.walls = []  // 记录墙
        this.num_inner_walls = 10  // 障碍物的个数

        // 蛇
        this.snakes = [
            new Snake ({id: 0, color: '#4876EC', r: this.height - 2, c: 1}, this),
            new Snake ({id: 1, color: '#F94848', r: 1, c: this.width - 2}, this)
        ]

        this.store = store  // 后端传来的 地图数据

        
    }

    start () {
        for (let i = 0; i < 10; i ++) {
            if (this.create_walls()) break;
        }

        this.add_listening()
        

        
    }

    update_size() {
        // 辅助函数 用来动态更新地图大小 地图是正方形 按照画布(PlayGround)的最短边
        this.L = Math.min(this.parent.clientWidth / this.width, this.parent.clientHeight / this.height);  // this.parent.clientWidth是求div的长宽的api
        this.ctx.canvas.width = this.L * this.width;
        this.ctx.canvas.height = this.L * this.height;

    }

    check_legal (g, sx, sy, tx, ty) {
        // 查看是否连通 不连通的话就重新生成
        // sx sy 起点 tx ty 终点
        // DFS 
        
        if (sx == tx && sy == ty) return true
        g[sx][sy] = true

        const dx = [-1, 1, 0, 0]
        const dy = [0, 0, -1, 1]

        for (let i = 0; i < 4; i ++) {
            for (let j = 0; j < 4; j ++) {
                let x = sx + dx[i]
                let y = sy + dy[j]
                
                if (!g[x][y] && this.check_legal(g, x, y, tx, ty)) return true

            }
        } 

        return false

    }

    create_walls () {
        // 如果后端有数据 直接读取
        const g = this.store.state.pk.gamemap

        for (let r = 0; r < this.height; r ++) {
            for (let c = 0; c < this.width; c ++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this))
                }
            }
        }

        return true
        
        /*
        // 创建墙 
        
        const g = []  // bool 数组 标记是否有墙

        // 初始化
        for (let r = 0; r < this.height; r ++) {
            g[r] = []
            for (let c = 0; c < this.width; c ++) {
                g[r][c] = false
            }
        }

        // 四周加墙
        for (let r = 0; r < this.height; r ++) {
            g[r][0] = true
            g[r][this.width - 1] = true
        }
        for (let c = 0; c < this.width; c ++) {
            g[0][c] = true
            g[this.height - 1][c] = true
        }

        // 加随机墙
        for (let i = 0; i < this.num_inner_walls; i ++) {
            // 跑充分多次 用随机数生成位置
            for (let j = 0; j < 100; j ++) {
                let r = parseInt(Math.random() * this.height)
                let c = parseInt(Math.random() * this.width)
                
                // 重复或在生成在了起点  注意 是中心对称
                if (g[r][c] || g[this.height - 1 - r][this.width - 1 - c]) continue
                if (r == this.height - 2 && c == 1 || r == 1 && c == this.width - 2) continue

                g[r][c] = g[this.height - 1 - r][this.width - 1 - c] = true
                break
            }
        }
        
        // 检查之前 先备份
        const g_ = JSON.parse(JSON.stringify(g))
        if (!this.check_legal(g_, this.height - 2, 1, 1, this.width - 2)) return false

        for (let r = 0; r < this.height; r ++) {
            for (let c = 0; c < this.width; c ++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this))
                }
            }
        }

        return true
        */
    }

    update () {
        this.update_size()
        
        if (this.check_ready()) {
            this.next_step()
        }
        this.render()
    }

    check_ready () {
        // 看看两条蛇是否都准备好了 进行下一个回合
        // 注意 用户的操作必须都放到后端进行计算

        for (const snake of this.snakes) {
            if (snake.state !== 'idle') return false;
            if (snake.direction === -1) return false;

        }

        return true

    }

    next_step () {
        // 进入下一回合的函数
        for (const snake of this.snakes) {
            snake.next_step()  // 更新蛇位置和状态
        }
    }

    add_listening () {
        // 判断当前是播放历史 还是对战
        if (this.store.state.record.is_record) {
            // 每300ms播放一次操作
            let k = 0 
            const a_steps = this.store.state.record.a_steps
            const b_steps = this.store.state.record.b_steps
            const loser = this.store.state.record.record_loser
            const [snake0, snake1] = this.snakes;
            const interval_id = setInterval(() => {
                if (k >= a_steps.length - 1) {
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);  // 取消循环
                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k ++ ;
            }, 300);

        }
        else {
            // 监听键盘操作
            this.ctx.canvas.focus()

            this.ctx.canvas.addEventListener('keydown', e => {
                console.log(e.key)
                let d = -1
                if (e.key === 'w') d = 0;
                else if (e.key === 's') d = 1;
                else if (e.key === 'a') d = 2;
                else if (e.key === 'd') d = 3;

                // 往后端发消息
                if (d >= 0) {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: 'move', 
                        direction: d, 
                    }))
                }


                /*
                if (e.key === 'w') snake0.set_direction(0);
                else if (e.key === 's') snake0.set_direction(1);
                else if (e.key === 'a') snake0.set_direction(2);
                else if (e.key === 'd') snake0.set_direction(3);
                else if (e.key === 'i') snake1.set_direction(0);
                else if (e.key === 'k') snake1.set_direction(1);
                else if (e.key === 'j') snake1.set_direction(2);
                else if (e.key === 'l') snake1.set_direction(3);
                */
            })
        }

        

    }

    check_valid (cell) {
        // 检测某个cell是否合法
        // 即 不能是墙或者蛇的身体
        for (const wall of this.walls) {
            if (cell.r === wall.r && cell.c === wall.c) {
                return false
            }
        }

        // 判断蛇 注意 如果是追蛇尾的话 蛇尾可能会缩 就撞不上 需要特判一下
        for (const snake of this.snakes) {
            let k = snake.cells.length

            if (!snake.check_tail_increasing()) k --;

            for (let i = 0; i < k; i ++) {
                if (cell.r === snake.cells[i].r && cell.c === snake.cells[i].c) return false
            }
        }

        return true
    }


    render () {
        // 渲染 画图
        // this.ctx.fillStyle = 'green'
        // this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.width)

        const color_even = '#AAD751'  // 偶数格子的颜色
        const color_odd = '#A2D149'  // 奇数格子的颜色

        for (let r = 0; r < this.height; r ++) {
            for (let c = 0; c < this.width; c ++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even
                }
                else {
                    this.ctx.fillStyle = color_odd
                }
                
                // 定坐标 注意canvas的坐标系是反的 横着向右是x 竖着向下是y
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L)
            }
        }

    }
}