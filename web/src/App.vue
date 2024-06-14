<template>
  <NavBar />
  <router-view/>
</template>

<script>
  import NavBar from "./components/NavBar.vue"
  import "bootstrap/dist/css/bootstrap.min.css"
  import "bootstrap/dist/js/bootstrap"
  // import $ from 'jquery'

  export default {
    components: {
      NavBar
    },
    
    setup () {

      
      /*
      $.ajax({
        url: "http://localhost:3000/user/account/token/",
        type: 'post', 
        data: {
          username: 'root', 
          password: 'root', 
        }, 
        success (resp) {
          console.log(resp)
        },
        error (resp) {
          console.log(resp)
        }
      }) */

      /*
      $.ajax({
        url: "http://localhost:3000/user/account/info/", 
        type: 'get', 
        // header协议参见 com/kob/backend/configs/filter/JwtAuthenticationTokenFilter.java
        headers: {
          Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjI0ZDQ4OTAyNGY0ZGEyOWNmN2EwMDJlYzlhNzUwMSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcxNzUwODMxMywiZXhwIjoxNzE4NzE3OTEzfQ.T3HrhEooi9qjaFre-v_ZZkVP9MrAx1NvGufJkecRec0", 
        }, 
        success (resp) {
          console.log(resp)
        },
        error (resp) {
          console.log(resp)
        }
      }) */
      
      /*
      $.ajax({
        url: "http://localhost:3000/user/account/register/", 
        type: 'post', 
        data: {
          username: 'root', 
          password: 'root', 
          confirmPassword: 'root'
        }, 
        success (resp) {
          console.log(resp)
        }, 
        error (resp) {
          console.log(resp)
        }
      })
      */

    }
  }
</script>

<style>
  body {
    background-image: url('./assets/images/background.jpg');
    background-size: cover;
  }
</style>
