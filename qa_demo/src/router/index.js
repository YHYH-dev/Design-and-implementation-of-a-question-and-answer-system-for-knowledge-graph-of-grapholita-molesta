import Vue from 'vue'
import Router from 'vue-router'
import Main from '../components/Main.vue'
Vue.use(Router)

export default new Router({
  routes: [
  {
    path:'/',
    redirect:"/main"
  },
    {
      path: '/main',
      name: 'main',
      component: Main,
    }
  ]
})
