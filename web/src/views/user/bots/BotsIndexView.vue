<template>
    <div class="container">  <!-- container可以实现动态变化 -->
        <div class="row">
            <div class="col-3">  <!-- bootstrap的col分12列 头像占3列 -->
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>

                </div>
            </div>

            <div class="col-9">  <!-- 用户的bot信息占9列 -->
                <div class="card"  style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%;"> My Bots </span>  <!-- span 调节字体格式 -->
                        <!-- float-end表示向右对齐 -->
                        <button type="button" class="btn btn-primary float-end" style="width: 40%;" data-bs-toggle="modal" data-bs-target="#add_bot">
                            Add a bot
                        </button>

                        <!-- 悬浮框 bootstrap中的 modal  -->
                        <!-- Modal -->
                        <div class="modal fade" id="add_bot" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-xl">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">Create a bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="add-bot-title" class="form-label">Bot title</label>
                                    <input type="text" v-model="add_bot.title" class="form-control" id="add-bot-title" placeholder="Bot name">
                                </div>
                                <div class="mb-3">
                                    <label for="add-bot-description" class="form-label">Bot Desciption</label>
                                    <textarea v-model="add_bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="Add Description"></textarea>
                                </div>     
                                <div class="mb-3">
                                    <label for="add-bot-code" class="form-label">Bot Code</label>
                                    <!-- 代码编辑器部分 -->>
                                    <VAceEditor
                                        v-model:value="add_bot.content"
                                        @init="editorInit"
                                        lang="c_cpp"
                                        theme="textmate"
                                        style="height: 300px" />
                                </div>                            
                            </div>
                            <div class="modal-footer">
                                <div class="error-message" style="color: red;">{{ add_bot.error_message }}</div>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="width: 20%;">Cancel</button>
                                <button type="button" class="btn btn-primary" style="width: 20%;" @click="add_bot_request">Create</button>
                            </div>
                            </div>
                        </div>
                        </div>
                    </div>
                    
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>
                                        Bot Name
                                    </th>
                                    <th>
                                        Create Time
                                    </th>
                                    <th>
                                        Operation
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>
                                        {{ bot.title }}
                                    </td>
                                    <td>
                                        {{ bot.createtime }}
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px; width: 40%;" data-bs-toggle="modal" :data-bs-target="'#modify_bot' + bot.id">Modify</button>
                                        <button type="button" class="btn btn-danger" style="width: 40%;" @click="remove_bot_request(bot)">Delete</button>

                                        <!-- 悬浮框 bootstrap中的 modal  -->
                                        <!-- 修改bot 用到的模态框 -->
                                        <!-- 每一个bot 用到的都不是同一个模态框 因此 模态框需要用不同的id  :id="bot.id"-->
                                        <div class="modal fade" :id="'modify_bot' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-xl">
                                            <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5" id="exampleModalLabel">Modify a bot</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="mb-3">
                                                    <label for="add-bot-title" class="form-label">Bot title</label>
                                                    <input type="text" v-model="bot.title" class="form-control" id="add-bot-title" placeholder="Bot name">
                                                </div>
                                                <div class="mb-3">
                                                    <label for="add-bot-description" class="form-label">Bot Desciption</label>
                                                    <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="Add Description"></textarea>
                                                </div>     
                                                <div class="mb-3">
                                                    <label for="add-bot-code" class="form-label">Bot Code</label>
                                                    <!-- 代码编辑器部分 -->>
                                                    <VAceEditor
                                                        v-model:value="bot.content"
                                                        @init="editorInit"
                                                        lang="c_cpp"
                                                        theme="textmate"
                                                        style="height: 300px" />
                                                </div>                            
                                            </div>
                                            <div class="modal-footer">
                                                <div class="error-message" style="color: red;">{{ bot.error_message }}</div>
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="width: 20%;">Cancel</button>
                                                <button type="button" class="btn btn-primary" style="width: 20%;" @click="modify_bot_request(bot)">Save Changes</button>
                                            </div>
                                            </div>
                                        </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { useStore } from 'vuex'
    import { ref } from 'vue'
    import { reactive } from 'vue'  // 用于绑定变量
    import $ from 'jquery'
    import { Modal } from 'bootstrap/dist/js/bootstrap'
    import { VAceEditor } from 'vue3-ace-editor';  // 代码编辑框的依赖
    import ace from 'ace-builds';

    export default {
    names: "BotsIndexView", 
    components: {
        VAceEditor
    }, 


    setup () {
        const store = useStore()  // 本地存储 token
        let bots = ref([])

        // 代码编辑框的相应操作
        ace.config.set(
        "basePath", 
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

        const add_bot = reactive({
            title: "", 
            description: "", 
            content: "", 
            error_message: ""
        })

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

        // 向后端发送增加bot的请求
        const add_bot_request = () => {
            add_bot.error_message = ""
            $.ajax({
                url: 'http://localhost:3000/user/bot/add/',
                type: 'post',
                data: {
                    title: add_bot.title,
                    description: add_bot.description,
                    content: add_bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },

                success (resp) {
                    console.log(resp)
                    if (resp.error_message === "success") {
                        // 如果成功 就刷新bot列表
                        refresh_bot()
                        // 把输入的内容清空
                        add_bot.title = ""
                        add_bot.description = ""
                        add_bot.content = ""
                        // 把modal框 收回去
                        Modal.getInstance("#add_bot").hide()  // 注意 # 号

                    }
                    else {
                        // 否则 将报错打印出来
                        console.log(resp.error_message)
                        add_bot.error_message = resp.error_message
                    }
                },
                error (resp) {
                    console.log(resp)
                }
            })
        }
        
        // 向后端发送删除bot请求
        const remove_bot_request = (bot) => {
            $.ajax({
                url: 'http://localhost:3000/user/bot/remove/',
                type: 'post',
                data: {
                    bot_id: bot.id
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },

                success (resp) {
                    console.log(resp)
                    if (resp.error_message === "success") {
                        // 如果成功 就刷新bot列表
                        refresh_bot()
                        
                    }
                },
                error (resp) {
                    console.log(resp)
                }

            })
        }

        // 向后端发送修改bot的请求
        const modify_bot_request = (bot) => {
            add_bot.error_message = ""
            $.ajax({
                url: 'http://localhost:3000/user/bot/update/',
                type: 'post',
                data: {
                    bot_id: bot.id, 
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token
                },

                success (resp) {
                    console.log(resp)
                    if (resp.error_message === "success") {
                        // 如果成功 就刷新bot列表
                        refresh_bot()
                        // 修改也是要在弹出的modal框进行修改 不需要把modal框清空
                        // 把modal框 收回去
                        Modal.getInstance('#modify_bot' + bot.id).hide()  // 注意 # 号

                    }
                    else {
                        // 否则 将报错打印出来
                        console.log(resp.error_message)
                        bot.error_message = resp.error_message
                    }
                },
                error (resp) {
                    console.log(resp)
                }
            })
        }


        return {
            bots, 
            store, 
            add_bot, 
            add_bot_request, 
            remove_bot_request, 
            modify_bot_request, 
        }
    }
   }
</script>

<style>
</style>