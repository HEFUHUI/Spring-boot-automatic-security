import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../views/Main.vue'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Main,
    children:[
      {
        path:"/",
        component:Home
      },
      {path:"/user",component:()=>import("../views/pages/User.vue")},
      {path:"/image",component:()=>import("../views/pages/Image.vue")},
      {path:"/role",component:()=>import("../views/pages/Role.vue")},
      {path:"/permissions",component:()=>import("../views/pages/Permission.vue")},
      {path:"/equ",component:()=>import("../views/pages/Equ.vue")},
      {path:"/debug",component:()=>import("../views/pages/DeBug.vue")},
      {path:"/monitor/:id",component:()=>import("../views/pages/MonitorEqu.vue")},
      {path:"/logging",component:()=>import("../views/pages/Logging.vue")},
      {path:"/student",component:()=>import("../views/pages/Student.vue")},
      {path:"/classes",component:()=>import("../views/pages/Classes.vue")},
    ]
  },
  {
    path: '/login',
    meta:{
      isPublic:true
    },
    component: () => import('../views/Login.vue')
  }
]


const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
router.beforeEach((to, from, next) => {
  if (!to.meta.isPublic && !sessionStorage.getItem("token")) {
    return next('/login')
  }
  next()
})

export default router
