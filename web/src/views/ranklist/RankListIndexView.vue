<template>
    <ContentField>
        <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>Players</th>
                    <th>Scores</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ user.username }}</span>
                    </td>
                    <td>{{ user.rating }}</td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_page(-2, $event)">
                <a class="page-link" href="#">Prev</a>
            </li>
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

    export default {
    names: "RecordIndexView", 
    components: {
        ContentField
    },

    setup () {
        const store = useStore();
        let users = ref([]);
        let current_page = 1;
        let total_users = 0;
        let pages = ref([]);

        const click_page = (page, event) => {
            event.preventDefault();
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_users / 10));

            if (page >= 1 && page <= max_pages) {
                pull_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(total_users / 10));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }



        const pull_page = (page)  => {
            current_page = page
            $.ajax({
                url: 'http://localhost:3000/ranklist/getlist/',
                type: 'get',
                data: {
                    page, 
                }, 
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },

                success (resp) {
                    console.log(resp)
                    users.value = resp.users
                    total_users = resp.users_count
                    udpate_pages()
                },
                error (resp) {
                    console.log(resp)
                }

            })
        }

        pull_page(current_page)


        return {
            users, 
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