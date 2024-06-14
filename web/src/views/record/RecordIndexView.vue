<template>
    <ContentField>
        <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>A</th>
                    <th>B</th>
                    <th>Results</th>
                    <th>Time</th>
                    <th>Operation</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id">
                    <td>
                        <img :src="record.a_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.a_username }}</span>
                    </td>
                    <td>
                        <img :src="record.b_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.b_username }}</span>
                    </td>
                    <td>{{ record.result }}</td>
                    <td>{{ record.record.createtime }}</td>
                    <td>
                        <button type="button" class="btn btn-secondary" @click="open_record_content(record.record.id)">View Competition</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <!-- 分页操作 -->
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_page(-2, $event)">
                <a class="page-link" href="#">Prev</a>
            </li>
            <!-- 利用 page.is_activate进行高亮 -->
            <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number, $event)">
                <a class="page-link" href="#">{{ page.number }}</a>
            </li>
            <li class="page-item" @click="click_page(-1, $event)">
                <a class="page-link" href="#">Next</a>
            </li>
        </ul>
        </nav>
    </ContentField>
</template>

<script>
    import ContentField from '../../components/ContentField.vue'
    import { useStore } from 'vuex';
    import { ref } from 'vue'
    import $ from 'jquery'
    import router from '../../router/index';


    export default {
    names: "RecordIndexView", 
    components: {
        ContentField
    },

    setup () {
        const store = useStore() 
        let current_page = 1
        let records = ref([])  // 表示当前的记录
        let total_records = 0
        let pages = ref([]);

        const click_page = (page, event) => {
            event.preventDefault()  // 阻止浏览器的默认刷新行为 不然就回主页了
            if (page === -2) page = current_page - 1
            else if (page === -1) page = current_page + 1
            let max_pages = parseInt(Math.ceil(total_records / 10))

            if (page >= 1 && page <= max_pages) {
                pull_page(page)  // page 合法 获取 当前页信息
            }
        }


        // 更新当前的有效页面 
        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(total_records / 10))
            let new_pages = []
            for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    })
                }
            }
            pages.value = new_pages
        }



        const pull_page = (page)  => {
            current_page = page
            $.ajax({
                url: 'http://localhost:3000/record/getlist/',
                type: 'get',
                data: {
                    page, 
                }, 
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },

                success (resp) {
                    console.log(resp)
                    records.value = resp.records
                    total_records = resp.records_count
                    udpate_pages()
                },
                error (resp) {
                    console.log(resp)
                }

            })
        }

        pull_page(current_page)

        // 将一维字符串转换为 2D map
        const stringTo2D = map => {
            let g = [];
            for (let i = 0, k = 0; i < 13; i ++ ) {
                let line = [];
                for (let j = 0; j < 14; j ++, k ++ ) {
                    if (map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }


        const open_record_content = (recordId) => {
            for (const record of records.value) {
                if (record.record.id === recordId) {
                    // 找到了
                    store.commit("updateIsRecord", true)
                    store.commit("updateGame", {
                        map: stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,

                    })
                    store.commit("updateSteps", {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                    });
                    store.commit("updateRecordLoser", record.record.loser);


                    router.push({
                        name: 'record_content', 
                        params: {
                            recordId: recordId
                        }
                    })

                    break
                }
                
            }
        }



        return {
            store, 
            records, 
            total_records, 
            open_record_content, 
            pages, 
            click_page
        }
    }
   }
</script>

<style>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}

</style>