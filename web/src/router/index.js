import { createRouter, createWebHashHistory } from 'vue-router'
import PKIndexView from '../views/pk/PKIndexView.vue'
import RankListIndexView from '../views/ranklist/RankListIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import RecordContentView from '../views/record/RecordContentView.vue'
import BotIndexView from '../views/user/bots/BotsIndexView.vue'
import ErrorIndex from '../views/error/ErrorIndex.vue'

import UserAccountLoginView from '../views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView.vue'

import store from '../store/index'

const routes = [
  {
    path: '/',
    redirect: '/pk/', 
    name: 'home', 
    meta: {
      requestAuth: true,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },
  {
    path: '/pk/',
    name: 'pk_index', 
    component: PKIndexView, 
    meta: {
      requestAuth: true,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },
  {
    path: '/record/',
    name: 'record_index', 
    component: RecordIndexView, 
    meta: {
      requestAuth: true,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },

  {
    path: '/record/:recordId/',  // : 表示 参数
    name: 'record_content', 
    component: RecordContentView, 
    }, 


  {
    path: '/rank/',
    name: 'rank_index', 
    component: RankListIndexView, 
    meta: {
      requestAuth: true,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },
  {
    path: '/user/bot/',
    name: 'bot_index', 
    component: BotIndexView, 
    meta: {
      requestAuth: true,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },
  {
    path: '/404/',
    name: 'error_index', 
    component: ErrorIndex, 
    meta: {
      requestAuth: false,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  }, 
  {
    path: '/:catchAll(.*)', 
    redirect: '/404/'
  }, 

  {
    path: '/user/account/login/',
    name: 'user_login', 
    component: UserAccountLoginView, 
    meta: {
      requestAuth: false,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },

  {
    path: '/user/account/register/',
    name: 'user_register', 
    component: UserAccountRegisterView, 
    meta: {
      requestAuth: false,  // 是否需要授权 是的话 就直接跳到登录页面
    }
  },

]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach ((to, from, next) => {  // beforeEach是在每次跳转之前执行某些动作
  // to 是要去的页面 from是来的页面 next是下一步的动作
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({name: 'user_login'})  // 跳转到登陆页面
  }
  else {
    next()
  }

})

export default router
