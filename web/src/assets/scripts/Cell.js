// 定义蛇的每个格子

export class Cell {
    constructor (r, c) {
        // 注意转换canvas坐标系 横着向右是x 竖着向下是y
        this.r = r
        this.c = c 
        this.x  = c + 0.5 
        this.y = r + 0.5
    }
}