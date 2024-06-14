<template>
    <div class="playground">
        <GameMap></GameMap>
    </div>
</template>

<script>

import GameMap from './GameMap.vue'  // 地图是在PlayGround的基础上搞得

export default {
    components: {
        GameMap
    }
}

</script>

<style scoped>
    div.playground {
        width: 60vw;
        height: 70vh;
        background: lightblue;
        margin: 40px auto; 
    }
</style>